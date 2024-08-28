package nl.sanderhautvast.sqlighter.fileviewer.page;

import nl.sanderhautvast.sqlighter.fileviewer.model.Metadatabase;
import nl.sanderhautvast.sqlighter.fileviewer.util.Encoding;

import java.nio.ByteBuffer;

import static nl.sanderhautvast.sqlighter.fileviewer.util.UnsignedIntReader.*;

public class RootpageReader {

    public static Metadatabase read(ByteBuffer in) {
        in.position(0); //seems clearer to me than using reset/flip etc
        in.limit(100);

        Metadatabase m = new Metadatabase();
        m.setMagicHeader(readBytes(in, 16));
        m.setPagesize(readU16(in));
        m.setWriteversion(readU8(in));
        m.setReadversion(readU8(in));
        m.setUnusedPerPageSize(readU8(in));
        m.setMaxEmbeddedPayloadFraction(readU8(in));
        m.setMinEmbeddedPayloadFraction(readU8(in));
        m.setLeafPayloadFraction(readU8(in));
        m.setFileChangeCounter(readU32(in));
        m.setSizeInPages(readU32(in));
        m.setPageNrFirstFreelistTrunkpage(readU32(in));
        m.setTotalNrOfFreelistPages(readU32(in));
        m.setSchemaCookie(readU32(in));
        m.setSchemaCookie(readU32(in));
        m.setDefaultPageCachesize(in.getInt()); // it's signed say the docs
        m.setPageNrLargestRootBtreePage(readU32(in));
        m.setEncoding(Encoding.fromCode((int) readU32(in))); // no truncate expected
        m.setUserVersion(readU32(in));
        m.setIncrementalVacuumMode(readU32(in) > 0);
        m.setApplicationID(readU32(in));
        m.setExpansion(readBytes(in, 20));
        m.setVersionValidFor(readU32(in));
        m.setSqliteVersion(readU32(in));
        return m;
    }

    static byte[] readBytes(ByteBuffer in, int length) {
        byte[] header = new byte[length];
        in.get(header);
        return header;
    }
}
