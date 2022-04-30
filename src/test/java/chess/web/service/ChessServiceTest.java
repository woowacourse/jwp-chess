package chess.web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.board.Board;
import chess.board.Room;
import chess.board.Team;
import chess.board.Turn;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import chess.board.piece.position.Position;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessServiceTest {

    private final PieceDao pieceDao = new MockPieceDao();
    private final RoomDao roomDao = new MockRoomDao();
    private final ChessService chessService = new ChessService(pieceDao, roomDao);

    @Test
    @DisplayName("64개의 piece를 갖고 있는 게임을 불러왔을 떄, 그떄의 piece 개수도 64개여야한다.")
    void loadGame() {
        Long roomId = chessService.createRoom("title", "password");
        chessService.initBoard(roomId);
        Board board = chessService.loadGame(roomId);
        assertThat(board.getPieces().getPieces().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("보드판에서 from에서 to로 이동하면 처음 from에 있던 piece는 이동 후, to에 있는 piece와 같다.")
    void move() {
        Long roomId = chessService.createRoom("title", "password");
        chessService.initBoard(roomId);
        Pieces pieces = Pieces.from(pieceDao.findAllByRoomId(roomId));
        Turn turn = new Turn(Team.from(roomDao.findById(roomId).get().getTurn()));
        Board board = Board.create(pieces, turn);
        String from = "a2";
        String to = "a3";
        Piece piece = board.getPieces().findByPosition(Position.from(from));
        MoveDto moveDto = new MoveDto(from, to);

        Board movedBoard = chessService.move(moveDto, roomId);

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
        Long roomId = chessService.createRoom("title", "password");
        ;

        Board initBoard = chessService.initBoard(roomId);

        Pieces pieces = initBoard.getPieces();
        assertThat(pieces.getPieces().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("초기 board들의 점수는 black, white팀 모두 38이다.")
    void getStatus() {
        Long roomId = chessService.createRoom("title", "password");
        chessService.initBoard(roomId);

        ScoreDto status = chessService.getStatus(roomId);

        assertThat(status.getBlackTeamScore()).isEqualTo(38D);
        assertThat(status.getWhiteTeamScore()).isEqualTo(38D);
    }

    @Test
    @DisplayName("입력한 방이름과 비밀번호로 새로운 체스방이 만들어진다.")
    void createRoom() {
        String title = "title";
        String password = "password";

        Long roomId = chessService.createRoom(title, password);

        Optional<Room> room = roomDao.findById(roomId);
        assertThat(room).isPresent();
        assertThat(room.get().getTitle()).isEqualTo(title);
        assertThat(room.get().getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("생성된 모든 체스 방의 정보를 가져온다.")
    void getRooms() {
        String title1 = "title1";
        String title2 = "title2";
        String password1 = "password1";
        String password2 = "password2";
        chessService.createRoom(title1, password1);
        chessService.createRoom(title2, password2);

        List<Room> rooms = chessService.getRooms();

        assertThat(rooms.size()).isEqualTo(2);
        assertThat(rooms.get(0).getTitle()).isEqualTo(title1);
        assertThat(rooms.get(1).getTitle()).isEqualTo(title2);
    }

    @Test
    @DisplayName("생성된 체스방과 체스게임의 상태를 삭제한다.")
    void removeRoom() {
        String title = "title";
        String password = "password";
        Long roomId = chessService.createRoom(title, password);
        chessService.move(new MoveDto("e2", "e3"), roomId);
        chessService.move(new MoveDto("f7", "f6"), roomId);
        chessService.move(new MoveDto("d1", "h5"), roomId);
        chessService.move(new MoveDto("a7", "a6"), roomId);
        chessService.move(new MoveDto("h5", "e8"), roomId);

        boolean result = chessService.removeRoom(roomId, password);

        long resultCount = chessService.getRooms().stream()
                .filter(room -> room.getId().equals(roomId))
                .filter(room -> room.getTitle().equals(title))
                .filter(room -> room.getPassword().equals(password))
                .count();
        assertThat(result).isTrue();
        assertThat(resultCount).isZero();
    }

    @Test
    @DisplayName("게임이 진행 중이라면 생성된 체스방을 삭제할 수 없다.")
    void removeRoomFailInRunningGame() {
        String title = "title";
        String password = "password";
        Long roomId = chessService.createRoom(title, password);
        chessService.initBoard(roomId);

        boolean result = chessService.removeRoom(roomId, password);

        long resultCount = chessService.getRooms().stream()
                .filter(room -> room.getId().equals(roomId))
                .filter(room -> room.getTitle().equals(title))
                .filter(room -> room.getPassword().equals(password))
                .count();
        assertThat(result).isFalse();
        assertThat(resultCount).isOne();
    }
}
