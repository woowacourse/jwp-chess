package chess.web.controller.dto;

import chess.domain.entity.Room;

public class RoomResponse {
    public static class IdAndTitle {
        private final Long id;
        private final String title;

        public IdAndTitle(Room room) {
            this.title = room.getTitle();
            this.id = room.getId();
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}
