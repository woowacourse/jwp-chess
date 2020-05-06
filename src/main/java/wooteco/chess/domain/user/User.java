package wooteco.chess.domain.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import wooteco.chess.domain.piece.Team;

@Table("user")
public class User {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("password")
    private String password;

    @Column("team")
    private String team;

    @Column("user_status")
    private String userStatus;

    public User(final String name, final String password) {
        this.name = name;
        this.password = password;
        team = Team.BLANK.name();
        userStatus = UserStatus.WAITING.name();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getTeam() {
        return team;
    }

    public String getUserStatus() {
        return userStatus;
    }
}
