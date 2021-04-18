package chess.webdao;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.PieceCaptured;
import chess.domain.team.PiecePosition;
import chess.domain.team.Score;
import chess.domain.team.Team;
import chess.webdto.ChessGameTableDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static chess.service.TeamFormat.BLACK_TEAM;
import static chess.service.TeamFormat.WHITE_TEAM;

@Repository
public class SpringChessGameDao {
    private JdbcTemplate jdbcTemplate;

    public SpringChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ChessGame createChessGame() {
        String sql = "INSERT INTO chess_game (current_turn_team, is_playing) VALUES (?, ?)";
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        createTeamInfo(WHITE_TEAM.asDaoFormat(), chessGame.currentWhitePiecePosition());
        createTeamInfo(BLACK_TEAM.asDaoFormat(), chessGame.currentBlackPiecePosition());
        this.jdbcTemplate.update(sql, WHITE_TEAM.asDaoFormat(), chessGame.isPlaying());
        return chessGame;
    }

    private void createTeamInfo(final String team, final Map<Position, Piece> teamPiecePosition) {
        final String query = "INSERT INTO team_info (team, piece_info) VALUES (?, ?)";
        this.jdbcTemplate.update(query, team, PiecePositionDaoConverter.asDao(teamPiecePosition));
    }

    public ChessGame readChessGame() {
        final Team blackTeam = readTeamInfo(BLACK_TEAM.asDaoFormat());
        final Team whiteTeam = readTeamInfo(WHITE_TEAM.asDaoFormat());
        return generateChessGame(blackTeam, whiteTeam);
    }

    private Team readTeamInfo(final String team) {
        final String teamQuery = "SELECT piece_info FROM team_info where team = (?)";
        final String teamPieceInfo = this.jdbcTemplate.queryForObject(teamQuery, String.class, team);
        return generateTeam(teamPieceInfo, team);
    }

    private Team generateTeam(final String teamPieceInfo, final String team) {
        Map<Position, Piece> piecePosition;
        piecePosition = PiecePositionDaoConverter.asPiecePosition(teamPieceInfo, team);
        final PiecePosition PiecePositionByTeam = new PiecePosition(piecePosition);
        return new Team(PiecePositionByTeam, new PieceCaptured(), new Score());
    }

    private ChessGame generateChessGame(final Team blackTeam, final Team whiteTeam) {
        final String chessGameQuery = "SELECT * FROM chess_game";
        final ChessGameTableDto chessGameTableDTO = this.jdbcTemplate.queryForObject(chessGameQuery, ChessGameInfoRowMapper);
        return generateChessGameAccordingToDB(blackTeam, whiteTeam,
                chessGameTableDTO.getCurrentTurnTeam(), chessGameTableDTO.getIsPlaying());
    }

    private final RowMapper<ChessGameTableDto> ChessGameInfoRowMapper = (resultSet, rowNum) -> {
        ChessGameTableDto chessGameTableDTO = new ChessGameTableDto(
                resultSet.getString("current_turn_team"),
                resultSet.getBoolean("is_playing")
        );
        return chessGameTableDTO;
    };

    private ChessGame generateChessGameAccordingToDB(final Team blackTeam, final Team whiteTeam,
                                                     final String currentTurnTeam, final boolean isPlaying) {
        if (WHITE_TEAM.asDaoFormat().equals(currentTurnTeam)) {
            return new ChessGame(blackTeam, whiteTeam, whiteTeam, isPlaying);
        }
        return new ChessGame(blackTeam, whiteTeam, blackTeam, isPlaying);
    }

    public void updateChessGame(final ChessGame chessGame, final String currentTurnTeam) {
        updateTeamInfo(chessGame.currentWhitePiecePosition(), WHITE_TEAM.asDaoFormat());
        updateTeamInfo(chessGame.currentBlackPiecePosition(), BLACK_TEAM.asDaoFormat());
        final String query = "UPDATE chess_game SET current_turn_team = (?), is_playing = (?)";
        this.jdbcTemplate.update(query, currentTurnTeam, chessGame.isPlaying());
    }

    private void updateTeamInfo(final Map<Position, Piece> teamPiecePosition, final String team) {
        final String query = "UPDATE team_info SET piece_info = (?) WHERE team = (?)";
        this.jdbcTemplate.update(query, PiecePositionDaoConverter.asDao(teamPiecePosition), team);
    }

    public void deleteChessGame() {
        final String deletePiecePositionQuery = "DELETE FROM team_info";
        final String deleteChessGameQuery = "DELETE FROM chess_game";
        this.jdbcTemplate.update(deletePiecePositionQuery);
        this.jdbcTemplate.update(deleteChessGameQuery);
    }
}
