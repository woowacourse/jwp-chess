package chess.webdao;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.CapturedPieces;
import chess.domain.team.PiecePositions;
import chess.domain.team.Score;
import chess.domain.team.Team;
import chess.webdto.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static chess.webdto.TeamDto.BLACK_TEAM;
import static chess.webdto.TeamDto.WHITE_TEAM;

//todo: CRUD 위주로 진행해야함
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

    public ChessGame createChessGame() {
        String sql = "INSERT INTO chess_game (current_turn_team, is_playing) VALUES (?, ?)";
        //todo : Service로 이동
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        createTeamInfo(WHITE_TEAM.team(), chessGame.currentWhitePiecePosition());
        createTeamInfo(BLACK_TEAM.team(), chessGame.currentBlackPiecePosition());
        this.jdbcTemplate.update(sql, WHITE_TEAM.team(), chessGame.isPlaying());
        return chessGame;
    }

    private void createTeamInfo(final String team, final Map<Position, Piece> teamPiecePosition) {
        final String query = "INSERT INTO team_info (team, piece_info) VALUES (?, ?)";
        this.jdbcTemplate.update(query, team, PiecePositionDaoConverter.asDAO(teamPiecePosition));
    }

    public ChessGame readChessGame() {
        final Team blackTeam = readTeamInfo(BLACK_TEAM.team());
        final Team whiteTeam = readTeamInfo(WHITE_TEAM.team());
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
        final PiecePositions piecePositionsByTeam = new PiecePositions(piecePosition);
        return new Team(piecePositionsByTeam, new CapturedPieces(), new Score());
    }

    private ChessGame generateChessGame(final Team blackTeam, final Team whiteTeam) {
        final String chessGameQuery = "SELECT * FROM chess_game";
        final TurnDto turnDto = this.jdbcTemplate.queryForObject(chessGameQuery, actorRowMapper);
        return generateChessGameAccordingToDB(blackTeam, whiteTeam, turnDto.getCurrentTurnTeam(), turnDto.getIsPlaying());
    }

    private ChessGame generateChessGameAccordingToDB(final Team blackTeam, final Team whiteTeam,
                                                     final String currentTurnTeam, final boolean isPlaying) {
        if (WHITE_TEAM.team().equals(currentTurnTeam)) {
            return new ChessGame(blackTeam, whiteTeam, whiteTeam, isPlaying);
        }
        return new ChessGame(blackTeam, whiteTeam, blackTeam, isPlaying);
    }

    public void updateChessGame(final ChessGame chessGame, final String currentTurnTeam) {
        updateTeamInfo(chessGame.currentWhitePiecePosition(), WHITE_TEAM.team());
        updateTeamInfo(chessGame.currentBlackPiecePosition(), BLACK_TEAM.team());
        final String query = "UPDATE chess_game SET current_turn_team = (?), is_playing = (?)";
        this.jdbcTemplate.update(query, currentTurnTeam, chessGame.isPlaying());
    }

    private void updateTeamInfo(final Map<Position, Piece> teamPiecePosition, final String team) {
        final String query = "UPDATE team_info SET piece_info = (?) WHERE team = (?)";
        this.jdbcTemplate.update(query, PiecePositionDaoConverter.asDAO(teamPiecePosition), team);
    }

    public void deleteChessGame() {
        final String deletePiecePositionQuery = "DELETE FROM team_info";
        final String deleteChessGameQuery = "DELETE FROM chess_game";

        this.jdbcTemplate.update(deletePiecePositionQuery);
        this.jdbcTemplate.update(deleteChessGameQuery);
    }

}
