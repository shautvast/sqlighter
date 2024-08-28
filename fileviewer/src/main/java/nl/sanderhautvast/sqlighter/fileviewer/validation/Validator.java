package nl.sanderhautvast.sqlighter.fileviewer.validation;

public class Validator {
    public static void validate(String message, Object v1, Object v2) {
        if (!v1.equals(v2)) {
            throw new ValidationException(message + ": Not OK: " + v1 + " != " + v2);
        }
    }

    //TODO use or remove
    public static void notNull(String message, Object value) {
        if (value == null) {
            throw new ValidationException("Not OK: " + message + ": is NULL");
        }
    }
}
