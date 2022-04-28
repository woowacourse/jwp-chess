package chess.dto.requestDto;

public class NewGameInfoDto {

    private final String title;
    private final String firstMemberName;
    private final String secondMemberName;
    private final String password;

    private NewGameInfoDto(String title, String firstMemberName, String secondMemberName, String password) {
        this.title = title;
        this.firstMemberName = firstMemberName;
        this.secondMemberName = secondMemberName;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstMemberName() {
        return firstMemberName;
    }

    public String getSecondMemberName() {
        return secondMemberName;
    }

    public String getPassword() {
        return password;
    }
}
