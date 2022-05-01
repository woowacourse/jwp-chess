package chess.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.board.Board;
import chess.board.BoardEntity;
import chess.board.Team;
import chess.board.Turn;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import chess.board.piece.position.Position;
import chess.web.dao.BoardDao;
import chess.web.dao.PieceDao;
import chess.web.service.dto.BoardDto;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessServiceTest {

    private final PieceDao pieceDao = new MockPieceDao();
    private final BoardDao boardDao = new MockBoardDao();
    private final ChessService chessService = new ChessService(pieceDao, boardDao);

    @Test
    @DisplayName("64개의 piece를 갖고 있는 게임을 불러왔을 떄, 그떄의 piece 개수도 64개여야한다.")
    void loadGame() {
        Long boardId = chessService.createBoard("title", "password");
        chessService.initBoard(boardId);

        Board board = chessService.loadGame(boardId);

        assertThat(board.getPieces().getPieces().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("보드판에서 from에서 to로 이동하면 처음 from에 있던 piece는 이동 후, to에 있는 piece와 같다.")
    void move() {
        Long boardId = chessService.createBoard("title", "password");
        chessService.initBoard(boardId);
        Pieces pieces = Pieces.from(pieceDao.findAllByBoardId(boardId));
        Turn turn = new Turn(Team.from(boardDao.findById(boardId).get().getTurn()));
        Board board = Board.create(pieces, turn);
        String from = "a2";
        String to = "a3";
        Piece piece = board.getPieces().findByPosition(Position.from(from));
        MoveDto moveDto = new MoveDto(from, to);

        Board movedBoard = chessService.move(moveDto, boardId);

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
        Long boardId = chessService.createBoard("title", "password");

        Board initBoard = chessService.initBoard(boardId);

        Pieces pieces = initBoard.getPieces();
        assertThat(pieces.getPieces().size()).isEqualTo(64);
        assertThat(pieces.getPieces().containsAll(Pieces.createInit().getPieces())).isTrue();
    }

    @Test
    @DisplayName("초기 board들의 점수는 black, white팀 모두 38이다.")
    void getStatus() {
        Long boardId = chessService.createBoard("title", "password");
        chessService.initBoard(boardId);

        ScoreDto status = chessService.getStatus(boardId);

        assertThat(status.getBlackTeamScore()).isEqualTo(38D);
        assertThat(status.getWhiteTeamScore()).isEqualTo(38D);
    }

    @Test
    @DisplayName("입력한 방이름과 비밀번호로 새로운 체스방이 만들어진다.")
    void createBoard() {
        String title = "title";
        String password = "password";

        Long boardId = chessService.createBoard(title, password);

        Optional<BoardEntity> board = boardDao.findById(boardId);
        assertThat(board).isPresent();
        assertThat(board.get().getTitle()).isEqualTo(title);
        assertThat(board.get().getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("생성된 모든 체스 방의 정보를 가져온다.")
    void getBoards() {
        String title1 = "title1";
        String title2 = "title2";
        String password1 = "password1";
        String password2 = "password2";
        chessService.createBoard(title1, password1);
        chessService.createBoard(title2, password2);

        List<BoardDto> boardDtos = chessService.getBoards();

        assertThat(boardDtos.size()).isEqualTo(2);
        assertThat(boardDtos.get(0).getTitle()).isEqualTo(title1);
        assertThat(boardDtos.get(1).getTitle()).isEqualTo(title2);
    }

    @Test
    @DisplayName("생성된 체스방과 체스게임의 상태를 삭제한다.")
    void removeBoard() {
        String title = "title";
        String password = "password";
        Long boardId = chessService.createBoard(title, password);
        chessService.move(new MoveDto("e2", "e3"), boardId);
        chessService.move(new MoveDto("f7", "f6"), boardId);
        chessService.move(new MoveDto("d1", "h5"), boardId);
        chessService.move(new MoveDto("a7", "a6"), boardId);
        chessService.move(new MoveDto("h5", "e8"), boardId);

        boolean result = chessService.removeBoard(boardId, password);

        long resultCount = chessService.getBoards().stream()
                .filter(board -> board.getId().equals(boardId))
                .count();
        assertThat(result).isTrue();
        assertThat(resultCount).isZero();
    }

    @Test
    @DisplayName("게임이 진행 중이라면 생성된 체스방을 삭제할 수 없다.")
    void removeRoomFailInRunningGame() {
        String title = "title";
        String password = "password";
        Long boardId = chessService.createBoard(title, password);
        chessService.initBoard(boardId);

        assertThatThrownBy(() -> chessService.removeBoard(boardId, password))
                .isInstanceOf(IllegalStateException.class);
    }
}
