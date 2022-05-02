package chess.controller;

import chess.dao.BoardDao;
import chess.dao.BoardDaoImpl;
import chess.dao.RoomDao;
import chess.dao.RoomDaoImpl;
import chess.domain.state.BoardInitialize;
import org.springframework.jdbc.core.JdbcTemplate;

public class FakeDao {

    private final JdbcTemplate jdbcTemplate;

    public FakeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setUpTable() {
        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("DROP TABLE room IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS room" +
            "(" +
            "  id bigint AUTO_INCREMENT," +
            "  team varchar(50) NOT NULL," +
            "  title varchar(50) NOT NULL," +
            "  password varchar(50) NOT NULL," +
            "  status boolean," +
            " PRIMARY KEY (id)" +
            ")");

        jdbcTemplate.execute("CREATE TABLE board(\n" +
            "  id bigint NOT NULL AUTO_INCREMENT,\n" +
            "  position varchar(50) NOT NULL,\n" +
            "  symbol varchar(50) NOT NULL,\n" +
            "  room_id bigint NOT NULL,\n" +
            "  PRIMARY KEY (id),\n" +
            "  CONSTRAINT id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE ON UPDATE CASCADE)");
    }

    public void createRoom(String title, String password) {
        RoomDao roomDao = new RoomDaoImpl(jdbcTemplate);
        Long roomId = roomDao.save(title, password);
        saveAllAtBoard(roomId);
    }

    private void saveAllAtBoard(Long roomId) {
        BoardDao boardDao = new BoardDaoImpl(jdbcTemplate);
        boardDao.saveAll(BoardInitialize.create(), roomId);
    }
}
