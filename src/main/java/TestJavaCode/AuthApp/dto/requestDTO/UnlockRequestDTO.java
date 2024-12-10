package TestJavaCode.AuthApp.dto.requestDTO;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnlockRequestDTO {
    @Valid
    String userName;
}
