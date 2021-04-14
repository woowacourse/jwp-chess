package chess.domain.piece.factory;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

import java.util.List;
import java.util.Map;

public interface LocationInitializer {

    List<Piece> whiteInitialize();

    List<Piece> blackInitialize();
}
