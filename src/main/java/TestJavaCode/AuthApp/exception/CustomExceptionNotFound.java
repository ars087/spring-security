package TestJavaCode.AuthApp.exception;

public class CustomExceptionNotFound extends RuntimeException {
    public CustomExceptionNotFound(String message) {
        super(message);
    }
}
