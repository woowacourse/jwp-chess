package chess.repository;

import chess.domain.team.BlackTeam;
import chess.domain.team.Team;
import chess.domain.team.WhiteTeam;
import dto.MoveDto;

public interface TeamRepository {
    Long create(Team team, Long gameId);
    WhiteTeam whiteTeam(Long gameId);
    BlackTeam blackTeam(Long gameId);
    void update(Long gameId, Team team);
    void move(Long gameId, MoveDto moveDto);
}
