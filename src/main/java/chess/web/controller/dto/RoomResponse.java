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

    public static class PasswordAndFinish {
        private final String password;
        private final boolean finish;

        public PasswordAndFinish(Room room, boolean finish) {
            this.password = room.getPassword();
            this.finish = finish;
        }

        public String getPassword() {
            return password;
        }

        public boolean isFinish() {
            return finish;
        }
    }

    public static class Password {
        private final String password;

        public Password(Room room) {
            this.password = room.getPassword();
        }

        public String getPassword() {
            return password;
        }
    }
}
