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
public class BoardPieceDao {

    private static final String SAVE_DML = "insert into board_pieces (board_piece_id, game_id, position, piece) values (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public void save(String gameId, Map<String, String> piecesByPositions) {
        final Set<String> positions = piecesByPositions.keySet();

        List<Object[]> boardData = new ArrayList<>();

        for (String position : positions) {
            String piece = piecesByPositions.get(position);
            String boardId = createUuid();
            boardData.add(new Object[]{boardId, gameId, position, piece});
        }

        jdbcTemplate.batchUpdate(SAVE_DML, boardData);
    }

    public List<BoardPiece> findLastBoardPiece(String lastGameId) {
        final String lastBoardPieceDml = "select * from board_pieces where game_id = ?";
        final List<BoardPiece> boardPieces = jdbcTemplate.query(lastBoardPieceDml,
                (rs, rowNum) -> new BoardPiece(
                        rs.getString("board_piece_id"),
                        rs.getString("game_id"),
                        rs.getString("position"),
                        rs.getString("piece")
                ),
                lastGameId);
        return boardPieces;
    }
}
