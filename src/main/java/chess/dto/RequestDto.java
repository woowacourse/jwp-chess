package chess.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestDto {

    private final String title;
    private final String firstMemberName;
    private final String secondMemberName;
    private final String password;

    private RequestDto(String title, String firstMemberName, String secondMemberName, String password) {
        this.title = title;
        this.firstMemberName = firstMemberName;
        this.secondMemberName = secondMemberName;
        this.password = password;
    }

    private static void validateInputSize(List<String> inputs) {
        if (inputs.size() != 3) {
            throw new IllegalArgumentException("입력값이 올바르지 않습니다.");
        }
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
