package domain.state;

import domain.pieces.Piece;
import domain.pieces.Pieces;

import java.util.Set;

public interface State {
	State pushCommand(String input);

	boolean isReported();

	boolean isPlaying();

	boolean isEnded();

	Set<Piece> getSet();

	Pieces getPieces();

	String getStateName();
}
