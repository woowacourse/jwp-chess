package chess.dao;

import chess.domain.Board;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseGameDao implements GameDao {

    private final PieceDao pieceDao = new PieceDao();
    private final SqlExecutor executor = SqlExecutor.getInstance();
    private final MemberDao memberDao;

    public DatabaseGameDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Long save(ChessGame game) {
        final Long gameId = saveGame(game);
        savePieces(gameId, game.getBoard());
        return gameId;
    }

    private Long saveGame(ChessGame game) {
        final String sql = "insert into Game (turn, white_member_id, black_member_id) values (?, ?, ?)";
        return executor.insertAndGetGeneratedKey(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, game.getTurn().name());
            statement.setLong(2, game.getWhiteId());
            statement.setLong(3, game.getBlackId());
            return statement;
        });
    }

    @Override
    public Optional<ChessGame> findById(Long id) {
        final String sql = "select id, turn, white_member_id, black_member_id from Game where id = ?";
        final ChessGame game = executor.select(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            return statement;
        }, resultSet -> loadChessGame(id, resultSet));
        return Optional.ofNullable(game);
    }

    private ChessGame loadChessGame(Long id, ResultSet resultSet)
            throws SQLException {
        if (!resultSet.next()) {
            return null;
        }
        return makeChessGame(id, resultSet);
    }

    @Override
    public List<ChessGame> findAll() {
        final String sql = "select id, turn, white_member_id, black_member_id from Game";
        return executor.select(connection -> connection.prepareStatement(sql), this::makeAllChessGame);
    }

    private List<ChessGame> makeAllChessGame(ResultSet resultSet) throws SQLException {
        final List<ChessGame> games = new ArrayList<>();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            games.add(makeChessGame(id, resultSet));
        }
        return games;
    }

    @Override
    public List<ChessGame> findHistoriesByMemberId(Long memberId) {
        return findAll().stream()
                .filter(ChessGame::isEnd)
                .filter(game -> Objects.equals(game.getBlackId(), memberId)
                        || Objects.equals(game.getWhiteId(), memberId))
                .collect(Collectors.toList());
    }

    @Override
    public void update(ChessGame game) {
        updateGame(game);
        movePieces(game);
    }

    private void updateGame(ChessGame game) {
        final String sql = "update Game set turn = ? where id = ?";
        executor.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, game.getTurn().name());
            statement.setLong(2, game.getId());
            return statement;
        });
        savePieces(game.getId(), game.getBoard());
    }

    private void movePieces(ChessGame game) {
        final String deleteSql = "delete from Piece where game_id = ?";
        executor.delete(connection -> {
            final PreparedStatement statement = connection.prepareStatement(deleteSql);
            statement.setLong(1, game.getId());
            return statement;
        });
        savePieces(game.getId(), game.getBoard());
    }

    private void savePieces(Long gameId, Board board) {
        final List<Piece> pieces = board.getPieces();
        for (final Piece piece : pieces) {
            pieceDao.save(gameId, piece);
        }
    }

    private ChessGame makeChessGame(Long id, ResultSet resultSet) throws SQLException {
        final Member white = memberDao.findById(resultSet.getLong("white_member_id"))
                .orElseThrow(() -> new RuntimeException("찾는 멤버가 존재하지 않습니다."));
        final Member black = memberDao.findById(resultSet.getLong("black_member_id"))
                .orElseThrow(() -> new RuntimeException("찾는 멤버가 존재하지 않습니다."));
        final String rawTurn = resultSet.getString("turn");
        return new ChessGame(id, loadBoard(id), Team.valueOf(rawTurn),
                new Participant(white, black));
    }

    private Board loadBoard(Long gameId) {
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

    private Map<Square, Piece> createBoard(ResultSet resultSet) throws SQLException {
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

    private class PieceDao {
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
    }
}
