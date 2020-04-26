package wooteco.chess.database;

import java.sql.Connection;

// TODO: 2020/04/25 명명 수정하기 -> DataSource
public interface DataSource {

	Connection getConnection();

}
