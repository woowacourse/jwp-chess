package chess.dto.game;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public final class SectionDTO {
    @NotBlank
    private String roomId;

    @NotBlank
    @Pattern(regexp = "^[a-hA-H]{1}[1-8]{1}$")
    private String clickedSection;
}
