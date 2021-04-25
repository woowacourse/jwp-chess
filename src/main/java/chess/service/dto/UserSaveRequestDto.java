package chess.service.dto;

import chess.entity.User;

public class UserSaveRequestDto {

    private String name;
    private String password;

    public UserSaveRequestDto() {
    }

    public UserSaveRequestDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public User toEntity() {
        return new User(name, password);
    }
}
