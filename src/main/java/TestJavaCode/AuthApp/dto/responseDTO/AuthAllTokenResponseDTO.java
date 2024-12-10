package TestJavaCode.AuthApp.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAllTokenResponseDTO {
    String tokenAccess;
    String tokenRefresh;
}
