package chess.webdto.converter;

import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdto.dao.PieceDto;
import chess.webdto.dao.TeamInfoDto;

import java.util.Map;

public class TeamInfoToDto {
    private String team;
    private Map.Entry<Position, Piece> infos;
    private long roomId;

    public TeamInfoToDto() {
    }

    public TeamInfoToDto(String team, Map.Entry<Position, Piece> infos, long roomId) {
        this.team = team;
        this.infos = infos;
        this.roomId = roomId;
    }

    public TeamInfoDto convertToTeamInfoDto() {
        TeamInfoDto teamInfoDto = new TeamInfoDto();

        teamInfoDto.setTeam(team);
        teamInfoDto.setPosition(infos.getKey().getPositionInitial());
        teamInfoDto.setPiece(convert(infos.getValue()));
        teamInfoDto.setIsFirstMoved(infos.getValue().isFirstMove());
        teamInfoDto.setRoomId(roomId);

        return teamInfoDto;
    }

    private String convert(Piece value) {
        return PieceDto.convert(value);
    }

}
