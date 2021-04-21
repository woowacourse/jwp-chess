package chess.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class ResultDTO {
    @NotBlank
    private String roomId;

    @NotBlank
    private String winner;

    @NotBlank
    private String loser;
}