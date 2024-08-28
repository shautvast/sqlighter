package nl.sanderhautvast.sqlighter.fileviewer;

import nl.sanderhautvast.sqlighter.fileviewer.model.Metadatabase;
import nl.sanderhautvast.sqlighter.fileviewer.page.PageReader;
import nl.sanderhautvast.sqlighter.fileviewer.page.RootpageReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Outputs the lowlevel structure of the database file as json object.
 * Meant to be piped into jq / other.
 * <p>
 * <p>
 * Can/should be extended to find more corruption in sqlite files.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String filename = args[0]; //TODO cmdline arg validation

        // first 100 bytes contain the header part with the actual pagesize used, which is what we'll use as
        // size of the bytebuffer as well. So first we read 100 bytes, then reopen the file
        // reading the whole page (excluding the header) and then the following pages
        Metadatabase metadatabase = readRootPage(filename);

        ByteBuffer buffer = ByteBuffer.allocate(metadatabase.getPagesize());
        PageReader pageReader = new PageReader(metadatabase);

        try (FileInputStream file = new FileInputStream(filename)) {
            for (long pageNumber = 1; ; pageNumber += 1) {
                buffer.position(0);
                int nread = file.getChannel().read(buffer);

                if (nread == 0) {
                    break; //regular end
                } else if (nread < metadatabase.getPagesize()) {
                    break;
                } else {
                    buffer.position(0);
                    if (pageNumber == 1) {
                        SchemaReader schemaReader = new SchemaReader(metadatabase);
                        metadatabase.getSchemaRecords().addAll(schemaReader.readSchema(buffer));
                    } else {
                        pageReader.readPage(buffer, pageNumber);
                    }
                }
            }
        }
        System.out.println(metadatabase.report());
    }

    private static Metadatabase readRootPage(String filename) throws IOException {
        try (FileInputStream file = new FileInputStream(filename)) {
            ByteBuffer headerDataBUffer = ByteBuffer.allocate(0x100);
            file.getChannel().read(headerDataBUffer);
            return RootpageReader.read(headerDataBUffer);
        }
    }
}
