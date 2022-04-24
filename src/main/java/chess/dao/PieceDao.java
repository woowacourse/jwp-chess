package chess.dao;

import static chess.domain.position.File.A;
import static chess.domain.position.File.B;
import static chess.domain.position.File.C;
import static chess.domain.position.File.D;
import static chess.domain.position.File.E;
import static chess.domain.position.File.F;
import static chess.domain.position.File.G;
import static chess.domain.position.File.H;
import static chess.domain.position.Rank.EIGHT;
import static chess.domain.position.Rank.FIVE;
import static chess.domain.position.Rank.FOUR;
import static chess.domain.position.Rank.ONE;
import static chess.domain.position.Rank.SEVEN;
import static chess.domain.position.Rank.SIX;
import static chess.domain.position.Rank.THREE;
import static chess.domain.position.Rank.TWO;

import chess.domain.piece.Color;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import chess.dto.PieceDto;
import chess.dto.PieceType;

@Repository
public class PieceDao {

    private static final Map<String, Rank> RANKS = Map.of(
            "1", ONE, "2", TWO, "3", THREE, "4", FOUR, "5", FIVE, "6", SIX, "7", SEVEN, "8", EIGHT
    );
    private static final Map<String, File> FILES = Map.of(
            "A", A, "B", B, "C", C, "D", D, "E", E, "F", F, "G", G, "H", H
    );

    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PieceDto> findPieces(int chessGameId) {
        return jdbcTemplate.query("SELECT chess_game_id, position, type, color FROM piece WHERE chess_game_id = ?",
                this::mapToPieceDTO, chessGameId);
    }

    private PieceDto mapToPieceDTO(ResultSet rs, int rowNum) throws SQLException {
        Position position = createPosition(rs.getString("position"));
        PieceType type = PieceType.valueOf(rs.getString("type"));
        Color color = Color.valueOf(rs.getString("color"));
        return new PieceDto(position, type, color);
    }

    private Position createPosition(String position) {
        File file = FILES.get(position.substring(0, 1));
        Rank rank = RANKS.get(position.substring(1, 2));
        return new Position(file, rank);
    }

    public void deletePieceByPosition(int chessGameId, Position position) {
        jdbcTemplate
                .update("DELETE FROM piece WHERE chess_game_id = ? and position = ?", chessGameId, position.toString());
    }

    public void savePiece(int chessGameId, PieceDto pieceDto) {
        jdbcTemplate.update("INSERT INTO piece(chess_game_id, position, type, color) VALUES(?, ?, ?, ?)",
                chessGameId, pieceDto.getPosition().toString(), pieceDto.getType().name(), pieceDto.getColor().name());
    }

    public void savePieces(int chessGameId, List<PieceDto> pieceDtos) {
        for (PieceDto pieceDTO : pieceDtos) {
            savePiece(chessGameId, pieceDTO);
        }
    }

    public void deleteAll(int chessGameId) {
        jdbcTemplate.update("DELETE FROM piece WHERE chess_game_id = ?", chessGameId);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM piece");
    }
}
