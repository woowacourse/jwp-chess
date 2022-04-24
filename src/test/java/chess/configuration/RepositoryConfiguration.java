package chess.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import chess.repository.BoardRepository;
import chess.repository.PieceRepository;
import chess.repository.RoomRepository;

@TestConfiguration
public class RepositoryConfiguration {

	@Bean
	public RoomRepository roomRepository() {
		return new FakeRoomRepository();
	}

	@Bean
	public BoardRepository boardRepository() {
		return new FakeBoardRepository();
	}

	@Bean
	public PieceRepository pieceRepository() {
		return new FakePieceRepository();
	}
}
