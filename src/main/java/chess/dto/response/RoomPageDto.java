package chess.dto.response;

import java.util.List;

public class RoomPageDto {

    private final int currentPage;
    private final int lastPage;
    private final int size;
    private final List<RoomResponseDto> rooms;

    public RoomPageDto(final int currentPage, final int lastPage, final int size,
                       final List<RoomResponseDto> rooms) {
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.size = size;
        this.rooms = rooms;
    }

    public static RoomPageDto of(final int currentPage, final int lastPage, final List<RoomResponseDto> rooms) {
        return new RoomPageDto(
                currentPage,
                lastPage,
                rooms.size(),
                rooms
        );
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public int getSize() {
        return size;
    }

    public List<RoomResponseDto> getRooms() {
        return rooms;
    }
}
