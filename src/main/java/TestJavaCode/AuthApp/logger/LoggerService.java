package TestJavaCode.AuthApp.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
@Getter
@Setter
public class LoggerService {


    private final  ILogEntryRepository logEntryRepository;

    public void log(String message,String request ) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LogEntry logEntry = new LogEntry();
        logEntry.setMessage(message);
        logEntry.setMessageInfo(request);
        logEntry.setTimestamp(timestamp);

        logEntryRepository.save(logEntry);
    }


    public   void logAction(String message, String request, String requestUri){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LogEntry logEntry = new LogEntry();
        logEntry.setMessage(message);
        logEntry.setMessageInfo(request);
        logEntry.setMessageInfoSecond(requestUri);
        logEntry.setTimestamp(timestamp);
        logEntryRepository.save(logEntry);


    }


}
