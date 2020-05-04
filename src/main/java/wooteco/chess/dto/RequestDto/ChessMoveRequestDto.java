package wooteco.chess.dto.RequestDto;

import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.entity.Move;
import wooteco.chess.entity.Room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChessMoveRequestDto {

    @NotNull(message = "id is not null")
    private Long roomId;

    @NotBlank(message = "password is mandatory")
    @Size(max=21, message = "password size must be less than 20 characters")
    private String userPassword;

    @NotBlank(message = "source is mandatory")
    private String source;

    @NotBlank(message = "target is mandatory")
    private String target;

    public ChessMoveRequestDto(Long roomId, String userPassword, String source, String target) {
        this.roomId = roomId;
        this.userPassword = userPassword;
        this.source = source;
        this.target = target;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Move toEntity() {
        return new Move(roomId,source,target);
    }
}
