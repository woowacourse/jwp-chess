package chess.dto.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserNamesDto {

    private final List<String> userNames;

    public UserNamesDto(List<String> userNames) {
        this.userNames = Collections.unmodifiableList(new ArrayList<>(userNames));
    }

    public List<String> getUserNames() {
        return userNames;
    }

    @Override
    public String toString() {
        return "UserNamesDto{" +
            "userNames=" + userNames +
            '}';
    }
}
