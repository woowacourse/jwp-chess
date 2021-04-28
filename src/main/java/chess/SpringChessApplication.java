package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }

}
