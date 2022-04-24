package chess.dao;

import static chess.util.RandomCreationUtils.createUuid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessBoardDao {

    // chess_board_id	game_id	team_color	position	piece
    private static final String saveDml = "insert into chess_boards (chess_board_id, game_id, position, piece) values (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public ChessBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String gameId, Map<String, String> piecesByPositions) {

        final Set<String> positions = piecesByPositions.keySet();

        List<Object[]> boardData = new ArrayList<>();

        for (String position : positions) {
            String piece = piecesByPositions.get(position);
            String boardId = createUuid();
            boardData.add(new Object[]{boardId, gameId, position, piece});
        }

        jdbcTemplate.batchUpdate(saveDml, boardData);
    }
}
