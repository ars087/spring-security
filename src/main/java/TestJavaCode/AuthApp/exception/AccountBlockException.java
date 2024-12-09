package TestJavaCode.AuthApp.exception;

public class AccountBlockException extends  RuntimeException {

    public AccountBlockException(String message) {
        super(message);
    }
}
