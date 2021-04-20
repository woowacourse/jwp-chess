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

import java.util.HashMap;
import java.util.Map;

@Repository
public class PieceDao implements PieceRepository {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePieces(ChessGameManager chessGameManager, int gameId) {
        Map<String, PieceDto> board = ChessBoardDto.from(chessGameManager.getBoard()).board();
        for (String position : board.keySet()) {
            String query = "insert into piece(game_id, name, color, position) values(?, ?, ?, ?)";

            PieceDto piece = board.get(position);
            this.jdbcTemplate.update(query, gameId, piece.getName(), piece.getColor(), position);
        }
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
}
