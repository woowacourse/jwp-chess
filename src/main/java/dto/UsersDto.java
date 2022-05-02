package dto;

import chess.domain.game.Room;
import java.util.List;
import java.util.stream.Collectors;

public class UsersDto {
    private final List<UserDto> users;

    public UsersDto(List<UserDto> users) {
        this.users = users;
    }

    public static UsersDto of(List<Room> rooms) {

        return new UsersDto(rooms
                .stream()
                .map(room -> new UserDto(room.getId(), room.getPassword()))
                .collect(Collectors.toList()));
    }

    public List<UserDto> getUsers() {
        return users;
    }
}
