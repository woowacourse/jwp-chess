package chess.repository;

import chess.domain.game.ChessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChessRepository {

    private JdbcTemplate jdbcTemplate;

    public ChessRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addGame(ChessGame chessGame) {
        String addQuery = "INSERT INTO chess_game (turn, finished, board) VALUES (?, ?, ?)";
        jdbcTemplate.update(addQuery, chessGame.getTurn(), chessGame.isOver(), chessGame.serialize());
        String findQuery = "SELECT count(*) FROM chess_game";
        return jdbcTemplate.queryForObject(findQuery, Integer.class);
    }
}
