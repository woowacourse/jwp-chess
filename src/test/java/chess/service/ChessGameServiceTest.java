package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.MockBoardDao;
import chess.dao.MockPieceDao;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.dto.ChessBoardDto;
import chess.dto.ResponseDto;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {

    private ChessGameService chessGameService;
    private final String title = "abc";
    private final String password = "123";

    @BeforeEach
    void init(){
        chessGameService = new ChessGameService(new MockPieceDao(), new MockBoardDao());
    }

    @Test
    @DisplayName("입력된 이름으로 저장된 체스판이 없으면 생성한다.")
    void create_board() {
        //when
        chessGameService.create(title, password);
        //then
        final ChessBoardDto chessBoardDto = chessGameService.getBoard(1L);
        final Map<String, Piece> board = chessBoardDto.getBoard();
        assertThat(board.size()).isEqualTo(64);
    }

    @Test
    @DisplayName("중복된 이름의 방 제목을 생성하면 statusCode 401을 반환한다.")
    void create_duplicate_title_board() {
        //given
        chessGameService.create(title, password);
        //when
        final ResponseDto responseDto = chessGameService.create(title, password);
        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(401);
    }

    @Test
    @DisplayName("move 명령을 했을 때 원하는 위치로 기물이 움직였는지 확인")
    void move_piece() {
        //given
        chessGameService.create(title, password);
        //when
        chessGameService.move(1L, "a2", "a4");
        //then
        final ChessBoardDto board = chessGameService.getBoard(1L);
        assertThat(board.getBoard().get("a4")).isInstanceOf(Pawn.class);
    }
//    @Test
//    @DisplayName("게임 시작 시 저장된 체스가 있으면 불러온다.")
//    void initial_start() {
//        //given
//        Board board = new Board();
//        PieceDao pieceDao = new MockPieceDao();
//        BoardDao boardDao = new MockBoardDao();
//        board.move(Position.of(Column.A, Row.TWO), Position.of(Column.A, Row.FOUR));
//        pieceDao.save(board.getPiecesByPosition());
//        boardDao.save(Color.BLACK);
//        ChessGameService chessGameService = new ChessGameService(pieceDao, boardDao);
//        //when
//        chessGameService.start();
//        //then
//        ChessBoardDto chessBoardDto = chessGameService.getBoard();
//        Map<String, Piece> pieceMap = chessBoardDto.getBoard();
//        assertThat(pieceMap.get("a4")).isInstanceOf(Pawn.class);
//    }

//    @Test
//    @DisplayName("게임을 종료하면 저장된 게임이 삭제된다.")
//    void delete() {
//        //given
//        Board board = new Board();
//        PieceDao pieceDao = new MockPieceDao();
//        BoardDao boardDao = new MockBoardDao();
//        pieceDao.save(board.getPiecesByPosition());
//        boardDao.save(Color.WHITE);
//        ChessGameService chessGameService = new ChessGameService(pieceDao, boardDao);
//        chessGameService.start();
//        //when
//        chessGameService.end();
//        //then
//        assertThat(pieceDao.existPieces()).isFalse();
//    }
}
