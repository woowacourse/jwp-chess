package chess.dao;

import chess.converter.PieceConverter;
import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.ChessGameDto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ChessGameDto chessGameDto) {
        String sql = "insert into chessgame (game_name, turn) values (?, ?)";
        jdbcTemplate.update(sql, chessGameDto.getGameName(), chessGameDto.getTurn());
    }

    public void update(ChessGameDto chessGameDto) {
        String sql = "update chessgame set turn = ? where game_name = ?";
        jdbcTemplate.update(sql, chessGameDto.getTurn(), chessGameDto.getGameName());
    }

    public ChessGame findByName(String gameName) {
        String sql = "select CHESSGAME.turn, CHESSGAME.game_name, PIECE.type, PIECE.team, PIECE.`rank`, PIECE.file from CHESSGAME, PIECE\n"
                + "where CHESSGAME.game_name = PIECE.game_name AND CHESSGAME.game_name = ?;";

        List<ChessGame> result = jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, gameName);
            return preparedStatement;
        }, chessGameRowMapper);

        if(result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    private final RowMapper<ChessGame> chessGameRowMapper = (resultSet, rowNum) -> new ChessGame(
        getTurn(resultSet),
        resultSet.getString("game_name"),
        makeCells(resultSet)
    );

    private String getTurn(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();
        resultSet.next();
        return resultSet.getString("turn");
    }

    private Map<Position, Piece> makeCells(ResultSet resultSet) throws SQLException {
        resultSet.beforeFirst();

        Map<Position, Piece> cells = new HashMap<>();

        while (resultSet.next()) {
            Position position = makePosition(resultSet);
            Piece piece = makePiece(resultSet);
            cells.put(position, piece);
        }

        return cells;
    }

    private Position makePosition(ResultSet resultSet) throws SQLException {
        int rank = resultSet.getInt("rank");
        String file = resultSet.getString("file");

        return Position.of(File.toFile(file.charAt(0)), Rank.toRank(rank));
    }

    private Piece makePiece(ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("type");
        String team = resultSet.getString("team");

        return PieceConverter.from(type, team);
    }

    public void remove(String gameName) {
        String sql = "delete from chessgame where game_name = ?";
        jdbcTemplate.update(sql, gameName);
    }
}
