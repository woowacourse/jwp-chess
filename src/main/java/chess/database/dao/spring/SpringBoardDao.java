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
    public void saveBoard(BoardDto boardDto, String roomName) {
        String sql = "insert into board (horizontal_index, vertical_index, piece_type, piece_color, room_name)"
            + " values (?, ?, ?, ?, ?)";
        Map<PointDto, PieceDto> pointPieceDto = boardDto.getPointPieces();
        List<Object[]> batch = new ArrayList<>();
        for (Map.Entry<PointDto, PieceDto> entry : pointPieceDto.entrySet()) {
            PointDto point = entry.getKey();
            PieceDto piece = entry.getValue();
            final Object[] objects =
                {point.getHorizontal(), point.getVertical(), piece.getType(), piece.getColor(), roomName};
            batch.add(objects);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    @Override
    public BoardDto readBoard(String roomName) {
        final String sql = "select * from board where room_name = ?";
        final List<Map.Entry<PointDto, PieceDto>> entries = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            PointDto point = PointDto.of(resultSet.getInt("horizontal_index"), resultSet.getInt("vertical_index"));
            PieceDto piece = PieceDto.of(resultSet.getString("piece_type"), resultSet.getString("piece_color"));
            return Map.entry(point, piece);
        }, roomName);
        final Map<PointDto, PieceDto> pointPieces = entries.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        validateExist(pointPieces, roomName);
        return new BoardDto(pointPieces);
    }

    private void validateExist(Map<PointDto, PieceDto> pointPieces, String roomName) {
        if (pointPieces.size() == 0) {
            throw new IllegalArgumentException(
                String.format("[ERROR] %s에 해당하는 이름의 보드가 없습니다.", roomName)
            );
        }
    }

    @Override
    public void deletePiece(PointDto destination, String roomName) {
        final String sql = "DELETE FROM board WHERE horizontal_index = ? and vertical_index = ? and room_name = ?";
        jdbcTemplate.update(sql, destination.getHorizontal(), destination.getVertical(), roomName);
    }

    @Override
    public void updatePiece(RouteDto routeDto, String roomName) {
        PointDto source = routeDto.getSource();
        PointDto destination = routeDto.getDestination();

        final String sql = "update board set horizontal_index = ?, vertical_index = ? "
            + "where horizontal_index = ? and vertical_index = ? and room_name = ?";
        jdbcTemplate.update(sql,
            destination.getHorizontal(), destination.getVertical(),
            source.getHorizontal(), source.getVertical(), roomName);
    }

    @Override
    public void removeBoard(String roomName) {
        final String sql = "DELETE FROM board WHERE room_name = ?";
        jdbcTemplate.update(sql, roomName);
    }
}
