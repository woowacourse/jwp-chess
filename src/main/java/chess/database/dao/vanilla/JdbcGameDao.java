package chess.database.dao.vanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import chess.database.dto.GameStateDto;
import chess.database.dao.GameDao;

public class JdbcGameDao implements GameDao {

    @Override
    public List<String> readStateAndColor(String roomName) {
        String sql = "select * from game where room_name = ?";
        JdbcConnector.ResultSetHolder holder = JdbcConnector.query(sql)
            .parameters(roomName)
            .executeQuery();
        if (holder.next()) {
            return holder.getStrings("state", "turn_color");
        }
        return new ArrayList<>();
    }

    @Override
    public void saveGame(GameStateDto gameStateDto, String roomName) {
        JdbcConnector.query("insert into game(room_name, turn_color, state) values (?, ?, ?)")
            .parameters(roomName, gameStateDto.getTurnColor(), gameStateDto.getState())
            .executeUpdate();
    }

    @Override
    public void updateState(GameStateDto gameStateDto, String roomName) {
        JdbcConnector.query("UPDATE game SET state = ?, turn_color = ? WHERE room_name = ?")
            .parameters(gameStateDto.getState(), gameStateDto.getTurnColor(), roomName)
            .executeUpdate();
    }

    @Override
    public void removeGame(String roomName) {
        JdbcConnector.query("DELETE FROM game WHERE room_name = ?")
            .parameters(roomName)
            .executeUpdate();
    }
}
