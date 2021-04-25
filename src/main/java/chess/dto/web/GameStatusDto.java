package chess.dto.web;

import chess.domain.board.Board;
import chess.domain.board.Team;
import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.Turn;
import chess.domain.gamestate.Finished;
import chess.domain.gamestate.GameState;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Running;

public class GameStatusDto {

    private static final String BLACK = "b";
    private static final String WHITE = "w";
    private static final String NONE = "n";
    private static final String READY = "Ready";
    private static final String RUNNING = "Running";
    private static final String FINISH = "Finished";

    private String gameState;
    private String turn;
    private String winner;

    public GameStatusDto() {

    }

    public GameStatusDto(ChessGame chessGame) {
        gameState = encodedGameState(chessGame.gameState());
        turn = encodedTeam(chessGame.currentTurn());
        winner = winnerByGameStatus(chessGame);
    }

    private static String winnerByGameStatus(ChessGame chessGame) {
        if (!chessGame.isOngoing()) {
            return encodedTeam(chessGame.winner());
        }
        return NONE;
    }

    private static String encodedTeam(Team team) {
        if (team.isBlack()) {
            return BLACK;
        }
        if (team.isWhite()) {
            return WHITE;
        }
        return NONE;
    }

    private String encodedGameState(GameState gameState) {
        if (gameState instanceof Ready) {
            return READY;
        }
        if (gameState instanceof Running) {
            return RUNNING;
        }
        return FINISH;
    }

    private Team toWinnerEntity() {
        if (winner.equals(WHITE)) {
            return Team.WHITE;
        }
        if (winner.equals(BLACK)) {
            return Team.BLACK;
        }
        return Team.NONE;
    }

    public Turn toTurnEntity() {
        if (turn.equals(WHITE)) {
            return new Turn(Team.WHITE);
        }
        return new Turn(Team.BLACK);
    }

    public GameState toGameStateEntity(Board board) {
        if (gameState.equals("Ready")) {
            return new Ready(board);
        }
        if (gameState.equals("Running")) {
            return new Running(board);
        }
        return new Finished(board, toWinnerEntity());
    }

    public String getGameState() {
        return gameState;
    }

    public String getTurn() {
        return turn;
    }

    public String getWinner() {
        return winner;
    }
}
