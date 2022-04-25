package chess.database.dao.vanilla;

import chess.database.dao.GameDao;
import chess.database.dto.GameStateDto;
import java.util.ArrayList;
import java.util.List;

public class JdbcGameDao implements GameDao {

    @Override
    public void create(GameStateDto gameStateDto, int roomId) {
        JdbcConnector.query("insert into game(room_id, turn_color, state) values (?, ?, ?)")
            .parameters(Integer.toString(roomId), gameStateDto.getTurnColor(),
                gameStateDto.getState())
            .executeUpdate();
    }

    @Override
    public List<String> readStateAndColor(int roomId) {
        String sql = "select * from game where room_id = ?";
        JdbcConnector.ResultSetHolder holder = JdbcConnector.query(sql)
            .parameters(roomId)
            .executeQuery();
        if (holder.next()) {
            return holder.getStrings("state", "turn_color");
        }
        return new ArrayList<>();
    }

    @Override
    public void updateState(GameStateDto gameStateDto, int roomId) {
        JdbcConnector.query("UPDATE game SET state = ?, turn_color = ? WHERE room_id = ?")
            .parameters(gameStateDto.getState(), gameStateDto.getTurnColor(),
                Integer.toString(roomId))
            .executeUpdate();
    }

    @Override
    public void removeGame(int roomId) {
        JdbcConnector.query("DELETE FROM game WHERE room_id = ?")
            .parameters(roomId)
            .executeUpdate();
    }
}
