package chess.webdto.converter;

import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdto.dao.PieceDto;

import java.util.Map;

public class TeamInfoToDto {
    private String team;
    private String position;
    private String piece;
    private boolean isFirstMoved;
    private long roomId;

    public TeamInfoToDto(){
    }

    public TeamInfoToDto(String team, Map.Entry<Position, Piece> infos, long roomId) {
        this.team = team;
        this.position = infos.getKey().getPositionInitial();
        this.piece = convert(infos.getValue());
        this.isFirstMoved =infos.getValue().isFirstMove();
        this.roomId = roomId;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setIsFirstMoved(boolean firstMoved) {
        isFirstMoved = firstMoved;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public String getPiece() {
        return piece;
    }

    public boolean getIsFirstMoved() {
        return isFirstMoved;
    }

    public long getRoomId() {
        return roomId;
    }

    private String convert(Piece value) {
        return PieceDto.convert(value);
    }

}
