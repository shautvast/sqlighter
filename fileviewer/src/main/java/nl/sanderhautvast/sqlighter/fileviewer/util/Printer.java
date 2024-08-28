package nl.sanderhautvast.sqlighter.fileviewer.util;

import java.util.StringJoiner;

/**
 * debug util
 */
public class Printer {

    public static String printHex(byte[] bytes) {
        StringJoiner s = new StringJoiner(" ");
        for (byte aByte : bytes) {
            s.add(leftpad(Long.toString(aByte & 0xFF, 16).toUpperCase()));
        }

        return s.toString();
    }

    private static String leftpad(String text) {
        return String.format("%1$2s", text).replace(' ', '0');
    }
}
