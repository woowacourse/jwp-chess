package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.TurnDto;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChessService {
    private final ChessDao chessDao;
    private final ChessGame chessGame;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
        this.chessGame = new ChessGame();
    }

    public BoardDto resetBoard() {
        Board initiatedBoard = BoardFactory.create();
        chessDao.resetTurnOwner();
        chessDao.resetBoard(initiatedBoard);
        return BoardDto.of(initiatedBoard);
    }

    public BoardDto getSavedBoardInfo() {
        BoardDto boardDto = chessDao.getSavedBoardInfo();
        TurnDto turnDto = chessDao.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDto.getBoardInfo(), turnDto.getTurn());
        return boardDto;
    }

    public String score() {
        return chessGame.scoreStatus();
    }

    public BoardDto move(MoveInfoDto moveInfoDto) {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDto.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDto.getTarget(), moveInfoDto.getDestination());

        chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece);
        chessDao.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDto.of(board);
    }
    ////

//    public void move(String target, String destination) {
//        boardInitializeCheck();
//        turnOwner = board.movePiece(convertStringToPosition(target),
//                convertStringToPosition(destination), turnOwner);
//    }
//
//    public String scoreStatus() {
//        boardInitializeCheck();
//        double whiteScore = board.calculateScore(Team.WHITE);
//        double blackScore = board.calculateScore(Team.BLACK);
//        return "백 : " + whiteScore + "  흑 : " + blackScore;
//    }
//
//    private Position convertStringToPosition(String input) {
//        return Position.convertStringToPosition(input);
//    }
//
//    private void boardInitializeCheck() {
//        if (board == null) {
//            throw new IllegalArgumentException("보드가 세팅되지 않았습니다. start 명령어를 입력해주세요.");
//        }
//    }
//
//    public void loadSavedBoardInfo(Map<String, String> boardInfo, String turnOwner) {
//        board = BoardFactory.loadSavedBoardInfo(boardInfo);
//        this.turnOwner = Team.convertStringToTeam(turnOwner);
//    }
}
