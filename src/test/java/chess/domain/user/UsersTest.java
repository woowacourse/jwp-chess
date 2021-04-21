package chess.domain.user;

import chess.domain.piece.TeamType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class UsersTest {

    @DisplayName("유저 수가 2명이면 생성 예외가 발생한다.")
    @Test
    void cannotMakeUsers() {
        List<User> users = Arrays.asList(new User(1, "pa", "black", 1),
                new User(1, "paa", "white", 1));

        assertThatCode(() -> new Users(users))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 꽉 찬 방입니다.");
    }

    @DisplayName("유저 수가 0명이면 다음에 추가될 유저의 팀은 백팀이다.")
    @Test
    void isFirstTeamWhite() {
        Users users = new Users(Collections.emptyList());

        assertThat(users.generateTeamType()).isEqualTo(TeamType.WHITE);
    }

    @DisplayName("유저 수가 1명이면 두 번째 유저의 팀은 그 반대 팀이다.")
    @Test
    void isNextTeamOpposite() {
        List<User> userList = Arrays.asList(new User(1, "pa", "BLACK", 1));
        Users users = new Users(userList);

        assertThat(users.generateTeamType()).isEqualTo(TeamType.WHITE);
    }
}
