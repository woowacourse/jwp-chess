package chess.service.dto.game;

import chess.domain.manager.Game;

public class GameDto {

    private Long id;
    private String turnOwner;
    private int turnNumber;
    private boolean isPlaying;
    private double whiteScore;
    private double blackScore;

    public GameDto(String turnOwner, int turnNumber, boolean isPlaying, double whiteScore, double blackScore) {
        this(null, turnOwner, turnNumber, isPlaying, whiteScore, blackScore);
    }

    public GameDto(Long id, String turnOwner, int turnNumber, boolean isPlaying, double whiteScore, double blackScore) {
        this.id = id;
        this.turnOwner = turnOwner;
        this.turnNumber = turnNumber;
        this.isPlaying = isPlaying;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static GameDto from(Game game) {
        return new GameDto(
                game.getId(),
                game.turnOwnerName(),
                game.turnNumberValue(),
                game.isPlaying(),
                game.gameStatus().whiteScore(),
                game.gameStatus().blackScore());
    }

    public Long getId() {
        return id;
    }

    public String getTurnOwner() {
        return turnOwner;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }
}
