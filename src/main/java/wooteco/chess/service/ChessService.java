package wooteco.chess.service;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.domain.Score;
import wooteco.chess.domain.board.ChessGame;
import wooteco.chess.domain.command.MoveCommand;
import wooteco.chess.domain.entity.Game;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.position.Position;
import wooteco.chess.domain.piece.team.Team;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.GameDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.dto.SavedGameBundleDto;
import wooteco.chess.dto.SavedGameDto;
import wooteco.chess.repository.GameRepository;
import wooteco.chess.util.ScoreConverter;
import wooteco.chess.util.UnicodeConverter;

@Service
public class ChessService {
	private final GameRepository gameRepository;

	public ChessService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@Transactional
	public BoardDto createGame() {
		ChessGame chessGame = new ChessGame();
		Long gameId = saveGame(chessGame)
			.getId();
		return createBoardDto(gameId, chessGame);
	}

	@Transactional
	public MoveResponseDto move(MoveRequestDto moveRequestDto) {
		Game game = findGameById(moveRequestDto.getGameId());
		MoveCommand moveCommand = new MoveCommand(moveRequestDto.getCommand());

		game.move(moveCommand);
		gameRepository.save(game);

		ChessGame chessGame = game.getChessGame();

		Position targetPosition = moveCommand.getTargetPosition();
		Piece piece = chessGame.findPieceByPosition(targetPosition);

		return new MoveResponseDto(game.getId(), chessGame.isKingAlive(), UnicodeConverter.convert(piece.getSymbol()),
			chessGame.getTurnName(), getScore(chessGame));
	}

	private Map<String, Double> getScore(ChessGame chessGame) {
		Map<Team, Double> score = Score.calculateScore(chessGame.getPieces(), Team.values());
		return ScoreConverter.convert(score);
	}

	private Game findGameById(Long gameId) {
		return gameRepository.findById(gameId)
			.orElseThrow(() -> new IllegalArgumentException(gameId + "은 존재하지 않는 게임입니다."));
	}

	@Transactional(readOnly = true)
	public BoardDto load(Long gameId) {
		Game game = findGameById(gameId);
		ChessGame chessGame = game.getChessGame();
		return createBoardDto(gameId, chessGame);
	}

	private GameDto saveGame(ChessGame chessGame) {
		Game game = new Game(chessGame);
		game = gameRepository.save(game);
		return new GameDto(game);
	}

	private BoardDto createBoardDto(Long gameId, ChessGame chessGame) {
		List<String> symbols = UnicodeConverter.convert(chessGame.getReverse());
		Map<String, Double> score = getScore(chessGame);
		return new BoardDto(gameId, symbols, score, chessGame.getTurnName());
	}

	@Transactional(readOnly = true)
	public SavedGameBundleDto findAllGames() {
		List<Long> gameId = gameRepository.finAllGameId();
		return gameId.stream()
			.map(SavedGameDto::new)
			.collect(collectingAndThen(toList(), SavedGameBundleDto::new));
	}
}