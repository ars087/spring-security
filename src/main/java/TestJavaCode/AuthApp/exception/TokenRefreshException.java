package TestJavaCode.AuthApp.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshException extends  RuntimeException {
    String token;
    String message;

    public TokenRefreshException(String token,String message) {
        super(message);
        this.token =token;
        this.message = message;
    }
}
