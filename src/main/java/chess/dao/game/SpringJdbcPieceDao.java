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
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJdbcPieceDao implements PieceDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Piece> pieceRowMapper = (resultSet, rowNumber) -> serializePiece(resultSet);

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

        final List<Piece> pieces = jdbcTemplate.query(sql, pieceRowMapper, gameId);
        final Map<Square, Piece> board = new HashMap<>();
        for (Piece piece : pieces) {
            board.put(piece.getSquare(), piece);
        }

        return new Board(board);
    }

    @Override
    public void move(final Long gameId, final String rawFrom, final String rawTo) {
        Piece source = findPieceByGameIdAndSquare(gameId, rawFrom);
        moveSource(gameId, source, rawTo);
        initBlank(gameId, rawFrom);
    }

    private Piece findPieceByGameIdAndSquare(final Long gameId, final String rawFrom) {
        final String sql = "select square_file, square_rank, team, piece_type "
                + "from Piece "
                + "where game_id = ? and square_file = ? and square_rank = ?";
        final Square from = Square.from(rawFrom);
        return jdbcTemplate.queryForObject(sql, pieceRowMapper,
                gameId, String.valueOf(from.getFile().getValue()), String.valueOf(from.getRank().getValue())
        );
    }

    private void moveSource(final Long gameId, final Piece source, final String rawTo) {
        final String sql = "update Piece "
                + "set piece_type = ?, team = ? "
                + "where game_id = ? and square_file = ? and square_rank = ?";
        final Square to = Square.from(rawTo);
        jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql);
            setPieceMoveParams(statement, source.getPieceType(), source.getTeam(), gameId, to);
            return statement;
        });
    }

    private void initBlank(final Long gameId, final String rawFrom) {
        final String sql = "update Piece "
                + "set piece_type = ?, team = ? "
                + "where game_id = ? and square_file = ? and square_rank = ?";

        final Square from = Square.from(rawFrom);
        jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql);
            setPieceMoveParams(statement, PieceType.BLANK, Team.NONE, gameId, from);
            return statement;
        });
    }

    private void setPieceMoveParams(final PreparedStatement statement, final PieceType blank, final Team none,
                                    final Long gameId, final Square from) throws SQLException {
        statement.setString(1, blank.name());
        statement.setString(2, none.name());
        statement.setLong(3, gameId);
        statement.setString(4, String.valueOf(from.getFile().getValue()));
        statement.setString(5, String.valueOf(from.getRank().getValue()));
    }


    private Piece serializePiece(final ResultSet resultSet) throws SQLException {
        final String rawSquare = resultSet.getString("square_file") + resultSet.getString("square_rank");
        final Square square = Square.from(rawSquare);
        final PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
        final Team team = Team.valueOf(resultSet.getString("team"));
        return PieceFactory.createPiece(pieceType, team, square);
    }
}
