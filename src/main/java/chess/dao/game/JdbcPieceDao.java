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

    @Override
    public void deletePiecesByGameId(final Long gameId) {
        final String deleteSql = "delete from Piece where game_id = ?";
        executor.delete(connection -> {
            final PreparedStatement statement = connection.prepareStatement(deleteSql);
            statement.setLong(1, gameId);
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
            final String rawSquare = resultSet.getString("square_file") + resultSet.getString("square_rank");
            final Square square = Square.from(rawSquare);
            final PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
            final Team team = Team.valueOf(resultSet.getString("team"));
            final Piece piece = PieceFactory.createPiece(pieceType, team, square);
            board.put(square, piece);
        }

        return board;
    }
}
