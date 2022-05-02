package chess.dao;

import chess.domain.Board;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.Room;
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
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final ChessPieceDao pieceDao;
    private final MemberDao memberDao;

    public ChessGameDao(final JdbcTemplate jdbcTemplate, final ChessPieceDao pieceDao,
                        final MemberDao memberDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.pieceDao = pieceDao;
        this.memberDao = memberDao;
    }

    @Override
    public ChessGame save(ChessGame game) {
        final Long gameId = saveGame(game);
        savePieces(gameId, game.getBoard());
        return new ChessGame(gameId, game.getBoard(), game.getTurn(), game.getRoom());
    }

    @Override
    public Optional<ChessGame> findById(Long id) {
        final String sql = "select id, title, password, turn, white_member_id, black_member_id from Game where id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        sql, (resultSet, rowNum) ->
                                makeChessGame(resultSet.getLong("id"), resultSet),
                        id)
        );
    }

    @Override
    public List<ChessGame> findAll() {
        final String sql = "select id, title, password, turn, white_member_id, black_member_id from Game";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) ->
                        makeChessGame(resultSet.getLong("id"), resultSet));
    }

    @Override
    public List<ChessGame> findHistoriesByMemberId(Long memberId) {
        final String sql = "select id, turn, title, password, white_member_id, black_member_id from Game"
                + " where turn = ? and (white_member_id = ? or black_member_id = ?)";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum)
                        -> makeChessGame(resultSet.getLong("id"), resultSet),
                Team.NONE.name(), memberId, memberId);
    }

    @Override
    public void terminate(ChessGame game) {
        updateGame(game);
    }

    @Override
    public void updateByMove(final ChessGame game, final String rawFrom, final String rawTo) {
        updateGame(game);
        movePiece(game, rawFrom, rawTo);
    }

    @Override
    public void deleteGameById(final Long id) {
        final String sql = "delete from Game where id = ?";
        jdbcTemplate.update(sql, id);

    }

    private Long saveGame(ChessGame game) {
        final String sql = "insert into Game (turn, title, password, white_member_id, black_member_id) "
                + "values (?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, game.getTurn().name());
            ps.setString(2, game.getRoom().getTitle());
            ps.setString(3, game.getRoom().getPassword());
            ps.setLong(4, game.getWhiteId());
            ps.setLong(5, game.getBlackId());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private ChessGame makeChessGame(Long id, ResultSet resultSet) throws SQLException {
        final Member white = memberDao.findById(resultSet.getLong("white_member_id"))
                .orElseThrow(() -> new RuntimeException("찾는 멤버가 존재하지 않습니다."));
        final Member black = memberDao.findById(resultSet.getLong("black_member_id"))
                .orElseThrow(() -> new RuntimeException("찾는 멤버가 존재하지 않습니다."));
        final String rawTurn = resultSet.getString("turn");
        final String title = resultSet.getString("title");
        final String password = resultSet.getString("password");
        final Participant participant = new Participant(white, black);
        return new ChessGame(id, loadBoard(id), Team.valueOf(rawTurn), new Room(title, password, participant));
    }

    private void movePiece(final ChessGame game, final String rawFrom, final String rawTo) {
        updatePiece(game, rawFrom);
        updatePiece(game, rawTo);
    }

    private void updatePiece(final ChessGame game, final String rawSquare) {
        deletePiece(game, rawSquare);
        insertPiece(game, rawSquare);
    }

    private void insertPiece(final ChessGame game, final String rawSquare) {
        final Square square = Square.from(rawSquare);
        final Piece piece = game.getBoard().getPieceAt(square);
        pieceDao.save(game.getId(), piece);
    }

    private void deletePiece(final ChessGame game, final String rawSquare) {
        final String sql = "delete from Piece where game_id = ? and square_rank = ? and square_rank = ?";
        final String[] split = rawSquare.split("");
        jdbcTemplate.update(sql, game.getId(), split[0], split[1]);
    }

    private void updateGame(ChessGame game) {
        final String sql = "update Game set turn = ? where id = ?";
        jdbcTemplate.update(sql, game.getTurn().name(), game.getId());
    }

    private void savePieces(Long gameId, Board board) {
        pieceDao.saveAll(gameId, board.getPieces());
    }

    private Board loadBoard(Long gameId) {
        final String sql = "select square_file, square_rank, team, piece_type "
                + "from Piece "
                + "where game_id = ?";
        final List<Piece> pieces = jdbcTemplate.query(sql, (resultSet, rowNum) -> toPiece(resultSet), gameId);
        final Map<Square, Piece> boardValue = new HashMap<>();

        for (Piece piece : pieces) {
            boardValue.put(piece.getSquare(), piece);
        }

        return new Board(boardValue);
    }

    private Piece toPiece(ResultSet resultSet) throws SQLException {
        final String rawSquare = resultSet.getString("square_file") + resultSet.getString("square_rank");
        final Square square = Square.from(rawSquare);
        final PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
        final Team team = Team.valueOf(resultSet.getString("team"));

        return PieceFactory.createPiece(pieceType, team, square);
    }
}
