package chess.dto.response;

import java.util.List;

public class RoomPageDto {

    private final int currentPage;
    private final int lastPage;
    private final int size;
    private final List<RoomResponseDto> contents;

    public RoomPageDto(final int currentPage, final int lastPage, final int size,
                       final List<RoomResponseDto> contents) {
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.size = size;
        this.contents = contents;
    }

    public static RoomPageDto of(final int currentPage, final int lastPage, final List<RoomResponseDto> contents) {
        return new RoomPageDto(
                currentPage,
                lastPage,
                contents.size(),
                contents
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

    public List<RoomResponseDto> getContents() {
        return contents;
    }
}
