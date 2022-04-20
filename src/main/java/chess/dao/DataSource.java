package chess.dao;

import java.sql.Connection;
import org.springframework.stereotype.Repository;

public interface DataSource {
    
    Connection connection();
}
