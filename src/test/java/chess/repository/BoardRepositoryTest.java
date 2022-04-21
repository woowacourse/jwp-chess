package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.entity.BoardEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("저장된 보드를 가져온다")
    void getBoard() {
        insertInitialData();
        assertThat(boardRepository.getBoard()).hasSize(64);
    }

    @DisplayName("a2 위치의 기물을 blank로 업데이트한다")
    @Test
    void update() {
        insertInitialData();
        final BoardEntity source = new BoardEntity(1L, "a2", "blank");
        boardRepository.updatePosition(source);
        final BoardEntity updatedPiece = boardRepository.findBoardByRoomIdAndPosition(1L, "a2");
        assertThat(updatedPiece.getPiece()).isEqualTo("blank");
    }

    @DisplayName("a2와 a4의 기물을  blank, white_pawn으로 변경한다")
    @Test
    void updateBatch() {
        insertInitialData();
        final BoardEntity source = new BoardEntity(1L, "a2", "blank");
        final BoardEntity target = new BoardEntity(1L, "a4", "white_pawn");
        boardRepository.updateBatchPositions(List.of(source, target));

        assertAll(
            () -> assertThat(boardRepository.findBoardByRoomIdAndPosition(1L, "a2")
                .getPiece())
                .isEqualTo("blank"),
            () -> assertThat(boardRepository.findBoardByRoomIdAndPosition(1L, "a4")
                .getPiece())
                .isEqualTo("white_pawn")
        );
    }

    private void insertInitialData() {
        final Map<Position, Piece> boards = BoardFactory.initialize();
        final List<BoardEntity> boardEntities = boards.entrySet().stream()
            .map(entry -> new BoardEntity(1L, entry.getKey().convertPositionToString(),
                entry.getValue().convertPieceToString()))
            .collect(Collectors.toList());
        boardRepository.batchInsert(boardEntities);
    }

}
