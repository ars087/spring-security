package TestJavaCode.AuthApp.exception;

public class CustomBadCredentialsExceptions extends  RuntimeException{
    public CustomBadCredentialsExceptions(String message) {
        super(message);
    }
}
