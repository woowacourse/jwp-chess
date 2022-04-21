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
class BoardRepositoryImplTest {

    @Autowired
    private BoardRepositoryImpl boardRepositoryImpl;

    @DisplayName("저장된 보드를 가져온다")
    @Test
    void findBoardByRoomId() {
        insertInitialData();
        assertThat(boardRepositoryImpl.findBoardByRoomId(1L)).hasSize(64);
    }

    @DisplayName("a2 위치의 기물을 blank로 업데이트한다")
    @Test
    void update() {
        insertInitialData();
        final BoardEntity source = new BoardEntity(1L, "a2", "blank");
        boardRepositoryImpl.updatePosition(source);
        final BoardEntity updatedPiece = boardRepositoryImpl.findBoardByRoomIdAndPosition(1L, "a2");
        assertThat(updatedPiece.getPiece()).isEqualTo("blank");
    }

    @DisplayName("a2와 a4의 기물을  blank, white_pawn으로 변경한다")
    @Test
    void updateBatch() {
        insertInitialData();
        final BoardEntity source = new BoardEntity(1L, "a2", "blank");
        final BoardEntity target = new BoardEntity(1L, "a4", "white_pawn");
        boardRepositoryImpl.updateBatchPositions(List.of(source, target));

        assertAll(
            () -> assertThat(boardRepositoryImpl.findBoardByRoomIdAndPosition(1L, "a2")
                .getPiece())
                .isEqualTo("blank"),
            () -> assertThat(boardRepositoryImpl.findBoardByRoomIdAndPosition(1L, "a4")
                .getPiece())
                .isEqualTo("white_pawn")
        );
    }

    @DisplayName("a2 blank를 insert한다.")
    @Test
    void insert() {
        final BoardEntity boardEntity = new BoardEntity(1L, "a2", "blank");
        final BoardEntity insertBoard = boardRepositoryImpl.insert(boardEntity);

        assertAll(
            () -> assertThat(insertBoard.getRoomId()).isEqualTo(1L),
            () -> assertThat(insertBoard.getPosition()).isEqualTo("a2"),
            () -> assertThat(insertBoard.getPiece()).isEqualTo("blank")
        );
    }

    private void insertInitialData() {
        final Map<Position, Piece> boards = BoardFactory.initialize();
        final List<BoardEntity> boardEntities = boards.entrySet().stream()
            .map(entry -> new BoardEntity(1L, entry.getKey().convertPositionToString(),
                entry.getValue().convertPieceToString()))
            .collect(Collectors.toList());
        boardRepositoryImpl.batchInsert(boardEntities);
    }

}
