package chess.repository;

import chess.dao.PieceDao;
import chess.dao.TeamDao;
import chess.domain.Position;
import chess.domain.TeamRepository;
import chess.domain.piece.Piece;
import chess.domain.team.BlackTeam;
import chess.domain.team.Team;
import chess.domain.team.WhiteTeam;
import dto.MoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TeamRepositoryImpl implements TeamRepository {
    private final TeamDao teamDao;
    private final PieceDao pieceDao;

    @Autowired
    public TeamRepositoryImpl(final TeamDao teamDao, final PieceDao pieceDao) {
        this.teamDao = teamDao;
        this.pieceDao = pieceDao;
    }

    @Override
    public Long create(final Team team, Long gameId) {
        Long teamId = teamDao.create(team, gameId);
        pieceDao.create(team.getPiecePosition(), team.getName(), gameId);
        return teamId;
    }

    @Override
    public WhiteTeam whiteTeam(final Long gameId) {
        Team team = teamDao.load(gameId, WhiteTeam.DEFAULT_NAME);
        final Map<Position, Piece> pieces = pieceDao.load(gameId, WhiteTeam.DEFAULT_NAME);
        return new WhiteTeam(team.isCurrentTurn(), pieces);
    }

    @Override
    public BlackTeam blackTeam(final Long gameId) {
        Team team = teamDao.load(gameId, BlackTeam.DEFAULT_NAME);
        final Map<Position, Piece> pieces = pieceDao.load(gameId, BlackTeam.DEFAULT_NAME);
        return new BlackTeam(team.isCurrentTurn(), pieces);
    }

    @Override
    public void update(final Long gameId, final Team team) {
        teamDao.update(gameId, team);
    }

    @Override
    public void move(final Long gameId, final MoveDto moveDto) {
        pieceDao.delete(gameId, moveDto);
        pieceDao.update(gameId, moveDto);
    }
}
