package wooteco.chess.dao;

import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public interface Connectable {
	Connection getConnection();
}
