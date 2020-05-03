package chess.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.model.domain.piece.PieceFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PieceLetterConverterTest {

    @DisplayName("Piece가 null일 때, convertToLetter Test")
    @Test
    void convertToLetter_pieceNull() {
        assertThatThrownBy(() -> PieceLetterConverter.convertToLetter(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("null");
    }

    @DisplayName("Piece를 넣었을 때, convert되는 letter가 기대값과 동일한지 테스트")
    @ParameterizedTest
    @CsvSource(value = {"BLACK_KING, ♚", "WHITE_KING, ♔", "BLACK_QUEEN, ♛", "WHITE_QUEEN, ♕"
        , "BLACK_BISHOP, ♝", "WHITE_BISHOP, ♗", "BLACK_KNIGHT, ♞", "WHITE_KNIGHT, ♘"
        , "BLACK_ROOK, ♜", "WHITE_ROOK, ♖", "BLACK_PAWN, ♟", "WHITE_PAWN, ♙"})
    void convertToLetter(String pieceName, String expected) {
        String letter = PieceLetterConverter.convertToLetter(PieceFactory.getPiece(pieceName));
        assertThat(letter).isEqualTo(expected);
    }
}