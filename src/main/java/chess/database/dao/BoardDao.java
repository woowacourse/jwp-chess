package chess.database.dao;

import chess.database.PieceCache;
import chess.domain.board.Board;
import chess.domain.board.CustomBoardGenerator;
import chess.domain.board.Point;
import chess.domain.piece.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveBoard(int roomId, Board board) {
        String sql = "insert into board (horizontal_index, vertical_index, piece_type, piece_color, room_id) values (?, ?, ?, ?, ?)";
        Map<Point, Piece> pointPiece = board.getPointPiecesForDao();
        List<Object[]> batch = new ArrayList<>();
        for (Map.Entry<Point, Piece> entry : pointPiece.entrySet()) {
            Point point = entry.getKey();
            Piece piece = entry.getValue();
            final Object[] objects = {point.getHorizontal(), point.getVertical(), piece.getType(), piece.getColor(), roomId};
            batch.add(objects);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    public Board readBoard(int roomId) {
        final String sql = "select * from board where room_id = ?";
        final List<Map.Entry<Point, Piece>> entries = jdbcTemplate.query(sql,
            (resultSet, rowNum) -> {
                Point point = Point.of(resultSet.getInt("horizontal_index"),
                    resultSet.getInt("vertical_index"));
                Piece piece = PieceCache.getPiece(resultSet.getString("piece_type"),
                    resultSet.getString("piece_color"));
                return Map.entry(point, piece);
            }, roomId);
        final Map<Point, Piece> pointPieces = entries.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Board.of(new CustomBoardGenerator(pointPieces).generate());
    }

    public void updatePiece(int roomId, Point source, Point destination) {
        final String sql = "update board set horizontal_index = ?, vertical_index = ? "
            + "where horizontal_index = ? and vertical_index = ? and room_id = ?";
        jdbcTemplate.update(sql,
            destination.getHorizontal(), destination.getVertical(),
            source.getHorizontal(), source.getVertical(), roomId);
    }

    public void deletePiece(int roomId, Point destination) {
        final String sql = "DELETE FROM board WHERE horizontal_index = ? and vertical_index = ? and room_id = ?";
        jdbcTemplate.update(sql, destination.getHorizontal(), destination.getVertical(), roomId);
    }

    public void removeBoard(int roomId) {
        final String sql = "DELETE FROM board WHERE room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
