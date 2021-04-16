package chess.domain.piece.factory;

import chess.domain.piece.Piece;
import java.util.List;

public interface LocationInitializer {

    List<Piece> whiteInitialize();

    List<Piece> blackInitialize();
}
