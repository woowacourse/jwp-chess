package chess;

import chess.dao.ChessPieceDao;
import chess.dao.RoomDao;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import util.FakeChessPieceDao;
import util.FakeRoomDao;

@TestConfiguration
public class TestConfig {

    @Bean
    RoomDao roomDao() {
        return new FakeRoomDao();
    }

    @Bean
    ChessPieceDao chessPieceDao() {
        return new FakeChessPieceDao();
    }
}
