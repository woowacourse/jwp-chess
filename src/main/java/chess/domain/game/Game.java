package chess.domain.game;

import java.util.Map;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.game.state.FinishedState;
import chess.domain.game.state.GameState;
import chess.domain.game.state.RunningState;
import chess.domain.player.Players;

public class Game {

    private static final Long TEMPORARY_PLAYER_ID = 0L;

    private final Long id;
    private GameState gameState;

    private Game(final Long id, final GameState gameState) {
        this.id = id;
        this.gameState = gameState;
    }

    public static Game initializeGame() {
        return new Game(TEMPORARY_PLAYER_ID, RunningState.createFirstTurnRunning());
    }

    public static Game loadGame(final Long id, final Players players,
                                final boolean finished, final Color currentTurnColor) {
        if (finished) {
            return new Game(id, new FinishedState(players, currentTurnColor));
        }
        if (players.isPlayerAbleToPromotePawn(currentTurnColor.reverse())) {
            return new Game(id, new RunningState(players, currentTurnColor.reverse()));
        }
        return new Game(id, new RunningState(players, currentTurnColor));
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

    public Long getId() {
        return id;
    }

    public Players getPlayers() {
        return gameState.getPlayers();
    }

    public Color getColorOfCurrentTurn() {
        return gameState.getColor();
    }
}
