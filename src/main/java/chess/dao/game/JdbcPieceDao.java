package chess.dao.game;

import chess.dao.SqlExecutor;
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

public class JdbcPieceDao implements PieceDao {

    private final SqlExecutor executor;

    public JdbcPieceDao(final SqlExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void save(Long gameId, Piece piece) {
        final String sql = "insert into "
                + "Piece(square_file, square_rank, team, piece_type, game_id) "
                + "values (?, ?, ?, ?, ?);";
        executor.insert(connection -> {
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
        final String deleteSql = "delete from Piece where game_id = ?";
        executor.delete(connection -> {
            final PreparedStatement statement = connection.prepareStatement(deleteSql);
            statement.setLong(1, gameId);
            return statement;
        });
    }

    @Override
    public Board findBoardByGameId(final Long gameId) {
        final String sql = "select square_file, square_rank, team, piece_type "
                + "from Piece "
                + "where game_id = ?";

        final Map<Square, Piece> boardValues = executor.select(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, gameId);
            return statement;
        }, this::createBoard);

        return new Board(boardValues);
    }

    private Map<Square, Piece> createBoard(final ResultSet resultSet) throws SQLException {
        final Map<Square, Piece> board = new HashMap<>();

        while (resultSet.next()) {
            final Piece piece = serializePiece(resultSet);
            board.put(piece.getSquare(), piece);
        }

        return board;
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
        return executor.select(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, gameId);
            statement.setString(2, String.valueOf(from.getFile().getValue()));
            statement.setString(3, String.valueOf(from.getRank().getValue()));
            return statement;
        }, this::loadPiece);
    }

    private Piece loadPiece(final ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }
        return serializePiece(resultSet);
    }

    private void moveSource(final Long gameId, final Piece source, final String rawTo) {
        final String sql = "update Piece "
                + "set piece_type = ?, team = ? "
                + "where game_id = ? and square_file = ? and square_rank = ?";
        final Square to = Square.from(rawTo);
        executor.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, source.getPieceType().name());
            statement.setString(2, source.getTeam().name());
            statement.setLong(3, gameId);
            statement.setString(4, String.valueOf(to.getFile().getValue()));
            statement.setString(5, String.valueOf(to.getRank().getValue()));
            return statement;
        });
    }

    private void initBlank(final Long gameId, final String rawFrom) {
        final String sql = "update Piece "
                + "set piece_type = ?, team = ? "
                + "where game_id = ? and square_file = ? and square_rank = ?";

        final Square from = Square.from(rawFrom);
        executor.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, PieceType.BLANK.name());
            statement.setString(2, Team.NONE.name());
            statement.setLong(3, gameId);
            statement.setString(4, String.valueOf(from.getFile().getValue()));
            statement.setString(5, String.valueOf(from.getRank().getValue()));
            return statement;
        });
    }

    private Piece serializePiece(final ResultSet resultSet) throws SQLException {
        final String rawSquare = resultSet.getString("square_file") + resultSet.getString("square_rank");
        final Square square = Square.from(rawSquare);
        final PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
        final Team team = Team.valueOf(resultSet.getString("team"));
        return PieceFactory.createPiece(pieceType, team, square);
    }
}
