package chess.domain.piece.generator;

import java.util.Map;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public interface PiecesGenerator {

    Map<Position, Piece> generate();
}
