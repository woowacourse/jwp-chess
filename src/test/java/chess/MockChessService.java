package chess;

import chess.domain.board.Board;
import chess.domain.board.Point;
import chess.domain.chessgame.ChessGame;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import chess.dto.web.PointDto;
import chess.dto.web.PointsDto;
import chess.dto.web.RoomDto;
import chess.dto.web.UsersInRoomDto;
import chess.service.ChessService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockChessService implements ChessService {

    @Override
    public List<RoomDto> openedRooms() {
        return new ArrayList<>();
    }

    @Override
    public BoardDto latestBoard(String id) {
        return new BoardDto(new Board());
    }

    @Override
    public UsersInRoomDto usersInRoom(String id) {
        return new UsersInRoomDto("white", "0", "0", "black", "0", "0");
    }

    @Override
    public RoomDto create(RoomDto roomDto) {
        return new RoomDto("1", "roomName", "white", "black");
    }

    @Override
    public GameStatusDto gameStatus(String id) {
        return new GameStatusDto(new ChessGame(new Board()));
    }

    @Override
    public BoardDto start(String id) {
        return new BoardDto(new Board());
    }

    @Override
    public BoardDto exit(String id) {
        return new BoardDto(new Board());
    }

    @Override
    public void close(String id) {
    }

    @Override
    public PointsDto movablePoints(String id, String point) {
        return new PointsDto(
            new ArrayList<>(Collections.singletonList(new PointDto(Point.of("a1")))));
    }

    @Override
    public BoardDto move(String id, String source, String destination) {
        return new BoardDto(new Board());
    }
}
