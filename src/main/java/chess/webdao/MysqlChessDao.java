package chess.webdao;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.webdto.PiecePositionDaoConverter;
import chess.webdto.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static chess.webdto.TeamDto.WHITE_TEAM;

@Repository
public class MysqlChessDao implements ChessDao {
    private final RowMapper<TurnDto> actorRowMapper = (resultSet, rowNum) -> {
        TurnDto turnDto = new TurnDto(
                resultSet.getString("current_turn_team"),
                resultSet.getBoolean("is_playing")
        );
        return turnDto;
    };
    private JdbcTemplate jdbcTemplate;

    public MysqlChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int createChessGame(boolean isPlaying) {
        String sql = "INSERT INTO chess_game (current_turn_team, is_playing) VALUES (?, ?)";
        return this.jdbcTemplate.update(sql, WHITE_TEAM.team(), isPlaying);
    }

    @Override
    public int createTeamInfo(final String team, final Map<Position, Piece> teamPiecePosition) {
        final String query = "INSERT INTO team_info (team, piece_info) VALUES (?, ?)";
        return this.jdbcTemplate.update(query, team, PiecePositionDaoConverter.asDAO(teamPiecePosition));
    }

    @Override
    public String readTeamInfo(final String team) {
        final String teamQuery = "SELECT piece_info FROM team_info where team = (?)";
        return this.jdbcTemplate.queryForObject(teamQuery, String.class, team);
    }

    @Override
    public TurnDto readTurn() {
        final String chessGameQuery = "SELECT * FROM chess_game";
        final TurnDto turnDto = this.jdbcTemplate.queryForObject(chessGameQuery, actorRowMapper);
        return turnDto;
    }

    @Override
    public void updateChessGame(final ChessGame chessGame, final String currentTurnTeam) {
        final String query = "UPDATE chess_game SET current_turn_team = (?), is_playing = (?)";
        this.jdbcTemplate.update(query, currentTurnTeam, chessGame.isPlaying());
    }

    @Override
    public void updateTeamInfo(final Map<Position, Piece> teamPiecePosition, final String team) {
        final String query = "UPDATE team_info SET piece_info = (?) WHERE team = (?)";
        this.jdbcTemplate.update(query, PiecePositionDaoConverter.asDAO(teamPiecePosition), team);
    }

    @Override
    public void deleteChessGame() {
        final String deletePiecePositionQuery = "DELETE FROM team_info";
        final String deleteChessGameQuery = "DELETE FROM chess_game";

        this.jdbcTemplate.update(deletePiecePositionQuery);
        this.jdbcTemplate.update(deleteChessGameQuery);
    }

}
