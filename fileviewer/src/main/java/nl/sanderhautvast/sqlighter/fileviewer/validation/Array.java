package nl.sanderhautvast.sqlighter.fileviewer.validation;

// TODO use
public class Array {
    public static Result compare(byte[] a1, byte[] a2) {
        if (a1 == null) {
            if (a2 == null) {
                return new Result(true, "both are null");
            } else {
                return new Result(false, "first is null, second is non-null");
            }
        } else {
            if (a2 == null) {
                return new Result(false, "first is non-null, second is null");
            }
        }
        if (a1.length != a2.length) {
            return new Result(false, "arrays do not have equal lengths: " + a1.length + " vs: " + a2.length);
        } else {
            for (int i = 0; i < a1.length; i++) {
                if (a1[i] != a2[i]) {
                    return new Result(false, "arrays differ at index: " + i);
                }
            }
            return new Result(true, "Same");
        }
    }

    static class Result {
        final boolean areEqual;
        final String userMessage;

        Result(boolean areEqual, String userMessage) {
            this.areEqual = areEqual;
            this.userMessage = userMessage;
        }
    }
}
