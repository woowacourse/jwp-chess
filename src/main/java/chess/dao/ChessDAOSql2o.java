package chess.dao;

import chess.domain.ChessGame;
import chess.domain.TeamColor;
import chess.domain.piece.Piece;
import java.util.List;
import java.util.Optional;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

public class ChessDAOSql2o implements ChessDAO {

    private final Sql2o sql2o;

    public ChessDAOSql2o() {
        sql2o = new Sql2o("jdbc:postgresql://localhost:5432/chess", "nabom", "1234");
    }

    private void pieceBulkUpdate(ChessGame chessGame, Long gameId, Connection connection) {
        try (Query query = connection.createQuery(
            "insert into game(gameid, name, color, position) values(:gameId, :name, :color,:position)")) {
            chessGame.pieces().asList().forEach(piece -> {
                query.addParameter("gameId", gameId)
                    .addParameter("name", piece.name())
                    .addParameter("color", piece.color())
                    .addParameter("position", piece.currentPosition().columnAndRow())
                    .addToBatch();
            });
            query.executeBatch();
        }
    }

    @Override
    public void deleteAllByGameId(Long gameId) {
        try (Connection con = sql2o.open()) {
            con.createQuery("delete from game where gameid = :gameId")
                .addParameter("gameId", gameId)
                .executeUpdate();

            con.createQuery("delete from current_color where game_id = :gameId")
                .addParameter("gameId", gameId)
                .executeUpdate();
        }
    }

    @Override
    public Optional<TeamColor> currentTurnByGameId(Long gameId) {
        try (Connection con = sql2o.open()) {
            List<String> colors =
                con.createQuery("select color from current_color where game_id = :gameId")
                    .addParameter("gameId", gameId)
                    .executeAndFetch(String.class);

            if (colors.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(TeamColor.valueOf(colors.get(0)));
        }
    }

    @Override
    public List<ChessDto> chessByGameId(Long gameId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("select * from game where gameid=:gameId")
                .addParameter("gameId", gameId)
                .executeAndFetch(ChessDto.class);
        }
    }

    @Override
    public void savePieces(Long gameId, List<Piece> pieces) {
        try (Connection connection = sql2o.beginTransaction(); Query query = connection.createQuery(
            "insert into game(gameid, name, color, position) values(:gameId, :name, :color,:position)")) {
            pieces.forEach(piece -> {
                query.addParameter("gameId", gameId)
                    .addParameter("name", piece.name())
                    .addParameter("color", piece.color())
                    .addParameter("position", piece.currentPosition().columnAndRow())
                    .addToBatch();
            });
            query.executeBatch();
            connection.commit();
        }

    }

    @Override
    public Long saveCurrentColor(Long gameId, TeamColor teamColor) {
        try (Connection con = sql2o.open()) {
            con.createQuery("insert into current_color(game_id, color) values(:gameId, :color)")
                .addParameter("gameId", gameId)
                .addParameter("color", teamColor)
                .executeUpdate();
            return gameId;
        }
    }
}
