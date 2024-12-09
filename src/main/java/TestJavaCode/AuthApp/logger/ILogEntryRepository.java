package TestJavaCode.AuthApp.logger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ILogEntryRepository extends JpaRepository<LogEntry, Long> {
}
