package chess.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import chess.repository.BoardRepository;
import chess.repository.PieceRepository;
import chess.service.GameService;

@TestConfiguration
public class ServiceConfiguration {

	@Bean
	public GameService gameService(PieceRepository pieceRepository, BoardRepository boardRepository) {
		return new GameService(pieceRepository, boardRepository) {
			@Override
			public boolean isEnd(int roomId) {
				return true;
			}
		};
	}
}
