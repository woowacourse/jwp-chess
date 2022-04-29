package chess.web.controller.dto;

public class RoomRequestDto {

    public static class TitleAndPassword{
        private String title;
        private String password;

        private TitleAndPassword() {
        };

        public TitleAndPassword(String title, String password) {
            this.title = title;
            this.password = password;
        }

        public String getTitle() {
            return title;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class Password{
        private String password;

        private Password(){}
        public Password(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }
    }

}
