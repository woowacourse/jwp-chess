package chess.domain.board;

import chess.domain.board.Board;
import chess.domain.board.Team;
import chess.domain.board.Turn;
import chess.domain.board.piece.Pieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardTest {

    @Test
    @DisplayName("체스판을 생성하는 테스트")
    void createBoard() {
        Board board = Board.create(Pieces.createInit(), Turn.init());
        assertThat(board).isExactlyInstanceOf(Board.class);
    }

    @Test
    @DisplayName("흰색 턴에서 블랙 턴으로 바뀐다.")
    public void updateTurn() {
        Board board = Board.create(Pieces.createInit(), Turn.init());
        Board updatedBoard = board.updateTurn(new Turn(Team.BLACK));

        assertThat(updatedBoard.getTurn().getTeam()).isEqualTo(Team.BLACK);
    }

}
