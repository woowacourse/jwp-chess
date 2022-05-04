package chess;

import chess.repository.ChessGameRepository;
import chess.repository.RoomRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import util.FakeChessGameRepository;
import util.FakeRoomRepository;

@TestConfiguration
public class TestConfig {

    @Bean
    RoomRepository roomRepository() {
        return new FakeRoomRepository();
    }

    @Bean
    ChessGameRepository chessGameRepository() {
        return new FakeChessGameRepository();
    }
}
