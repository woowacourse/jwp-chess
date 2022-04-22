package chess.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import chess.domain.Board;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

@Repository
public class DatabaseGameDao implements GameDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PieceDao pieceDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Long save(ChessGame game) {
        final Long gameId = saveGame(game);
        savePieces(gameId, game.getBoard());
        return gameId;
    }

    private Long saveGame(ChessGame game) {
        final String sql = "insert into Game (turn, white_member_id, black_member_id) values (?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, game.getTurn().name());
            ps.setLong(2, game.getWhiteId());
            ps.setLong(3, game.getBlackId());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<ChessGame> findById(Long id) {
        final String sql = "select id, turn, white_member_id, black_member_id from Game where id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        sql, (resultSet, rowNum) ->
                                makeChessGame(resultSet.getLong("id"), resultSet),
                        id)
        );
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

    @Override
    public List<ChessGame> findAll() {
        final String sql = "select id, turn, white_member_id, black_member_id from Game";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) ->
                        makeChessGame(resultSet.getLong("id"), resultSet));
    }

    @Override
    public List<ChessGame> findHistoriesByMemberId(Long memberId) {
        final String sql = "select id, turn, white_member_id, black_member_id from Game"
                + " where turn = ? and (white_member_id = ? or black_member_id = ?)";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum)
                        -> makeChessGame(resultSet.getLong("id"), resultSet),
                Team.NONE.name(), memberId, memberId);
    }

    @Override
    public void update(ChessGame game) {
        updateGame(game);
        movePieces(game);
    }

    private void updateGame(ChessGame game) {
        final String sql = "update Game set turn = ? where id = ?";
        jdbcTemplate.update(sql, game.getTurn().name(), game.getId());
    }

    private void movePieces(ChessGame game) {
        final String sql = "delete from Piece where game_id = ?";
        jdbcTemplate.update(sql, game.getId());
        savePieces(game.getId(), game.getBoard());
    }

    private void savePieces(Long gameId, Board board) {
        final List<Piece> pieces = board.getPieces();
        for (final Piece piece : pieces) {
            pieceDao.save(gameId, piece);
        }
    }

    private Board loadBoard(Long gameId) {
        final String sql = "select square_file, square_rank, team, piece_type "
                + "from Piece "
                + "where game_id = ?";

        return new Board(jdbcTemplate.queryForObject(
                sql, (resultSet, rowNum) -> createBoard(resultSet), gameId)
        );
    }

    private Map<Square, Piece> createBoard(ResultSet resultSet) throws SQLException {
        final Map<Square, Piece> board = new HashMap<>();

        do {
            final String rawSquare = resultSet.getString("square_file") + resultSet.getString("square_rank");
            final Square square = Square.from(rawSquare);
            final PieceType pieceType = PieceType.valueOf(resultSet.getString("piece_type"));
            final Team team = Team.valueOf(resultSet.getString("team"));
            final Piece piece = PieceFactory.createPiece(pieceType, team, square);
            board.put(square, piece);
        } while (resultSet.next());

        return board;
    }
}
