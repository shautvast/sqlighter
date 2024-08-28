package nl.sanderhautvast.sqlighter.fileviewer.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public enum Encoding implements HasCode {

    UTF_8(1, StandardCharsets.UTF_8),
    UTF_16LE(2, StandardCharsets.UTF_16LE),
    UTF_16BE(3, StandardCharsets.UTF_16BE);


    private final int code;
    private final Charset charset;

    Encoding(int code, Charset charset) {
        this.code = code;
        this.charset = charset;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static Encoding fromCode(int code) {
        return HasCode.getFromCode(Encoding.class, code);
    }

    public Charset toCharset() {
        return charset;
    }
}
