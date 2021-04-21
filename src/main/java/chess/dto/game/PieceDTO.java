package chess.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class PieceDTO {
    private final String team;
    private final String initial;
    private final String position;
}
