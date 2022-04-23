package chess.dao;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcFixture {
    public static void dropTable(JdbcTemplate jdbcTemplate, String tableName) {
        jdbcTemplate.execute("DROP TABLE " + tableName + " IF EXISTS");
    }

    public static void createRoomTable(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("create table room ("
                + " id bigint not null auto_increment,"
                + " name VARCHAR(255) not null,"
                + " turn varchar(10) not null,"
                + " primary key (id),"
                + " constraint uniqueName unique (name))");
    }

    public static void createSquareTable(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(
                "create table square ("
                        + " id bigint not null auto_increment,"
                        + " position varchar(5) not null,"
                        + " piece varchar(20) not null,"
                        + " room_id bigint not null,"
                        + " primary key (id),"
                        + " foreign key (room_id) references room (id))");
    }

    public static void insertRoom(JdbcTemplate jdbcTemplate, String name, String turn) {
        jdbcTemplate.update("INSERT INTO room(name, turn) VALUES (?,?)", name, turn);
    }

    public static void insertSquares(JdbcTemplate jdbcTemplate, List<String> squares) {
        List<Object[]> pieces = squares
                .stream()
                .map(piece -> piece.split(","))
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into square (position, piece, room_id) values (?, ?, ?)", pieces);
    }
}
