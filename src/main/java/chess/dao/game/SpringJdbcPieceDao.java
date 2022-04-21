package chess.dao.game;

import chess.domain.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJdbcPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Board> boardRowMapper = (resultSet, rowNumber) -> createBoard(resultSet);

    private Board createBoard(ResultSet resultSet) throws SQLException {
        final Map<Square, Piece> board = new HashMap<>();

        while (!resultSet.isAfterLast()) {
            final String rawSquare = resultSet.getString("square_file") + resultSet.getString("square_rank");
            final Square square = Square.from(rawSquare);
            final PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
            final Team team = Team.valueOf(resultSet.getString("team"));
            final Piece piece = PieceFactory.createPiece(pieceType, team, square);
            board.put(square, piece);
            resultSet.next();
        }

        return new Board(board);
    }

    @Autowired
    public SpringJdbcPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(final Long gameId, final Piece piece) {
        final String sql = "insert into "
                + "Piece(square_file, square_rank, team, piece_type, game_id) "
                + "values (?, ?, ?, ?, ?);";
        jdbcTemplate.update((connection) -> {
            final PreparedStatement statement = connection.prepareStatement(sql);
            setPieceSaveParams(gameId, piece, statement);
            return statement;
        });
    }

    private void setPieceSaveParams(Long gameId,
                                    Piece piece,
                                    PreparedStatement statement) throws SQLException {
        statement.setString(1, String.valueOf(piece.getSquare().getFile().getValue()));
        statement.setString(2, String.valueOf(piece.getSquare().getRank().getValue()));
        statement.setString(3, piece.getTeam().name());
        statement.setString(4, piece.getPieceType().name());
        statement.setLong(5, gameId);
    }

    @Override
    public void deletePiecesByGameId(final Long gameId) {
        final String sql = "delete from Piece where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public Board findBoardByGameId(final Long gameId) {
        final String sql = "select square_file, square_rank, team, piece_type "
                + "from Piece "
                + "where game_id = ?";

        return jdbcTemplate.queryForObject(sql, boardRowMapper, gameId);
    }
}
