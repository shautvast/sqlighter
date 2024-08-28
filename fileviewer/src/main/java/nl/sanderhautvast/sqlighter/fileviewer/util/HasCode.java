package nl.sanderhautvast.sqlighter.fileviewer.util;

public interface HasCode {
    static <E extends HasCode> E getFromCode(Class<E> type, int code) {
        for (E candidate : type.getEnumConstants()) {
            if (candidate.getCode() == code) {
                return candidate;
            }
        }

        throw new IllegalArgumentException("Cannot create " + type.getName() + " from the code " + code);
    }

    int getCode();
}
