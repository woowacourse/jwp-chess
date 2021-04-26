package chess.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@RequiredArgsConstructor
@Getter
public class MoveRequest {

    private final String from;
    private final String to;
}
