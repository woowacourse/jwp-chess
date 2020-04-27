package spring.chess.command;

import spring.chess.board.ChessBoard;
import spring.chess.game.ChessGame;
import spring.chess.location.Location;
import spring.chess.location.LocationSubStringUtil;
import spring.chess.progress.Progress;
import spring.chess.team.Team;

import java.util.Objects;

public class MoveCommand implements Command {
    private static final String SPACE = " ";
    private static final int NOW_INDEX_IN_MOVE_COMMAND = 1;
    private static final int DESTINATION_INDEX_IN_MOVE_COMMAND = 2;
    private static final String INVALID_ARGUMENTS_ERROR_MESSAGE = "null 혹은 빈 문자열의 값이 존재합니다.";

    private final Location now;
    private final Location destination;
    private final ChessGame chessGame;

    private MoveCommand(Location now
            , Location destination
            , ChessGame chessGame) {
        this.now = now;
        this.destination = destination;
        this.chessGame = chessGame;
    }

    @Override
    public Progress conduct() {
        ChessBoard chessBoard = chessGame.getChessBoard();
        Team turn = chessGame.getTurn();

        if (chessBoard.isNotExist(now)
                || chessBoard.canNotMove(now, destination)
                || chessBoard.isNotCorrectTeam(now, turn)) {
            return Progress.ERROR;
        }
        chessGame.deletePieceIfExistIn(destination, turn);

        chessBoard.move(now, destination);
        chessGame.movePieceInPlayerChessSet(now, destination);

        return chessGame.finishIfKingDie();
    }

    public static MoveCommand of(String rawCommand, ChessGame chessGame) {
        valid(rawCommand, chessGame);

        String now = rawCommand.split(SPACE)[NOW_INDEX_IN_MOVE_COMMAND];
        String destination = rawCommand.split(SPACE)[DESTINATION_INDEX_IN_MOVE_COMMAND];

        Location nowLocation = LocationSubStringUtil.substring(now);
        Location destinationLocation = LocationSubStringUtil.substring(destination);

        return new MoveCommand(nowLocation, destinationLocation, chessGame);
    }

    private static void valid(String rawCommand, ChessGame chessGame) {
        if (Objects.isNull(rawCommand) || rawCommand.isEmpty() || Objects.isNull(chessGame)) {
            throw new IllegalArgumentException(INVALID_ARGUMENTS_ERROR_MESSAGE);
        }
    }

    public static MoveCommand of(Location now, Location destination, ChessGame chessGame) {
        return new MoveCommand(now, destination, chessGame);
    }

    public Location getNow() {
        return now;
    }

    public Location getDestination() {
        return destination;
    }
}
