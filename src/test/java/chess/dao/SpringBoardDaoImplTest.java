package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("application.properties")
class SpringBoardDaoImplTest {
    // @Value("${spring.datasource.driver-class-name}")
    // private String driverClassName;
    // @Value("${spring.datasource.url}")
    // private String url;
    // @Value("${spring.datasource.username}")
    // private String username;
    // @Value("${spring.datasource.password}")
    // private String password;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/chess?serverTimezone=UTC&characterEncoding=UTF-8");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    void name() {
        SpringBoardDaoImpl springBoardDao = new SpringBoardDaoImpl(jdbcTemplate);
        springBoardDao.getBoard("game-id");
    }
}
