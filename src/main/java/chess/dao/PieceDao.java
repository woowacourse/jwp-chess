package chess.dao;

import chess.domain.piece.Color;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.PieceDto;
import chess.dto.PieceType;
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

    public void savePiece(int chessGameId, PieceDto pieceDto) {
        jdbcTemplate.update("INSERT INTO piece(chess_game_id, position, type, color) VALUES(?, ?, ?, ?)",
            chessGameId, pieceDto.getPosition().toString(), pieceDto.getType().name(), pieceDto.getColor().name());
    }

    public List<PieceDto> findPieces(int chessGameId) {
        return jdbcTemplate.query("SELECT chess_game_id, position, type, color FROM piece WHERE chess_game_id = ?",
                this::mapToPieceDTO, chessGameId);
    }

    public void deletePieceByPosition(int chessGameId, Position position) {
        jdbcTemplate
                .update("DELETE FROM piece WHERE chess_game_id = ? and position = ?", chessGameId, position.toString());
    }

    public void savePieces(int chessGameId, List<PieceDto> pieceDtos) {
        for (PieceDto pieceDTO : pieceDtos) {
            savePiece(chessGameId, pieceDTO);
        }
    }

    public void deleteById(int chessGameId) {
        jdbcTemplate.update("DELETE FROM piece WHERE chess_game_id = ?", chessGameId);
    }

    public void deleteById() {
        jdbcTemplate.update("DELETE FROM piece");
    }

    private PieceDto mapToPieceDTO(ResultSet rs, int rowNum) throws SQLException {
        Position position = createPosition(rs.getString("position"));
        PieceType type = PieceType.valueOf(rs.getString("type"));
        Color color = Color.valueOf(rs.getString("color"));
        return new PieceDto(position, type, color);
    }

    private Position createPosition(String position) {
        File file = File.valueOf(position.substring(0, 1));
        Rank rank = Rank.find(position.substring(1, 2));
        return new Position(file, rank);
    }
}
