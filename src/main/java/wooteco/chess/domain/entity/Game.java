package wooteco.chess.domain.entity;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.board.ChessGame;
import wooteco.chess.domain.command.MoveCommand;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.util.PieceConverter;

@Table("GAME")
public class Game {
	@Id
	private Long id;
	private String turn;
	@MappedCollection(idColumn = "game_id", keyColumn = "id")
	private List<GamePiece> gamePieces;

	protected Game() {
	}

	public Game(String turn, List<GamePiece> gamePieces) {
		this.turn = turn;
		this.gamePieces = gamePieces;
	}

	public Game(Long id, String turn, List<GamePiece> gamePiece) {
		this(turn, gamePiece);
		this.id = id;
	}

	public Game(ChessGame chessGame) {
		this(chessGame.getTurnName(), chessGame.toPieceEntity());
	}

	public void move(MoveCommand moveCommand) {
		ChessGame chessGame = getChessGame();
		chessGame.move(moveCommand);
		this.turn = chessGame.getTurnName();
		this.gamePieces = chessGame.toPieceEntity();
	}

	private List<Piece> toPieces() {
		return this.gamePieces.stream()
			.map(piece -> PieceConverter.of(piece.getSymbol(), piece.getPosition()))
			.collect(Collectors.toList());
	}

	public ChessGame getChessGame() {
		return new ChessGame(toPieces(), turn);
	}

	public Long getId() {
		return id;
	}

	public String getTurn() {
		return turn;
	}
}
