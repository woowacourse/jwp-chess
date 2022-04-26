package chess.database.dao.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.database.dao.BoardDao;
import chess.database.dto.BoardDto;
import chess.database.dto.PieceDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;

@Repository
public class SpringBoardDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveBoard(BoardDto boardDto, Long gameId) {
        String sql = "INSERT INTO board (horizontal_index, vertical_index, piece_type, piece_color, game_id)"
            + " VALUES (?, ?, ?, ?, ?)";
        Map<PointDto, PieceDto> pointPieceDto = boardDto.getPointPieces();
        List<Object[]> batch = new ArrayList<>();
        for (Map.Entry<PointDto, PieceDto> entry : pointPieceDto.entrySet()) {
            PointDto point = entry.getKey();
            PieceDto piece = entry.getValue();
            final Object[] objects =
                {point.getHorizontal(), point.getVertical(), piece.getType(), piece.getColor(), gameId};
            batch.add(objects);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    @Override
    public BoardDto findBoardById(Long gameId) {
        final String sql = "SELECT * FROM board WHERE game_id = ?";
        final List<Map.Entry<PointDto, PieceDto>> entries = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            PointDto point = PointDto.of(resultSet.getInt("horizontal_index"), resultSet.getInt("vertical_index"));
            PieceDto piece = PieceDto.of(resultSet.getString("piece_type"), resultSet.getString("piece_color"));
            return Map.entry(point, piece);
        }, gameId);
        final Map<PointDto, PieceDto> pointPieces = entries.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        validateExist(pointPieces, gameId);
        return new BoardDto(pointPieces);
    }

    private void validateExist(Map<PointDto, PieceDto> pointPieces, Long gameId) {
        if (pointPieces.size() == 0) {
            throw new IllegalArgumentException(
                String.format("[ERROR] %d 번에 해당하는 보드가 없습니다.", gameId)
            );
        }
    }

    @Override
    public void deletePiece(PointDto destination, Long gameId) {
        final String sql = "DELETE FROM board WHERE horizontal_index = ? AND vertical_index = ? AND game_id = ?";
        jdbcTemplate.update(sql, destination.getHorizontal(), destination.getVertical(), gameId);
    }

    @Override
    public void updatePiece(RouteDto routeDto, Long gameId) {
        PointDto source = routeDto.getSource();
        PointDto destination = routeDto.getDestination();

        final String sql = "UPDATE board SET horizontal_index = ?, vertical_index = ? "
            + "WHERE horizontal_index = ? AND vertical_index = ? AND game_id = ?";
        jdbcTemplate.update(sql,
            destination.getHorizontal(), destination.getVertical(),
            source.getHorizontal(), source.getVertical(), gameId);
    }

    @Override
    public void removeBoard(Long gameId) {
        final String sql = "DELETE FROM board WHERE game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }
}
