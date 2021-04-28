package chess.dto.room;

import chess.controller.spring.vo.Pagination;

import java.util.List;

public class RoomResponseDTO {

    private final List<RoomDTO> roomDtos;
    private final Pagination pagination;

    public RoomResponseDTO(List<RoomDTO> roomDtos, Pagination pagination) {
        this.roomDtos = roomDtos;
        this.pagination = pagination;
    }

    public List<RoomDTO> getRoomDtos() {
        return roomDtos;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
