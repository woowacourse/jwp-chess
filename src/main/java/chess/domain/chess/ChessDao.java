package chess.domain.chess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import chess.domain.piece.PieceDto;

@Repository
public class ChessDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Chess findChessById(Long chessId) {
        String sql = "SELECT c.status, c.turn, p.position, p.color, p.name "
                + "FROM chess c LEFT JOIN piece p ON c.chess_id = p.chess_id "
                + "WHERE c.chess_id = (?)";

        return jdbcTemplate.queryForObject(sql, chessMapper(), chessId);
    }

    private RowMapper<Chess> chessMapper() {
        return (resultSet, rowNum) -> {
            final String status = resultSet.getString("c.status");
            final String turn = resultSet.getString("c.turn");
            final List<PieceDto> pieceDtos = pieceDtoSFromResultSet(resultSet);

            return Chess.of(pieceDtos, status, turn);
        };
    }

    private List<PieceDto> pieceDtoSFromResultSet(ResultSet resultSet) throws SQLException {
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
        try {
            String sql = "INSERT INTO chess (status, turn) VALUES ('RUNNING', 'WHITE')";
            Connection connection = jdbcTemplate.getDataSource()
                                                .getConnection();
            PreparedStatement preparedStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                return keys.getLong(1);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        throw new IllegalStateException("체스 게임 생성에 실패했습니다.");
    }


    public void updateChess(Long chessId, String status, String turn) {
        String sql = "UPDATE chess SET status = (?), turn = (?) WHERE chess_id = (?)";
        jdbcTemplate.update(sql, status, turn, chessId);
    }
}
