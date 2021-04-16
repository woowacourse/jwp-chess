package chess.web.domain.board.setting;

import chess.web.domain.piece.type.PieceWithColorType;
import java.util.List;

public class BoardCustomSetting extends BoardSetting {
    public BoardCustomSetting(List<PieceWithColorType> piecesSetting) {
        super(piecesSetting);
    }
}
