package chess.web.service;

import chess.domain.board.Board;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Pieces;
import chess.domain.board.piece.position.Position;
import chess.domain.entity.Room;
import chess.web.controller.dto.MoveDto;
import chess.web.controller.dto.ScoreDto;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
class ChessServiceTest {

    private final RoomDao roomDao = new MockRoomDao();
    private final PieceDao pieceDao = new MockPieceDao();
    private final ChessService chessService = new ChessService(roomDao, pieceDao);
    private final Long ProceedingRoomId = 1L;
    private final Long finishedRoomId = 2L;

    @Test
    @DisplayName("64개의 piece를 갖고 있는 게임을 불러왔을 떄, 그떄의 piece 개수도 64개여야한다.")
    void loadGame() {
        Board board = chessService.loadGame(ProceedingRoomId);
        assertThat(board.getPieces().getPieces().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("초기 보드판에서 from에서 to로 이동하면 처음 from에 있던 piece는 이동 후, to에 있는 piece와 같다.")
    void move() {
        Pieces pieces = Pieces.from(pieceDao.findAllByBoardId(ProceedingRoomId));
        String from = "a2";
        String to = "a3";
        Piece piece = pieces.findByPosition(Position.from(from));
        MoveDto moveDto = new MoveDto(from, to);
        Board movedBoard = chessService.move(moveDto, ProceedingRoomId);
        Piece movedPiece = movedBoard.getPieces().findByPosition(Position.from(to));
        assertAll(
                () -> assertThat(piece.getType()).isEqualTo(movedPiece.getType()),
                () -> assertThat(piece.getName()).isEqualTo(movedPiece.getName()),
                () -> assertThat(piece.getTeam()).isEqualTo(movedPiece.getTeam())
        );

    }

    @Test
    @DisplayName("64개의 말들이 초기화된다.")
    void initBoard() {
        Long boardId = roomDao.save("방제목", "123");
        Board initBoard = chessService.initBoard(boardId);
        Pieces pieces = initBoard.getPieces();
        assertThat(pieces.getPieces().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("초기 board들의 점수는 black, white팀 모두 38이다.")
    void getStatus() {
        ScoreDto status = chessService.getStatus(ProceedingRoomId);
        assertThat(status.getBlackTeamScore()).isEqualTo(38D);
        assertThat(status.getWhiteTeamScore()).isEqualTo(38D);
    }

    @Test
    @DisplayName("체스판이 만들어진다.")
    public void createRoom() {
        Long id = chessService.createRoom("방 제목", "비밀번호");
        assertThat(id).isEqualTo(ProceedingRoomId);
    }

    @Test
    @DisplayName("체스 경기가 끝나지않았다면 방이 삭제되지 않는다.")
    public void deleteFailureWhenNotFinish() {

        assertThatThrownBy(
                () -> chessService.delete("비밀번호", ProceedingRoomId)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("2개의 방 중 한 방을 지우면 남은 방은 한 개가 된다.")
    public void delete() {
        chessService.delete("비밀번호", finishedRoomId);
        List<Room> roomList = chessService.getRoomList();
        assertThat(roomList.size()).isEqualTo(1);
    }
}
