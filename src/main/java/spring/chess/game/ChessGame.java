package spring.chess.game;

import spring.chess.board.ChessBoard;
import spring.chess.board.ChessBoardCreater;
import spring.chess.command.Command;
import spring.chess.exception.InvalidConstructorValueException;
import spring.chess.location.Location;
import spring.chess.piece.type.Piece;
import spring.chess.player.ChessSet;
import spring.chess.player.Player;
import spring.chess.progress.Progress;
import spring.chess.result.ChessResult;
import spring.chess.result.ChessScores;
import spring.chess.team.Team;
import spring.entity.ChessGameEntity;
import spring.entity.PieceEntity;

import java.util.Objects;
import java.util.Set;

import static spring.chess.progress.Progress.END;
import static spring.chess.team.Team.BLACK;
import static spring.chess.team.Team.WHITE;

public class ChessGame {
    private final ChessBoard chessBoard;
    private final Player white;
    private final Player black;
    private Team turn;

    public ChessGame() {
        this(ChessBoardCreater.create(), Team.WHITE);
    }

    public ChessGame(ChessBoard chessBoard, Team turn) {
        validNullValue(chessBoard, turn);
        this.chessBoard = chessBoard;
        white = new Player(new ChessSet(chessBoard.giveMyPieces(WHITE)), WHITE);
        black = new Player(new ChessSet(chessBoard.giveMyPieces(BLACK)), BLACK);
        this.turn = turn;
    }

    private void validNullValue(ChessBoard chessBoard, Team turn) {
        if (Objects.isNull(chessBoard) || Objects.isNull(turn)) {
            throw new InvalidConstructorValueException();
        }
    }

    public void changeTurn() {
        turn = turn.changeTurn();
    }

    public Progress doOneCommand(Command command) {
        return command.conduct();
    }

    public void movePieceInPlayerChessSet(Location now, Location destination) {
        if (white.is(turn)) {
            white.movePiece(now, destination);
            return;
        }
        black.movePiece(now, destination);
    }

    public void deletePieceIfExistIn(Location location, Team turn) {
        Player counterplayer = getCounterTurnPlayer(turn);
        if (chessBoard.isExistPieceIn(location)) {
            counterplayer.deletePieceIfExistIn(location);
        }
    }

    public Progress finishIfKingDie() {
        if (isExistKingDiePlayer()) {
            return END;
        }
        return Progress.CONTINUE;
    }

    private boolean isExistKingDiePlayer() {
        return white.hasNotKing() || black.hasNotKing();
    }


    public ChessResult findWinner() {
        return ChessResult.of(white, black);
    }

    public ChessScores calculateScores() {
        return new ChessScores(
                white.calculate(),
                black.calculate()
        );
    }

    public Piece getPiece(Location location) {
        return chessBoard.getPiece(location);
    }

    private Player getCounterTurnPlayer(Team turn) {
        if(black.isNotSame(turn)) {
            return black;
        }
        return white;
    }

    public Team getTurn() {
        return turn;
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public ChessGameEntity toEntity() {
        Set<PieceEntity> pieces = this.chessBoard.toEntities();
        return new ChessGameEntity(this.turn == BLACK, pieces);
    }
}
