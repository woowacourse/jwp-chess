package chess.domain.player;

import chess.domain.board.ChessBoardFactory;
import chess.domain.command.Command;
import chess.domain.command.CommandFactory;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import chess.domain.position.Source;
import chess.domain.position.Target;
import chess.domain.state.State;

import java.util.Map;
import java.util.Queue;

public class Round {
    private final Player whitePlayer;
    private final Player blackPlayer;

    private Map<Position, Piece> board;
    private Command command;
    private String currentTurn;

    public Round(final Map<Position, Piece> board, final String currentTurn, final String command) {
        this.whitePlayer = new WhitePlayer(board, currentTurn);
        this.blackPlayer = new BlackPlayer(board, currentTurn);
        this.board = board;
        this.command = CommandFactory.initialCommand(command);
        this.currentTurn = currentTurn;
    }

    public Round(final State white, final State black, final Command command) {
        this(new WhitePlayer(white), new BlackPlayer(black), command);
        this.currentTurn = "white";
    }

    public Round(final Player whitePlayer, final Player blackPlayer, final Command command) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.command = command;
    }

    public static Round of(final Map<Position, Piece> board, final String currentTurn) {
        int totalKing = 0;
        for (Map.Entry<Position, Piece> boardEntry : board.entrySet()) {
            totalKing = countKing(totalKing, boardEntry);
        }
        if (isAllKingAlive(totalKing)) {
            return new Round(board, currentTurn, "start");
        }
        return new Round(board, currentTurn, "end");
    }

    private static int countKing(int totalKing, final Map.Entry<Position, Piece> boardEntry) {
        if (isKing(boardEntry)) {
            totalKing++;
        }
        return totalKing;
    }

    private static boolean isKing(Map.Entry<Position, Piece> boardEntry) {
        return "k".equals(boardEntry.getValue().getPiece()) || "K".equals(boardEntry.getValue().getPiece());
    }

    private static boolean isAllKingAlive(int totalKing) {
        return totalKing == 2;
    }

    public void execute(final Queue<String> commands) {
        this.command = this.command.execute(commands.poll());
        if (this.command.isMove()) {
            this.command = command.ready();
            Position sourcePosition = Position.from(commands.poll());
            Position targetPosition = Position.from(commands.poll());
            moveByTurn(sourcePosition, targetPosition);
        }
    }

    private void moveByTurn(final Position sourcePosition, final Position targetPosition) {
        if (whitePlayer.isFinish()) {
            move(blackPlayer, whitePlayer, sourcePosition, targetPosition);
            return;
        }
        move(whitePlayer, blackPlayer, sourcePosition, targetPosition);
    }

    private void move(final Player currentPlayer, final Player anotherPlayer,
                      final Position sourcePosition, final Position targetPosition) {
        Source source = Source.valueOf(sourcePosition, currentPlayer.getState());
        Target target = Target.valueOf(source, targetPosition, currentPlayer.getState());
        currentPlayer.move(source, target, anotherPlayer.getState());
        anotherPlayer.toRunningState(currentPlayer.getState());
        checkPieces(anotherPlayer.getState(), target);
    }

    private void checkPieces(final State state, final Target target) {
        if (state.isKing(target.getPosition())) {
            this.command = command.end();
        }
        if (state.findPiece(target.getPosition()).isPresent()) {
            state.removePiece(target.getPosition());
        }
    }

    public Map<Position, Piece> getBoard() {
        board = ChessBoardFactory.initializeBoard();
        Pieces whitePieces = whitePlayer.getPieces();
        Pieces blackPieces = blackPlayer.getPieces();
        whitePieces.getPieces().forEach(piece -> board.put(piece.getPosition(), piece));
        blackPieces.getPieces().forEach(piece -> board.put(piece.getPosition(), piece));
        return board;
    }

    public Map<Position, Piece> getBoard(final Map<Position, Piece> loadBoard) {
        board = loadBoard;
        return board;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }
}
