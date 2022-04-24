package chess.dao;

import static chess.dao.DBConnector.getConnection;

import chess.dto.ChessBoardDto;
import chess.dto.ChessGameDto;
import chess.dto.PieceDto;
import chess.dto.PositionDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String gameName = chessGameDto.getGameName();

            statement.setString(1, gameName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void update(ChessGameDto chessGameDto) {
        delete(chessGameDto);
        save(chessGameDto);
    }
}
