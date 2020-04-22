package chess.model.service;

import chess.model.dto.CreateRoomDto;
import chess.model.dto.DeleteRoomDto;
import chess.model.dto.RoomsDto;
import chess.model.repository.RoomDao;

public class RoomService {

    private static final RoomDao ROOM_DAO = RoomDao.getInstance();
    private static final RoomService INSTANCE = new RoomService();

    private RoomService() {
    }

    public static RoomService getInstance() {
        return INSTANCE;
    }

    public RoomsDto getUsedRooms() {
        return new RoomsDto(ROOM_DAO.selectUsedOnly());
    }

    public void addRoom(CreateRoomDto createRoomDto) {
        ROOM_DAO.insert(createRoomDto.getRoomName(), createRoomDto.getRoomPassword());
    }

    public void deleteRoom(DeleteRoomDto deleteRoomDto) {
        ROOM_DAO.updateUsedN(deleteRoomDto.getRoomId());
        ChessGameService.getInstance().roomBeforeGameOver(deleteRoomDto.getRoomId());
    }
}
