package chess.dao;

import chess.dto.ChessBoardDto;
import chess.dto.ChessGameDto;
import chess.dto.PieceDto;
import chess.dto.PositionDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PieceDao {
    private final JdbcTemplate jdbcTemplate;

    public PieceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long id, ChessGameDto chessGameDto) {
        ChessBoardDto chessBoard = chessGameDto.getChessBoard();
        Map<PositionDto, PieceDto> cells = chessBoard.getCells();

        updateCells(id, cells);
    }

    private void updateCells(Long id, Map<PositionDto, PieceDto> cells) {
        String sql = "insert into piece (type, team, `rank`, file, chessgame_id) values (?, ?, ?, ?, ?)";

        List<Object[]> parameters = new ArrayList<>();
        for (PositionDto positionDto : cells.keySet()) {
            parameters.add(new Object[]{
                    cells.get(positionDto).getSymbol(),
                    cells.get(positionDto).getTeam(),
                    positionDto.getRank(),
                    positionDto.getFile(),
                    id});
        }

        jdbcTemplate.batchUpdate(sql, parameters);
    }

    public void delete(Long id) {
        String sql = "delete from piece where chessgame_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void update(Long id, ChessGameDto chessGameDto) {
        delete(id);
        save(id, chessGameDto);
    }
}
