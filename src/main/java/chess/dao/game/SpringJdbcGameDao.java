package chess.dao.game;

import chess.dao.member.MemberDao;
import chess.domain.Board;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Team;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SpringJdbcGameDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final PieceDao pieceDao;
    private final MemberDao memberDao;
    private final RowMapper<ChessGame> gameRowMapper = (resultSet, rowNumber) -> makeChessGame(resultSet);

    @Autowired
    public SpringJdbcGameDao(final JdbcTemplate jdbcTemplate, final PieceDao pieceDao, final MemberDao memberDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.pieceDao = pieceDao;
        this.memberDao = memberDao;
    }

    @Override
    public Long save(final ChessGame game) {
        final Long gameId = saveGame(game);
        savePieces(gameId, game.getBoard());
        return gameId;
    }

    private Long saveGame(ChessGame game) {
        final String sql = "insert into Game (turn, white_member_id, black_member_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, game.getTurn().name());
            statement.setLong(2, game.getWhiteId());
            statement.setLong(3, game.getBlackId());
            return statement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void savePieces(Long gameId, Board board) {
        final List<Piece> pieces = board.getPieces();
        for (final Piece piece : pieces) {
            pieceDao.save(gameId, piece);
        }
    }

    @Override
    public Optional<ChessGame> findById(final Long id) {
        final String sql = "select id, turn, white_member_id, black_member_id from Game where id = ?";
        final ChessGame game = jdbcTemplate.queryForObject(sql, gameRowMapper, id);
        return Optional.ofNullable(game);
    }

    private ChessGame makeChessGame(ResultSet resultSet) throws SQLException {
        final Member white = memberDao.findById(resultSet.getLong("white_member_id"))
                .orElseThrow(() -> new RuntimeException("찾는 멤버가 존재하지 않습니다."));
        final Member black = memberDao.findById(resultSet.getLong("black_member_id"))
                .orElseThrow(() -> new RuntimeException("찾는 멤버가 존재하지 않습니다."));
        final Long id = resultSet.getLong("id");
        final String rawTurn = resultSet.getString("turn");
        return new ChessGame(id, pieceDao.findBoardByGameId(id), Team.valueOf(rawTurn),
                new Participant(white, black));
    }

    @Override
    public List<ChessGame> findAll() {
        final String sql = "select id, turn, white_member_id, black_member_id from Game";
        return jdbcTemplate.query(sql, gameRowMapper);
    }

    @Override
    public List<ChessGame> findHistoriesByMemberId(final Long memberId) {
        return findAll().stream()
                .filter(ChessGame::isEnd)
                .filter(game -> Objects.equals(game.getBlackId(), memberId)
                        || Objects.equals(game.getWhiteId(), memberId))
                .collect(Collectors.toList());
    }

    @Override
    public void move(final ChessGame game, final String rawFrom, final String rawTo) {
        pieceDao.move(game.getId(), rawFrom, rawTo);
        reverseTurn(game);
    }

    private void reverseTurn(final ChessGame game) {
        final String sql = "update Game set turn = ? where id = ?";

        jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, game.getTurn().name());
            statement.setLong(2, game.getId());
            return statement;
        });
    }

    @Override
    public void terminate(final Long id) {
        final String sql = "update Game set turn = ? where id = ?";

        jdbcTemplate.update(connection -> {
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, Team.NONE.name());
            statement.setLong(2, id);
            return statement;
        });
    }
}
