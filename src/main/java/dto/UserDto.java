package dto;

import javax.validation.constraints.Size;

public class UserDto {
    @Size(min = 2, max = 4)
    private final String name;
    @Size(min = 2, max = 8)
    private final String pw;

    public UserDto(@Size(min = 2, max = 4) final String name, @Size(min = 2, max = 8) final String pw) {
        this.name = name;
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }
}
