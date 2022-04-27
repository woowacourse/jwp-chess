package chess.database.dao.spring;

import chess.database.dao.BoardDao;
import chess.database.dto.BoardDto;
import chess.database.dto.PieceDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringBoardDao implements BoardDao {

    private final JdbcTemplate jdbcTemplate;

    public SpringBoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveBoard(BoardDto boardDto, int roomId) {
        String sql =
            "insert into board (horizontal_index, vertical_index, piece_type, piece_color, room_id)"
                + " values (?, ?, ?, ?, ?)";
        Map<PointDto, PieceDto> pointPieceDto = boardDto.getPointPieces();
        List<Object[]> batch = new ArrayList<>();
        for (Map.Entry<PointDto, PieceDto> entry : pointPieceDto.entrySet()) {
            PointDto point = entry.getKey();
            PieceDto piece = entry.getValue();
            final Object[] objects =
                {point.getHorizontal(), point.getVertical(), piece.getType(), piece.getColor(),
                    roomId};
            batch.add(objects);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    @Override
    public BoardDto readBoard(int roomId) {
        final String sql = "select * from board where room_id = ?";
        final List<Map.Entry<PointDto, PieceDto>> entries = jdbcTemplate.query(sql,
            (resultSet, rowNum) -> {
                PointDto point = PointDto.of(resultSet.getInt("horizontal_index"),
                    resultSet.getInt("vertical_index"));
                PieceDto piece = PieceDto.of(resultSet.getString("piece_type"),
                    resultSet.getString("piece_color"));
                return Map.entry(point, piece);
            }, roomId);
        final Map<PointDto, PieceDto> pointPieces = entries.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        validateExist(pointPieces, roomId);
        return new BoardDto(pointPieces);
    }

    private void validateExist(Map<PointDto, PieceDto> pointPieces, int roomId) {
        if (pointPieces.size() == 0) {
            throw new IllegalArgumentException(String.format("[ERROR] %s에 해당하는 번호의 보드가 없습니다.", roomId));
        }
    }

    @Override
    public void deletePiece(PointDto destination, int roomId) {
        final String sql = "DELETE FROM board WHERE horizontal_index = ? and vertical_index = ? and room_id = ?";
        jdbcTemplate.update(sql, destination.getHorizontal(), destination.getVertical(), roomId);
    }

    @Override
    public void updatePiece(RouteDto routeDto, int roomId) {
        PointDto source = routeDto.getSource();
        PointDto destination = routeDto.getDestination();

        final String sql = "update board set horizontal_index = ?, vertical_index = ? "
            + "where horizontal_index = ? and vertical_index = ? and room_id = ?";
        jdbcTemplate.update(sql,
            destination.getHorizontal(), destination.getVertical(),
            source.getHorizontal(), source.getVertical(), roomId);
    }

    @Override
    public void removeBoard(int roomId) {
        final String sql = "DELETE FROM board WHERE room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
