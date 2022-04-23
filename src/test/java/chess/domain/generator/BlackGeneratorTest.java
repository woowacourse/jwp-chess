package chess.domain.generator;

import chess.domain.piece.*;
import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BlackGeneratorTest {

    @Test
    @DisplayName("모든 체스말을 생성한다.")
    void generatePieces() {
        final List<Piece> expected = new ArrayList<>(createPawn());
        expected.addAll(List.of(
                new Rook(new Position(8, 'a')),
                new Rook(new Position(8, 'h')),
                new Knight(new Position(8, 'b')),
                new Knight(new Position(8, 'g')),
                new Bishop(new Position(8, 'c')),
                new Bishop(new Position(8, 'f')),
                new Queen(new Position(8, 'd')),
                new King(new Position(8, 'e'))
        ));
        final BlackGenerator generator = new BlackGenerator();

        final List<Piece> actual = generator.generate();

        assertThat(actual).isEqualTo(expected);
    }

    private List<Piece> createPawn() {
        final List<Piece> pawns = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            pawns.add(new Pawn(new Position(7, (char) ('a' + i))));
        }
        return pawns;
    }
}
