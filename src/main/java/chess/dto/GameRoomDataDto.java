package chess.dto;


//TODO 클래스 이름 바꾸기
public class GameRoomDataDto {

    private final String id;
    private final String title;


    public GameRoomDataDto(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
