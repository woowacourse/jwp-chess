package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.PieceDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePiece(int chessGameId, Position position, Piece piece) {
        jdbcTemplate.update("INSERT INTO piece(chess_game_id, position, type, color) VALUES(?, ?, ?, ?)",
            chessGameId, position.toString(), piece.getName().getValue(), piece.getColor().name());
    }

    public List<PieceDto> findPieces(int chessGameId) {
        return jdbcTemplate.query("SELECT chess_game_id, position, type, color FROM piece WHERE chess_game_id = ?",
                this::mapToPieceDto, chessGameId);
    }

    public void deletePieceByPosition(int chessGameId, Position position) {
        jdbcTemplate
                .update("DELETE FROM piece WHERE chess_game_id = ? and position = ?", chessGameId, position.toString());
    }

//    public void savePieces(int chessGameId, List<PieceDto> pieceDtos) {
//        for (PieceDto dto : pieceDtos) {
//            savePiece(chessGameId, dto.getPosition(), dto.getPiece());
//        }
//    }

    public void deleteById(int chessGameId) {
        jdbcTemplate.update("DELETE FROM piece WHERE chess_game_id = ?", chessGameId);
    }

    public void deleteById() {
        jdbcTemplate.update("DELETE FROM piece");
    }

    private PieceDto mapToPieceDto(ResultSet rs, int rowNum) throws SQLException {
        String position = rs.getString("position");
        String type = rs.getString("type");
        String color = rs.getString("color");
        return new PieceDto(position, type, color);
    }

}
