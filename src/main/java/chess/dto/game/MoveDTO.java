package chess.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class MoveDTO {
    @NotBlank
    private String roomId;

    @NotBlank
    @Pattern(regexp = "^[a-hA-H]{1}[1-8]{1}$")
    private String startPoint;

    @NotBlank
    @Pattern(regexp = "^[a-hA-H]{1}[1-8]{1}$")
    private String endPoint;
}
