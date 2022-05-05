package chess.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.position.Position;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ChessGame chessGame, int chessGameId) {
        ChessBoard chessBoard = chessGame.getChessBoard();
        deleteByChessGameId(chessGameId);
        Map<Position, Piece> cells = chessBoard.getCells();
        saveCells(cells, chessGameId);
    }

    private void deleteByChessGameId(int chessGameId) {
        String sql = "delete from piece where chess_game_id = ?";

        jdbcTemplate.update(sql, chessGameId);
    }

    private void saveCells(Map<Position, Piece> cells, int chessGameId) {
        String sql = "insert into piece (type, team, `rank`, file, chess_game_id) values (?, ?, ?, ?, ?)";

        List<Object[]> parameters = new ArrayList<>();
        for (Position position : cells.keySet()) {
            parameters.add(new Object[]{
                    cells.get(position).getSymbol(),
                    cells.get(position).getTeam().getTeam(),
                    position.getRank().getRank(),
                    String.valueOf(position.getFile().getFile()),
                    chessGameId});
        }

        jdbcTemplate.batchUpdate(sql, parameters);
    }

    public void deleteByPosition(String position, int chessGameId) {
        String sql = "delete from piece where `rank` = ? and file = ? and chess_game_id = ?";
        String file = position.substring(0, 1);
        String rank = position.substring(1, 2);
        jdbcTemplate.update(sql, Integer.parseInt(rank), file, chessGameId);
    }

    public void updatePosition(String from, String to, int chessGameId) {
        String sql = "update piece set `rank` = ?, file = ? where `rank` = ? and file = ? and chess_game_id = ?";
        String fromFile = from.substring(0, 1);
        String fromRank = from.substring(1, 2);

        String toFile = to.substring(0, 1);
        String toRank = to.substring(1, 2);
        jdbcTemplate.update(sql, Integer.parseInt(toRank), toFile, Integer.parseInt(fromRank), fromFile, chessGameId);
    }
}
