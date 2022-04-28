package chess.dao;

import chess.chessgame.ChessGame;
import chess.chessgame.Position;
import chess.dto.GameInfoDto;
import chess.dto.PieceDto;
import chess.piece.Piece;
import chess.utils.PieceGenerator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
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

    public void save(ChessGame chessGame) {
        truncateAll();
        addAll(chessGame);
    }

    public void truncateAll() {
        final String truncatePieces = "TRUNCATE TABLE pieces";
        final String truncateGameInfos = "TRUNCATE TABLE gameInfos";

        namedParameterJdbcTemplate.update(truncatePieces, Map.of());
        namedParameterJdbcTemplate.update(truncateGameInfos, Map.of());
    }

    public ChessGame load() {
        final String pieces_sql = "SELECT * FROM pieces ORDER BY x ASC, y ASC";
        List<PieceDto> pieces = namedParameterJdbcTemplate.query(pieces_sql, Map.of(), pieceRowMapper);

        final String gameInfo_sql = "SELECT * FROM gameInfos";
        


        return new ChessGame(gameInfo.getState(), gameInfo.getTurn(), loadPieces());
    }

    private Map<Position, Piece> loadPieces() {

        Map<Position, Piece> convertedPieces = new LinkedHashMap<>();
        for (PieceDto piece : pieces) {
            convertedPieces.put(new Position(piece.getX(), piece.getY()), PieceGenerator.generate(piece.getType(), piece.getColor()));
        }

        return convertedPieces;
    }

    private GameInfoDto loadGameInfo(){
        final String pieces_sql = "SELECT * FROM pieces ORDER BY x ASC, y ASC";
        List<PieceDto> pieces = namedParameterJdbcTemplate.query(pieces_sql, Map.of(), pieceRowMapper);
    }

    private void addAll(ChessGame chessGame) {
        Map<Position, Piece> chessboard = chessGame.getChessBoard();

        for (Position position : chessboard.keySet()) {
            addBoard(new PieceDto(chessboard.get(position), position));
        }

        addGameInfos(new GameInfoDto(chessGame.getStateToString(), chessGame.getColorOfTurn()));
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
