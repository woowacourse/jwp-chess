package chess.utils;

import chess.domain.Position;
import chess.piece.Piece;

import java.util.Map;

public interface ChessboardGenerator {

    Map<Position, Piece> generate();
}
