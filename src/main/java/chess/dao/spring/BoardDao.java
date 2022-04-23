package chess.dao.spring;

import chess.domain.Camp;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Map<Position, Piece> board) {
        final String sql = chooseSaveSql();

        for (Entry<Position, Piece> entry : board.entrySet()) {
            savePiece(sql, entry);
        }
    }

    private void savePiece(String sql, Entry<Position, Piece> entry) {
        Piece piece = entry.getValue();
        String type = piece.getType().toString();
        boolean isWhite = piece.isCamp(Camp.WHITE);
        String position = entry.getKey().toString();

        jdbcTemplate.update(sql, type, isWhite, position);
    }

    private String chooseSaveSql() {
        String sql = "insert into piece (game_no, type, white, position) values (1, ?, ?, ?)";
        if (isBoardExistIn()) {
            sql = "update piece set type = ?, white = ? where position = ?";
        }
        return sql;
    }

    private boolean isBoardExistIn() {
        final String sql = "select no from piece";

        return jdbcTemplate.query(sql, ResultSet::next);
    }

    public List<PieceDto> load() {
        final String sql = "select type, white, position from piece";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> PieceDto.of(
                    resultSet.getString("type"),
                    resultSet.getBoolean("white"),
                    resultSet.getString("position"))
        );
    }
}
