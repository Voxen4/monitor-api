package pw.keller.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * api <p>
 * de.clerkvest.api <p>
 * Application.java <p>
 * <p>
 * Entry point of the application.
 * Starts the Spring Boot 2.2.2 application with custom start up code.
 *
 * @author Danny B.
 * @version 1.0
 * @since 21 Dec 2019 18:11
 */
@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

    /**
     * Main method of the application.
     *
     * @param args Arguments given to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Custom start up code before springs starts.
     *
     * @param args Arguments given to the application
     */
    @Override
    public void run(String... args) {
    }
}
