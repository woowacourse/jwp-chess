package chess.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static chess.mysql.DbConnectionTemplate.executeQuery;
import static chess.mysql.DbConnectionTemplate.executeQueryWithGenerateKey;

public class MysqlChessDao implements ChessDao {
    @Override
    public ChessGameDto save(ChessGameDto entity) {
        String query =
                "INSERT INTO chessgame " +
                        "(pieces, running, next_turn) " +
                        "VALUES (?, ?, ?)";

        Long gameId = executeQueryWithGenerateKey(query, ps -> {
            ps.setString(1, entity.getPieces());
            ps.setBoolean(2, entity.isRunning());
            ps.setString(3, entity.getNextTurn());
        }, rs -> rs.getLong(1));

        return new ChessGameDto(gameId, entity.getNextTurn(), entity.isRunning(), entity.getPieces());
    }

    @Override
    public Optional<ChessGameDto> findById(long id) {
        String query =
                "SELECT * " +
                        "FROM chessgame " +
                        "WHERE id = ?";

        return executeQuery(query,
                (preparedStatement -> preparedStatement.setLong(1, id)),
                this::getOptionalChessGame);
    }


    @Override
    public void update(ChessGameDto entity) {
        String query =
                "UPDATE chessgame " +
                        "SET pieces = ?, running = ? , next_turn = ?" +
                        "WHERE id = ?";

        executeQuery(query, ps -> {
            ps.setString(1, entity.getPieces());
            ps.setBoolean(2, entity.isRunning());
            ps.setString(3, entity.getNextTurn());
            ps.setLong(4, entity.getId());
        });
    }

    @Override
    public List<ChessGameDto> findAllOnRunning() {
        String query =
                "SELECT * " +
                        "FROM chessgame " +
                        "WHERE running = ?";

        return executeQuery(query,
                ps -> ps.setBoolean(1, true),
                this::getChessGames);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM chessgame " +
                "WHERE id = ?";

        executeQuery(query, ps -> ps.setLong(1, id));
    }

    private Optional<ChessGameDto> getOptionalChessGame(ResultSet resultSet) {
        try (ResultSet rs = resultSet) {
            if (!rs.next()) {
                return Optional.empty();
            }

            Long rowId = rs.getLong("id");
            boolean isRunning = rs.getBoolean("running");
            String pieces = rs.getString("pieces");
            String nextTurn = rs.getString("next_turn");

            return Optional.of(new ChessGameDto(rowId, nextTurn, isRunning, pieces));
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private List<ChessGameDto> getChessGames(ResultSet resultSet) {
        try (ResultSet rs = resultSet) {
            List<ChessGameDto> chessGameDtos = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("id");
                String pieces = rs.getString("pieces");
                boolean isRunning = rs.getBoolean("running");
                String nextTurn = rs.getString("next_turn");
                chessGameDtos.add(new ChessGameDto(id, nextTurn, isRunning, pieces));
            }
            return chessGameDtos;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}

