package chess.repository.dao;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.ChessBoardDto;
import chess.dto.PieceDeserializeTable;
import chess.dto.PieceDto;
import chess.repository.PieceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PieceDao implements PieceRepository {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePieces(ChessGameManager chessGameManager, int gameId) {
        Map<String, PieceDto> board = ChessBoardDto.from(chessGameManager.getBoard()).board();
        String query = "INSERT INTO piece(game_id, name, color, position) VALUES(?, ?, ?, ?)";
        Set<String> positions = board.keySet();
        this.jdbcTemplate.batchUpdate(query, positions, positions.size(), (ps, argument) -> {
            ps.setInt(1, gameId);
            PieceDto piece = board.get(argument);
            ps.setString(2, piece.getName());
            ps.setString(3, piece.getColor());
            ps.setString(4, argument);
        });
    }

    public ChessBoard findChessBoardByGameId(int gameId) {
        String queryPiece = "SELECT * FROM piece WHERE game_id = ?";
        ChessBoard chessBoard = this.jdbcTemplate.queryForObject(queryPiece, chessBoardRowMapper, gameId);
        return chessBoard;
    }

    private final RowMapper<ChessBoard> chessBoardRowMapper = (resultSet, rowNum) -> {
        Map<Position, Piece> board = new HashMap<>();

        do {
            Piece piece = PieceDeserializeTable.deserializeFrom(
                    resultSet.getString("name"),
                    Color.of(resultSet.getString("color"))
            );
            Position position = Position.of(resultSet.getString("position"));
            board.put(position, piece);
        } while (resultSet.next());
        return ChessBoard.from(board);
    };

    public Piece findPieceByPosition(Position position, int gameId) {
        String queryPieceByPosition = "SELECT * FROM piece WHERE game_id = ? AND position = ?";
        Piece piece = this.jdbcTemplate.queryForObject(queryPieceByPosition, pieceRowMapper, gameId, position.getNotation());
        return piece;
    }

    private final RowMapper<Piece> pieceRowMapper = (resultSet, rowNum) ->
            PieceDeserializeTable.deserializeFrom(
                    resultSet.getString("name"),
                    Color.of(resultSet.getString("color"))
            );

    public void savePiece(Piece piece, Position position, int gameId) {
        String query = "INSERT INTO piece(game_id, name, color, position) VALUES(?, ?, ?, ?)";
        this.jdbcTemplate.update(query, gameId, piece.getName(), piece.getColor().name(), position.getNotation());
    }

    public void deletePieceByPosition(Position position, int gameId) {
        String queryPieceByPosition = "DELETE FROM piece WHERE game_id = ? AND position = ?";
        this.jdbcTemplate.update(queryPieceByPosition, gameId, position.getNotation());
    }

    @Override
    public void updatePiecePosition(Position fromPosition, Position toPosition, int gameId) {
        String query = "UPDATE piece SET position = ? WHERE game_id = ? AND position = ?";
        this.jdbcTemplate.update(query, toPosition.getNotation(), gameId, fromPosition.getNotation());
    }
}
