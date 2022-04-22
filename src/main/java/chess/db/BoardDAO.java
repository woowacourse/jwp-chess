package chess.db;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.domain.state.State;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDAO {

    public static final String FIND_ALL_SQL = "select location, name, color from board where roomID = ?";
    public static final String INSERT_ONE_PIECE_SQL = "insert into board (location, name, color, roomID) values (?, ?, ?, ?)";
    public static final String DELETE_ONE_PIECE_SQL = "delete from board where location = ? and roomID = ?";

    private JdbcTemplate jdbcTemplate;

    public BoardDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PiecesDto> pieceRowMapper = (resultSet, rowNum) -> {
        PiecesDto piece = new PiecesDto(
                resultSet.getString("location"),
                resultSet.getString("color"),
                resultSet.getString("name")
        );
        return piece;
    };

    public void initializePieces(State state, String roomId) {
        Map<Position, Piece> pieces = state.getBoard().getPieces();
        for (Position position : pieces.keySet()) {
            insert(position, pieces.get(position), roomId);
        }
    }

    public Map<Position, Piece> findAllPieces(String roomId) {
        List<PiecesDto> pieces = jdbcTemplate.query(FIND_ALL_SQL, pieceRowMapper, roomId);
        Map<Position, Piece> result = new HashMap<>();
        for (PiecesDto piecesDto : pieces) {
            result.put(new Position(piecesDto.getLocation()), PieceGenerator.of(piecesDto.getName(), piecesDto.getColor()));
        }
        return result;
    }

    public void insert(Position position, Piece piece, String roomId) {
        jdbcTemplate.update(INSERT_ONE_PIECE_SQL, position.getPosition(), piece.getName(), piece.getColor(), roomId);
    }

    public void delete(Position position, String roomId) {
        jdbcTemplate.update(DELETE_ONE_PIECE_SQL, position.getPosition(), roomId);
    }

}
