package chess.dao;

import static chess.util.RandomCreationUtils.createUuid;

import chess.domain.db.BoardPiece;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcBoardPieceDao implements BoardPieceDao {

    private static final String SAVE_DML = "insert into board_pieces (board_piece_id, game_id, position, piece) values (?, ?, ?, ?)";
    private static final String FIND_LAST_BOARD_PIECE_DML = "select * from board_pieces where game_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(String gameId, Map<String, String> positionToPiece) {
        final Set<String> positions = positionToPiece.keySet();

        List<Object[]> boardData = new ArrayList<>();

        for (String position : positions) {
            String piece = positionToPiece.get(position);
            String boardId = createUuid();
            boardData.add(new Object[]{boardId, gameId, position, piece});
        }

        jdbcTemplate.batchUpdate(SAVE_DML, boardData);
    }

    @Override
    public List<BoardPiece> findLastBoardPiece(String lastGameId) {
        return jdbcTemplate.query(FIND_LAST_BOARD_PIECE_DML,
                (rs, rowNum) -> new BoardPiece(
                        rs.getString("board_piece_id"),
                        rs.getString("game_id"),
                        rs.getString("position"),
                        rs.getString("piece")
                ),
                lastGameId);
    }
}
