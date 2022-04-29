package chess.domain.game;

import java.util.Map;
import java.util.Objects;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.game.state.GameState;
import chess.domain.game.state.GameStateFactory;
import chess.domain.game.state.RunningState;
import chess.domain.player.Players;

public class Game {

    private static final Long TEMPORARY_PLAYER_ID = 0L;

    private final Long id;
    private final String title;
    private final String password;
    private GameState gameState;

    private Game(final Long id, final String title, final String password, final GameState gameState) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.gameState = gameState;
    }

    public static Game initializeGame(final String title, final String password) {
        return new Game(TEMPORARY_PLAYER_ID, title, password, RunningState.createFirstTurnRunning());
    }

    public static Game loadGame(final Long id, final String title, final String password, final GameState gameState) {
        return new Game(id, title, password, gameState);
    }

    public void movePiece(final Position source, final Position target) {
        final RunningState runningState = convertToRunningState(gameState);
        gameState = runningState.move(source, target);
    }

    public void promotePiece(final String pieceName) {
        final RunningState runningState = convertToRunningState(gameState);
        gameState = runningState.promotion(pieceName);
    }

    private RunningState convertToRunningState(final GameState gameState) {
        validateGameIsRunning();
        return (RunningState) gameState;
    }

    private void validateGameIsRunning() {
        if (!gameState.isRunning()) {
            throw new IllegalStateException("게임이 시작되지 않았습니다.");
        }
    }

    public Map<Color, Double> getPlayerScores() {
        return gameState.getPlayerScores(new ScoreCalculator());
    }

    public void end() {
        gameState = gameState.end();
    }

    public boolean isRunning() {
        return gameState.isRunning();
    }

    public boolean isFinished() {
        return !isRunning();
    }

    public boolean isPromotable() {
        return gameState.isPromotable();
    }

    public boolean equalsPassword(final String password) {
        return password.equals(this.password);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public Players getPlayers() {
        return gameState.getPlayers();
    }

    public Color getColorOfCurrentTurn() {
        return gameState.getColor();
    }
}
