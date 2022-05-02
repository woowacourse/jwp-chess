package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static spark.Spark.staticFileLocation;

@SpringBootApplication
public class SpringChessApplication {
    public static void main(String[] args) {
        staticFileLocation("/public");
        SpringApplication.run(SpringChessApplication.class, args);
    }
}
