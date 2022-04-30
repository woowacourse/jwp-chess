package chess.entity;

import chess.domain.piece.Color;
import chess.dto.LogInDto;

public class RoomEntity {
    private static final String INCORRECT_PASSWORD_ERROR_MESSAGE = "올바르지 않은 비밀번호 입니다.";

    private String gameId;
    private String gamePassword;
    private boolean force_end_flag;

    private Color turn;

    public RoomEntity(String gameId, String gamePassword, Color turn, boolean force_end_flag) {
        this.gameId = gameId;
        this.gamePassword = gamePassword;
        this.turn = turn;
        this.force_end_flag = force_end_flag;
    }

    public RoomEntity(String gameId, Color turn, boolean force_end_flag) {
        this.gameId = gameId;
        this.gamePassword = "";
        this.turn = turn;
        this.force_end_flag = force_end_flag;
    }

    public void validateLogIn(LogInDto logInDto) {
        if (!gamePassword.equals(logInDto.getGamePassword())) {
            throw new IllegalArgumentException(INCORRECT_PASSWORD_ERROR_MESSAGE);
        }
    }

    public String getGameId() {
        return gameId;
    }

    public String getGamePassword() {
        return gamePassword;
    }

    public Color getTurn() {
        return turn;
    }

    public boolean isForce_end_flag() {
        return force_end_flag;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setGamePassword(String gamePassword) {
        this.gamePassword = gamePassword;
    }

    public void setTurn(Color turn) {
        this.turn = turn;
    }

    public void setForce_end_flag(boolean force_end_flag) {
        this.force_end_flag = force_end_flag;
    }
}
