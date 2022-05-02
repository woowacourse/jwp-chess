package chess.serviece.dto;

import chess.dao.entity.GameEntity;

public class GameDto {

    private final Long id;
    private final String title;
    private final String password;
    private final String turn;
    private final String status;

    public GameDto(Long id, String title, String password, String turn, String status) {
        this.id = id;
        this.title = title;
        this.password = password;
        this.turn = turn;
        this.status = status;
    }

    public static GameDto from(GameEntity game) {
        return new GameDto(game.getId(), game.getTitle(), game.getPassword(), game.getTurn(), game.getStatus());
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

    public String getTurn() {
        return turn;
    }

    public String getStatus() {
        return status;
    }
}
