package chess.db;

import chess.domain.board.Board;
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

    private static final String FIND_ALL_SQL = "select location, name, color from board where roomID = ?";
    private static final String INSERT_ONE_PIECE_SQL = "insert into board (location, name, color, roomID) values (?, ?, ?, ?)";
    private static final String DELETE_BOARD_SQL = "delete from board where roomID = ?";

    private final JdbcTemplate jdbcTemplate;

    public BoardDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PiecesDTO> pieceRowMapper = (resultSet, rowNum) ->
            new PiecesDTO(resultSet.getString("location"),
                    resultSet.getString("color"),
                    resultSet.getString("name")
    );

    public Map<Position, Piece> findAllPieces(String roomId) {
        List<PiecesDTO> pieces = jdbcTemplate.query(FIND_ALL_SQL, pieceRowMapper, roomId);
        Map<Position, Piece> result = new HashMap<>();
        for (PiecesDTO piecesDto : pieces) {
            result.put(new Position(piecesDto.getLocation()), PieceGenerator.of(piecesDto.getName(), piecesDto.getColor()));
        }
        return result;
    }

    public void deleteBoard(String roomId) {
        jdbcTemplate.update(DELETE_BOARD_SQL, roomId);
    }

    public void insertBoard(Board board, String roomId) {
        Map<Position, Piece> pieces = board.getPieces();
        for (Position position : pieces.keySet()) {
            insert(position, pieces.get(position), roomId);
        }
    }

    private void insert(Position position, Piece piece, String roomId) {
        jdbcTemplate.update(INSERT_ONE_PIECE_SQL, position.getPosition(), piece.getName(), piece.getColor(), roomId);
    }


}
