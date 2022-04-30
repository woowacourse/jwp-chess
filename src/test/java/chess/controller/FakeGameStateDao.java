package chess.controller;

import chess.dao.GameStateDao;

public class FakeGameStateDao implements GameStateDao {

    private String gameState;
    private String turn;

    public FakeGameStateDao() {
        this.gameState = "nothing";
        this.turn = "nothing";
    }

    @Override
    public boolean hasPlayingGame(int roomNumber) {
        return !gameState.equals("nothing");
    }

    @Override
    public void saveState(int roomNumber, final String state) {
        this.gameState = state;
    }

    @Override
    public void saveTurn(int roomNumber, final String turn) {
        this.turn = turn;
    }

    @Override
    public String getGameState(int roomNumber) {
        return gameState;
    }

    @Override
    public String getTurn(int roomNumber) {
        return turn;
    }

    @Override
    public void removeGameState(int roomNumber) {
        gameState = "nothing";
        turn = "nothing";
    }
}
