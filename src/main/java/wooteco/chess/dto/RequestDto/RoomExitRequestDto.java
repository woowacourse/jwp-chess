package wooteco.chess.dto.RequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoomExitRequestDto {

    @NotNull(message = "id is not null")
    private Long id;

    @NotBlank(message = "password is mandatory")
    @Size(max=21, message = "password size must be less than 20 characters")
    private String userPassword;

    public Long getId() {
        return id;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
