import ch.qos.logback.classic.PatternLayout

import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.WARN

appender("STDOUT", ConsoleAppender) {
    layout(PatternLayout) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
}
logger("org.springframework", WARN)
logger("com.kramphub", INFO)
root(INFO, ["STDOUT"])
