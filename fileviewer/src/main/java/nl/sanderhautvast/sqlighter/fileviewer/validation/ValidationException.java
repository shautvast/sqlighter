package nl.sanderhautvast.sqlighter.fileviewer.validation;

public class ValidationException extends RuntimeException{

    public ValidationException(String message){
        super(message);
    }
}
