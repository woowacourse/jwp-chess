package chess.dao;

import chess.domain.Camp;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {
    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(long gameNo, Map<Position, Piece> board) {
        String sql = "insert into piece (type, white, position, game_no) values (?, ?, ?, ?)";

        for (Entry<Position, Piece> entry : board.entrySet()) {
            updateEachPiece(sql, gameNo, entry);
        }
    }

    public void update(long gameNo, Map<Position, Piece> board) {
        final String sql = "update piece set type = ?, white = ? where position = ? and game_no = ?";

        for (Entry<Position, Piece> entry : board.entrySet()) {
            updateEachPiece(sql, gameNo, entry);
        }
    }

    private void updateEachPiece(String sql, long gameNo, Entry<Position, Piece> entry) {
        Piece piece = entry.getValue();
        String type = piece.getType().toString();
        boolean isWhite = piece.isCamp(Camp.WHITE);
        String position = entry.getKey().toString();

        int result = jdbcTemplate.update(sql, type, isWhite, position, gameNo);
        System.out.println("쿼리 실행 결과: " + result);
    }

    public List<PieceDto> load(long gameNo) {
        final String sql = "select type, white, position from piece where game_no = ?";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> PieceDto.of(
                        resultSet.getString("type"),
                        resultSet.getBoolean("white"),
                        resultSet.getString("position")),
                gameNo
        );
    }
}
