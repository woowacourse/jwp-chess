package wooteco.chess.dto;

import javax.validation.constraints.NotEmpty;

public class AuthorizeDto {

    private final Long id;

    @NotEmpty
    private final String password;

    public AuthorizeDto(@NotEmpty final Long id, @NotEmpty final String password) {
        this.id = id;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
