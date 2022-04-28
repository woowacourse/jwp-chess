package chess.web.controller.dto;

import chess.domain.entity.Room;

public class RoomResponseDto {
    public static class TitleAndId{
        private final String title;
        private final Long id;

        public TitleAndId(Room room) {
            this.title = room.getTitle();
            this.id = room.getId();
        }

        public String getTitle() {
            return title;
        }

        public Long getId() {
            return id;
        }

    }
}
