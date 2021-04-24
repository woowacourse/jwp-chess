package chess.webdao;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.team.PieceCaptured;
import chess.domain.team.PiecePosition;
import chess.domain.team.Score;
import chess.domain.team.Team;
import chess.webdto.ChessGameTableDto;
import chess.webdto.GameRoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import static chess.service.TeamFormat.BLACK_TEAM;
import static chess.service.TeamFormat.WHITE_TEAM;

@Repository
public class SpringChessGameDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ChessGameTableDto> ChessGameInfoRowMapper = (resultSet, rowNum) -> {
        ChessGameTableDto chessGameTableDTO = new ChessGameTableDto(
                resultSet.getString("current_turn_team"),
                resultSet.getBoolean("is_playing")
        );
        return chessGameTableDTO;
    };

    public SpringChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createGameRoom(final String roomName) {
        String sql = "INSERT into game_room_info (room_name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(con ->  {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"game_id"});
            ps.setString(1, roomName);
            return ps;
        }, keyHolder);

        System.out.println("keyHolder.getKey().intValue() = " + keyHolder.getKey().intValue());
        return keyHolder.getKey().intValue();
    }

    public List<GameRoomDto> loadGameRooms() {
        String sql = "SELECT * FROM game_room_info";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    final int game_id = resultSet.getInt("game_id");
                    final String room_name = resultSet.getString("room_name");
                    return new GameRoomDto(game_id, room_name);
                }
        );
    }

    public ChessGame createChessGame(final int gameId) {
        String sql = "INSERT INTO chess_game (game_id, current_turn_team, is_playing) VALUES (?, ?, ?)";
        final ChessGame chessGame = new ChessGame(Team.blackTeam(), Team.whiteTeam());
        createTeamInfo(gameId, WHITE_TEAM.asDaoFormat(), chessGame.currentWhitePiecePosition());
        createTeamInfo(gameId, BLACK_TEAM.asDaoFormat(), chessGame.currentBlackPiecePosition());
        this.jdbcTemplate.update(sql, gameId, WHITE_TEAM.asDaoFormat(), chessGame.isPlaying());
        return chessGame;
    }

    private void createTeamInfo(final int gameId, final String team, final Map<Position, Piece> teamPiecePosition) {
        final String sql = "INSERT INTO team_info (game_id, team, piece_info) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(sql, gameId, team, PiecePositionDaoConverter.asDao(teamPiecePosition));
    }

    public ChessGame readChessGame(final int gameId) {
        final Team blackTeam = readTeamInfo(gameId, BLACK_TEAM.asDaoFormat());
        final Team whiteTeam = readTeamInfo(gameId, WHITE_TEAM.asDaoFormat());
        return generateChessGame(gameId, blackTeam, whiteTeam);
    }

    private Team readTeamInfo(final int gameId, final String team) {
        final String sql = "SELECT piece_info FROM team_info where team = (?) && game_id = (?)";
        final String teamPieceInfo = this.jdbcTemplate.queryForObject(sql, String.class, team, gameId);
        return generateTeam(teamPieceInfo, team);
    }

    private Team generateTeam(final String teamPieceInfo, final String team) {
        Map<Position, Piece> piecePosition;
        piecePosition = PiecePositionDaoConverter.asPiecePosition(teamPieceInfo, team);
        final PiecePosition PiecePositionByTeam = new PiecePosition(piecePosition);
        return new Team(PiecePositionByTeam, new PieceCaptured(), new Score());
    }

    private ChessGame generateChessGame(final int gameId, final Team blackTeam, final Team whiteTeam) {
        final String sql = "SELECT * FROM chess_game where game_id = (?)";
        final ChessGameTableDto chessGameTableDTO = this.jdbcTemplate.queryForObject(sql, ChessGameInfoRowMapper, gameId);
        return generateChessGameAccordingToDB(blackTeam, whiteTeam,
                chessGameTableDTO.getCurrentTurnTeam(), chessGameTableDTO.getIsPlaying());
    }

    private ChessGame generateChessGameAccordingToDB(final Team blackTeam, final Team whiteTeam,
                                                     final String currentTurnTeam, final boolean isPlaying) {
        if (WHITE_TEAM.asDaoFormat().equals(currentTurnTeam)) {
            return new ChessGame(blackTeam, whiteTeam, whiteTeam, isPlaying);
        }
        return new ChessGame(blackTeam, whiteTeam, blackTeam, isPlaying);
    }

    public void updateChessGame(final int gameId, final ChessGame chessGame, final String currentTurnTeam) {
        updateTeamInfo(gameId, chessGame.currentWhitePiecePosition(), WHITE_TEAM.asDaoFormat());
        updateTeamInfo(gameId, chessGame.currentBlackPiecePosition(), BLACK_TEAM.asDaoFormat());
        final String sql = "UPDATE chess_game SET current_turn_team = (?), is_playing = (?) where game_id = (?)";
        this.jdbcTemplate.update(sql, currentTurnTeam, chessGame.isPlaying(), gameId);
    }

    private void updateTeamInfo(final int gameId, final Map<Position, Piece> teamPiecePosition, final String team) {
        final String sql = "UPDATE team_info SET piece_info = (?) WHERE team = (?) && game_id = (?)";
        this.jdbcTemplate.update(sql, PiecePositionDaoConverter.asDao(teamPiecePosition), team, gameId);
    }

    public void deleteChessGame(final int gameId) {
        final String team_info_sql = "DELETE FROM team_info where game_id = (?)";
        final String chess_game_sql = "DELETE FROM chess_game where game_id = (?)";
        final String game_room_info_sql = "DELETE FROM game_room_info where game_id = (?)";
        this.jdbcTemplate.update(team_info_sql, gameId);
        this.jdbcTemplate.update(chess_game_sql, gameId);
        this.jdbcTemplate.update(game_room_info_sql, gameId);
    }
}
