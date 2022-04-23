package chess.dao;

import chess.dto.ChessGameDto;
import chess.dto.GameInfoDto;
import chess.dto.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessboardDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChessboardDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<PieceDto> pieceRowMapper = (resultSet, rowNum) -> new PieceDto(
           resultSet.getString("color"),
           resultSet.getString("type"),
           resultSet.getInt("x"),
           resultSet.getInt("y")
   );

    private final RowMapper<GameInfoDto> gameInfoRowMapper = (resultSet, rowNum) -> new GameInfoDto(
            resultSet.getString("state"),
            resultSet.getString("turn")
    );

    public boolean isDataExist() {
        final String sql = "SELECT count(*) AS result FROM gameInfos";
        return jdbcTemplate.queryForObject(sql, Integer.class) > 0;
    }

    public void save(ChessGameDto chessGameDto) {
        truncateAll();
        addAll(chessGameDto);
    }

    public void truncateAll() {
        final String truncatePieces = "TRUNCATE TABLE pieces";
        final String truncateGameInfos = "TRUNCATE TABLE gameInfos";

        jdbcTemplate.update(truncatePieces);
        jdbcTemplate.update(truncateGameInfos);
    }

    public ChessGameDto load() {
        final String gameInfo_sql = "SELECT * FROM gameInfos";
        final String pieces_sql = "SELECT * FROM pieces ORDER BY x ASC, y ASC";

        List<PieceDto> pieces = jdbcTemplate.query(pieces_sql, pieceRowMapper);
        GameInfoDto gameInfo = jdbcTemplate.queryForObject(gameInfo_sql, gameInfoRowMapper);

        return new ChessGameDto(pieces, gameInfo);
    }

    private void addAll(ChessGameDto chessGameDto) {
        chessGameDto.getPieces()
                .forEach(this::addBoard);
        addGameInfos(chessGameDto.getGameInfo());
    }

    private void addBoard(PieceDto pieceDto) {
        final String sql = "INSERT INTO pieces (color,type,x,y) VALUES (:color,:type,:x,:y)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(pieceDto);

        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    private void addGameInfos(GameInfoDto gameInfo) {
        String sql = "INSERT INTO gameInfos (state,turn) VALUES (:state,:turn)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(gameInfo);

        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

}
