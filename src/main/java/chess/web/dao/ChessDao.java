package chess.web.dao;

import chess.domain.chess.Chess;
import chess.web.dto.PieceDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Chess findChessById(Long chessId) {
        String sql = "SELECT c.status, c.turn, p.position, p.color, p.name "
                + "FROM chess AS c LEFT JOIN piece AS p ON c.chess_id = p.chess_id "
                + "WHERE c.chess_id = (?)";

        return jdbcTemplate.queryForObject(sql, chessMapper(), chessId);
    }

    private RowMapper<Chess> chessMapper() {
        return (resultSet, rowNum) -> {
            final String status = resultSet.getString("c.status");
            final String turn = resultSet.getString("c.turn");
            final List<PieceDto> pieceDtos = pieceDTOSFromResultSet(resultSet);

            return Chess.of(pieceDtos, status, turn);
        };
    }

    private List<PieceDto> pieceDTOSFromResultSet(ResultSet resultSet) throws SQLException {
        List<PieceDto> pieceDtos = new ArrayList<>();

        resultSet.last();
        if (resultSet.getRow() == 1) {
            return pieceDtos;
        }

        resultSet.beforeFirst();
        while (resultSet.next()) {
            final String position = resultSet.getString("position");
            final String color = resultSet.getString("color");
            final String name = resultSet.getString("name");

            pieceDtos.add(new PieceDto(position, color, name));
        }

        return pieceDtos;
    }

    public long insert() {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO chess (status, turn) VALUES ('RUNNING', 'WHITE')";
        jdbcTemplate.update(con -> con.prepareStatement(
                sql, new String[]{"chess_id"}), keyHolder);
        return keyHolder.getKey().longValue();
    }


    public void updateChess(Long chessId, String status, String turn) {
        String sql = "UPDATE chess SET status = (?), turn = (?) WHERE chess_id = (?)";
        jdbcTemplate.update(sql, status, turn, chessId);
    }
}
