package chess.web.controller.dto;

import chess.domain.Room;

public class RoomResponseDto {
    public static class Title{
        private final String title;

        public Title(Room room){
            this.title = room.getTitle();
        }

        public String getTitle() {
            return title;
        }
    }
}
