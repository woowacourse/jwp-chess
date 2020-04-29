package chess.model.repository;

import static chess.model.repository.template.JdbcTemplate.getPssFromParams;
import static chess.model.repository.template.JdbcTemplate.makeQuery;

import chess.model.dto.GameResultDto;
import chess.model.repository.template.JdbcTemplate;
import chess.model.repository.template.PreparedStatementSetter;
import chess.model.repository.template.ResultSetMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ChessResultDao {

    private final static ChessResultDao INSTANCE = new ChessResultDao();

    private ChessResultDao() {
    }

    public static ChessResultDao getInstance() {
        return INSTANCE;
    }

    public List<String> findUsers() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "SELECT USER_NM",
            "  FROM CHESS_RESULT_TB",
            " ORDER BY USER_NM"
        );
        ResultSetMapper<List<String>> mapper = rs -> {
            List<String> users = new ArrayList<>();
            while (rs.next()) {
                users.add(rs.getString("USER_NM"));
            }
            return users;
        };
        return jdbcTemplate.executeQuery(query, pstmt -> {
        }, mapper);
    }

    public void update(String userName, GameResultDto gameResultDto) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "UPDATE CHESS_RESULT_TB",
            "   SET WIN = ?",
            "     , DRAW = ?",
            "     , LOSE = ?",
            " WHERE USER_NM = ?"
        );
        PreparedStatementSetter pss = getPssFromParams(gameResultDto.getWinCount(),
            gameResultDto.getDrawCount(), gameResultDto.getLoseCount(), userName);
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void insert(String userName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "INSERT INTO CHESS_RESULT_TB(USER_NM)",
            "VALUES (?)"
        );
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userName);
        jdbcTemplate.executeUpdate(query, pss);
    }

    public void delete(Set<String> userNames) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "DELETE FROM CHESS_RESULT_TB",
            " WHERE USER_NM = ?"
        );
        PreparedStatementSetter pss = pstmt -> {
            for (String userName : userNames) {
                pstmt.setString(1, userName);
                pstmt.addBatch();
                pstmt.clearParameters();
            }
        };
        jdbcTemplate.executeUpdateWhenLoop(query, pss);
    }

    public Optional<GameResultDto> findWinOrDraw(String userName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String query = makeQuery(
            "SELECT WIN",
            "     , DRAW",
            "     , LOSE",
            "  FROM CHESS_RESULT_TB",
            " WHERE USER_NM = ?"
        );
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userName);
        ResultSetMapper<Optional<GameResultDto>> mapper = rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional
                .of(new GameResultDto(rs.getInt("WIN"), rs.getInt("DRAW"), rs.getInt("LOSE")));
        };
        return jdbcTemplate.executeQuery(query, pss, mapper);
    }
}

