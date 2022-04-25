package chess.database.dao.vanilla;

import java.util.HashMap;
import java.util.Map;

import chess.database.dao.BoardDao;
import chess.database.dto.BoardDto;
import chess.database.dto.PieceDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;

public class JdbcBoardDao implements BoardDao {

    @Override
    public void saveBoard(BoardDto boardDto, int roomId) {
        JdbcConnector connector = JdbcConnector.query(
            "insert into board (horizontal_index, vertical_index, piece_type, piece_color, room_id)"
                + " values (?, ?, ?, ?, ?)");

        Map<PointDto, PieceDto> pointPieceDto = boardDto.getPointPieces();
        for (Map.Entry<PointDto, PieceDto> entry : pointPieceDto.entrySet()) {
            PointDto point = entry.getKey();
            PieceDto piece = entry.getValue();
            connector = connector
                .parameters(point.getHorizontal(), point.getVertical())
                .parameters(piece.getType(), piece.getColor(), Integer.toString(roomId))
                .batch();
        }
        connector.executeBatch();
    }

    @Override
    public BoardDto readBoard(int roomId) {
        JdbcConnector.ResultSetHolder holder = JdbcConnector.query(
                "select * from board where room_id = ?")
            .parameters(roomId)
            .executeQuery();

        Map<PointDto, PieceDto> pointPieceDto = new HashMap<>();
        while (holder.next()) {
            PointDto point = PointDto.of(
                holder.getInteger("horizontal_index"), holder.getInteger("vertical_index")
            );
            PieceDto piece = PieceDto.of(
                holder.getString("piece_type"), holder.getString("piece_color")
            );
            pointPieceDto.put(point, piece);
        }
        validateExist(pointPieceDto, roomId);
        return new BoardDto(pointPieceDto);
    }

    private void validateExist(Map<PointDto, PieceDto> pointPieces, int roomId) {
        if (pointPieces.size() == 0) {
            throw new IllegalArgumentException(
                String.format("[ERROR] %s에 해당하는 번호의 보드가 없습니다.", roomId)
            );
        }
    }

    @Override
    public void deletePiece(PointDto pointDto, int roomId) {
        JdbcConnector.query(
                "DELETE FROM board WHERE horizontal_index = ? and vertical_index = ? and room_id = ?")
            .parameters(pointDto.getHorizontal(), pointDto.getVertical())
            .parameters(roomId)
            .executeUpdate();
    }

    @Override
    public void updatePiece(RouteDto routeDto, int roomId) {
        PointDto source = routeDto.getSource();
        PointDto destination = routeDto.getDestination();
        JdbcConnector.query("update board set horizontal_index = ?, vertical_index = ? "
                + "where horizontal_index = ? and vertical_index = ? and room_id = ?")
            .parameters(destination.getHorizontal(), destination.getVertical(),
                source.getHorizontal(), source.getVertical())
            .parameters(roomId)
            .executeUpdate();
    }

    @Override
    public void removeBoard(int roomId) {
        JdbcConnector.query("DELETE FROM board WHERE room_id = ?")
            .parameters(roomId)
            .executeUpdate();
    }
}
