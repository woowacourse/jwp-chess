package chess.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.dto.ChessBoardDto;
import chess.dto.ChessGameDto;
import chess.dto.PieceDto;
import chess.dto.PositionDto;

@Repository
public class PieceDao {
    private JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ChessGameDto chessGameDto) {
        String gameName = chessGameDto.getGameName();
        ChessBoardDto chessBoard = chessGameDto.getChessBoard();
        Map<PositionDto, PieceDto> cells = chessBoard.getCells();

        updateCells(gameName, cells);
    }

    private void updateCells(String gameName, Map<PositionDto, PieceDto> cells) {
        String sql = "insert into piece (type, team, `rank`, file, game_name) values (?, ?, ?, ?, ?)";

        List<Object[]> parameters = new ArrayList<>();
        for (PositionDto positionDto : cells.keySet()) {
            parameters.add(new Object[]{
                    cells.get(positionDto).getSymbol(),
                    cells.get(positionDto).getTeam(),
                    positionDto.getRank(),
                    positionDto.getFile(),
                    gameName});
        }

        jdbcTemplate.batchUpdate(sql, parameters);
    }

    public void delete(ChessGameDto chessGameDto) {
        String sql = "delete from piece where game_name = ?";
        jdbcTemplate.update(sql, chessGameDto.getGameName());
    }

    public void update(ChessGameDto chessGameDto) {
        delete(chessGameDto);
        save(chessGameDto);
    }

    public void deleteByPosition(String position, String gameName) {
        String sql = "delete from piece where `rank` = ? and file = ? and game_name = ?";
        String file = position.substring(0, 1);
        String rank = position.substring(1, 2);
        jdbcTemplate.update(sql, Integer.parseInt(rank), file, gameName);
    }

    public void updatePosition(String from, String to, String gameName) {
        String sql = "update piece set `rank` = ?, file = ? where `rank` = ? and file = ? and game_name = ?";
        String fromFile = from.substring(0, 1);
        String fromRank = from.substring(1, 2);

        String toFile = to.substring(0, 1);
        String toRank = to.substring(1, 2);
        jdbcTemplate.update(sql, Integer.parseInt(toRank), toFile, Integer.parseInt(fromRank), fromFile, gameName);
    }
}
