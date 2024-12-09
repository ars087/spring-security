package TestJavaCode.AuthApp.logger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "log_entry")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @Column(name = "message_info")
    private String messageInfo;
    @Column(name = "message_info_second")
    private String messageInfoSecond;
    private String timestamp;

}
