package chess.domain.manager;

import chess.domain.Entity;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.position.Path;
import chess.domain.board.position.Position;

import java.util.HashMap;
import java.util.Map;

public class Game extends Entity<Long> {

    private Board board;
    private State state;
    private GameStatus gameStatus;

    public Game() {
        this(BoardInitializer.initiateBoard(), State.newGameState());
    }

    public Game(final Board board, final State state) {
        this(null, board, state);
    }

    public Game(final Long id, final Board board, final State state) {
        this(id, board, state, GameStatus.statusOfBoard(board));
    }

    public Game(final Long id, final Board board, final State state, final GameStatus gameStatus) {
        super(id);
        this.board = board;
        this.state = state;
        this.gameStatus = gameStatus;
    }

    public void move(final Position source, final Position target) {
        validateSourcePiece(source);
        validateTurnOwner(source);
        board.move(source, target);
        this.state = state.changeTurnOwner();
        this.gameStatus = GameStatus.statusOfBoard(board);
        checkCaughtKing();
    }

    private void validateSourcePiece(final Position source) {
        if (board.pickPiece(source).isEmptyPiece()) {
            throw new IllegalArgumentException("지정한 칸에는 체스말이 존재하지 않습니다.");
        }
    }

    private void validateTurnOwner(final Position source) {
        if (!board.isPositionSameOwner(source, this.state.turnOwner())) {
            throw new IllegalArgumentException("현재는 " + this.state.turnOwnerName() + "플레이어의 턴입니다.");
        }
    }

    private void checkCaughtKing() {
        if (isCaughtKing()) {
            this.state = this.state.endGame();
        }
    }

    private boolean isCaughtKing() {
        return board.isCaughtKing(this.state.turnOwner());
    }

    public void gameEnd() {
        this.state = this.state.endGame();
    }

    public boolean isPlaying() {
        return state.isPlaying();
    }

    public Path movablePath(final Position source) {
        validateSourcePiece(source);
        return board.movablePath(source);
    }

    public Piece pickPiece(final Position position) {
        return this.board.pickPiece(position);
    }

    public State state() {
        return this.state;
    }

    public Owner turnOwner() {
        return this.state.turnOwner();
    }

    public String turnOwnerName() {
        return this.state.turnOwnerName();
    }

    public int turnNumberValue() {
        return this.state.turnNumberValue();
    }

    public GameStatus gameStatus() {
        return this.gameStatus;
    }

    public Board getBoard() {
        return board;
    }

    public Map<Position, Piece> boardToMap() {
        return new HashMap<>(this.board.getBoard());
    }
}
