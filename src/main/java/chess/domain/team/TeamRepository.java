package chess.domain.team;

import dto.MoveDto;

public interface TeamRepository {
    Long create(Team team, Long gameId);
    WhiteTeam whiteTeam(Long gameId);
    BlackTeam blackTeam(Long gameId);
    void update(Long gameId, Team team);
    void move(Long gameId, MoveDto moveDto);
}
