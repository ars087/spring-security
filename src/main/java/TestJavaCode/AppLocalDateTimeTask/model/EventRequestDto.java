package TestJavaCode.AppLocalDateTimeTask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {
    private LocalDateTime eventDateTime;
}
