package chess.dao;

import chess.dto.ChessGameDto;
import chess.dto.GameInfoDto;
import chess.dto.PieceDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChessboardDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChessboardDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
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
        final String sql = "SELECT count(*) FROM gameInfos";
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        return count != null && count > 0;
    }

    public void save(ChessGameDto chessGameDto) {
        truncateAll();
        addAll(chessGameDto);
    }

    public void truncateAll() {
        final String truncatePieces = "TRUNCATE TABLE pieces";
        final String truncateGameInfos = "TRUNCATE TABLE gameInfos";

        namedParameterJdbcTemplate.update(truncatePieces, Map.of());
        namedParameterJdbcTemplate.update(truncateGameInfos, Map.of());
    }

    public ChessGameDto load() {
        final String gameInfo_sql = "SELECT * FROM gameInfos";
        final String pieces_sql = "SELECT * FROM pieces ORDER BY x ASC, y ASC";

        List<PieceDto> pieces = namedParameterJdbcTemplate.query(pieces_sql, Map.of(), pieceRowMapper);
        GameInfoDto gameInfo = namedParameterJdbcTemplate.queryForObject(gameInfo_sql, Map.of(), gameInfoRowMapper);

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
