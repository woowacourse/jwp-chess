package chess.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import chess.domain.chess.Chess;
import chess.domain.piece.PieceDto;
import chess.repository.ChessRepository;

@Repository
public class ChessDao implements ChessRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChessDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Chess findChessById(long chessId) {
        List<PieceDto> pieceDtos = findPiecesById(chessId);
        String sql = "SELECT c.chess_id, c.status, c.turn FROM chess c WHERE c.chess_id = ?";
        return jdbcTemplate.queryForObject(sql, chessMapper(pieceDtos), chessId);
    }

    private List<PieceDto> findPiecesById(long chessId) {
        String sql =
                "SELECT p.piece_id, p.position, p.color, p.name FROM piece p WHERE p.chess_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final String position = rs.getString("position");
            final String color = rs.getString("color");
            final String name = rs.getString("name");
            return new PieceDto(position, color, name);
        }, chessId);
    }

    private RowMapper<Chess> chessMapper(List<PieceDto> pieceDtos) {
        return (resultSet, rowNum) -> {
            final String status = resultSet.getString("status");
            final String turn = resultSet.getString("turn");
            return Chess.of(pieceDtos, status, turn);
        };
    }
}
