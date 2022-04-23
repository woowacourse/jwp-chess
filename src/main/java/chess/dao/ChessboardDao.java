package chess.dao;

import chess.dto.ChessGameDto;
import chess.dto.GameInfoDto;
import chess.dto.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessboardDao {

    private JdbcTemplate jdbcTemplate;

    public ChessboardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PieceDto> pieceRowMapper = (resultSet, rowNum) -> {
        PieceDto piece = new PieceDto(
                resultSet.getString("color"),
                resultSet.getString("type"),
                resultSet.getInt("x"),
                resultSet.getInt("y")
        );
        return piece;
    };

    private final RowMapper<GameInfoDto> gameInfoRowMapper = (resultSet, rowNum) -> {
        GameInfoDto gameInfo = new GameInfoDto(
                resultSet.getString("state"),
                resultSet.getString("turn")
        );
        return gameInfo;
    };

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
        addGameInfos(chessGameDto.getState(), chessGameDto.getTurn());
    }

    private void addBoard(PieceDto pieceDto) {
        final String sql = "INSERT INTO pieces (color,type,x,y) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, pieceDto.getColor(), pieceDto.getType(), pieceDto.getX(), pieceDto.getY());
    }

    private void addGameInfos(String state, String turn) {
        String sql = "INSERT INTO gameInfos (state,turn) VALUES ('" + state + "','" + turn + "')";
        jdbcTemplate.update(sql);
    }

}
