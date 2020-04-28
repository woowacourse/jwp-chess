package wooteco.chess.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.Score;
import wooteco.chess.domain.board.ChessGame;
import wooteco.chess.domain.command.MoveCommand;
import wooteco.chess.domain.entity.Game;
import wooteco.chess.domain.entity.GamePiece;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.GameDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.repository.GameRepository;
import wooteco.chess.util.ScoreConverter;
import wooteco.chess.util.UnicodeConverter;

@Service
public class ChessService {
	private final GameRepository gameRepository;

	public ChessService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public BoardDto createGame() {
		ChessGame chessGame = new ChessGame();
		Long gameId = saveGame(chessGame)
			.getId();
		return createBoardDto(gameId, chessGame);
	}

	public MoveResponseDto move(MoveRequestDto moveRequestDto) {
		Long gameId = moveRequestDto.getGameId();
		String command = moveRequestDto.getCommand();

		Game game = gameRepository.findById(gameId)
			.orElseThrow(() -> new IllegalArgumentException(gameId + "은 존재하지 않는 게임입니다."));

		ChessGame chessGame = game.toChessGame();
		MoveCommand moveCommand = new MoveCommand(command);
		chessGame.move(moveCommand);

		String newPosition = moveCommand.getTargetPosition().getName();
		updateGame(gameId, chessGame);

		Piece piece = chessGame.findPieceByPosition(Position.of(newPosition));

		return new MoveResponseDto(gameId, chessGame.isKingAlive(), UnicodeConverter.convert(piece.getSymbol()),
			chessGame.getTurn().getName());
	}

	public BoardDto load(Long gameId) {
		Game game = gameRepository.findById(gameId)
			.orElseThrow(() -> new IllegalArgumentException(String.format("%d : 존재하지 않는 게임입니다.", gameId)));

		ChessGame chessGame = game.toChessGame();

		return createBoardDto(gameId, chessGame);
	}

	private GameDto saveGame(ChessGame chessGame) {
		Team turn = chessGame.getTurn();
		List<GamePiece> gamePieces = chessGame.toPieceEntity();
		Game game = new Game(turn.getName(), gamePieces);
		game = gameRepository.save(game);

		return new GameDto(game.getId(), game.getTurn());
	}

	private void updateGame(Long id, ChessGame chessGame) {
		Team turn = chessGame.getTurn();
		Game game = new Game(id, turn.getName(), chessGame.toPieceEntity());
		gameRepository.save(game);
	}

	private BoardDto createBoardDto(Long gameId, ChessGame chessGame) {
		List<String> symbols = UnicodeConverter.convert(chessGame.getReverse());
		Map<Team, Double> score = Score.calculateScore(chessGame.getPieces(), Team.values());
		return new BoardDto(gameId, symbols, ScoreConverter.convert(score), chessGame.getTurn().getName());
	}
}