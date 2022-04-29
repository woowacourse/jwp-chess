package chess.dto.request.web;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaveRequest {

    private String currentTeam;

    private Map<String, String> pieces;
}
