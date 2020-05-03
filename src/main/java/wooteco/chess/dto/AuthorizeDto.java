package wooteco.chess.dto;

import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;

public class AuthorizeDto {

    private Long id;

    @NotEmpty
    private String password;

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
