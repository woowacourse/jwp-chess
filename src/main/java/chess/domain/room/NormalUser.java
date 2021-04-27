package chess.domain.room;

import chess.domain.TeamColor;

public class NormalUser implements User{

    private boolean player;
    private String nickname;
    private TeamColor teamColor;
    private Long roomId;

    public NormalUser() {
        player = false;
        nickname = "사용자";
        teamColor = TeamColor.NONE;
    }

    @Override
    public void sendData(String data) {
        throw new IllegalStateException("사용할 수 없는 메서드입니다.");
    }

    @Override
    public boolean isPlayer() {
        return player;
    }

    @Override
    public void setAsPlayer(Long roomId, TeamColor teamColor, String nickname) {
        player = true;
        this.roomId = roomId;
        this.teamColor = teamColor;
        this.nickname = nickname;
    }

    @Override
    public void setAsNotPlayer(String nickname) {
        player = false;
        this.nickname = nickname;
        this.teamColor = TeamColor.NONE;
    }

    @Override
    public TeamColor teamColor() {
        return teamColor;
    }

    @Override
    public String name() {
        return nickname;
    }

    @Override
    public boolean isWhite() {
        return teamColor == TeamColor.WHITE;
    }

    @Override
    public boolean isBlack() {
        return teamColor == TeamColor.BLACK;
    }

    @Override
    public Long roomId() {
        return roomId;
    }
}
