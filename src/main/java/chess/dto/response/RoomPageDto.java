package chess.dto.response;

import java.util.List;

public class RoomPageDto {

    private final int page;
    private final int size;
    private final List<RoomResponseDto> contents;

    private RoomPageDto(final int page, final int size, final List<RoomResponseDto> contents) {
        this.page = page;
        this.size = size;
        this.contents = contents;
    }

    public static RoomPageDto of(final int page, final List<RoomResponseDto> contents) {
        return new RoomPageDto(
                page,
                contents.size(),
                contents
        );
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public List<RoomResponseDto> getContents() {
        return contents;
    }
}
