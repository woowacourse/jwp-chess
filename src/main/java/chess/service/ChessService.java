package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.dto.*;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

//    public BoardDto initiateBoard(ChessGame chessGame, int roomNumber) {
//        chessDao.resetTurnOwner(chessGame.getTurnOwner());
//        chessGame.settingBoard();
//        chessDao.resetBoard(chessGame.getBoard(), roomNumber);
//        return BoardDto.of(chessGame.getBoard());
//    }

    public BoardDto initializeByName(String roomName) {
        return chessDao.initializeByName(roomName);
    }

    public BoardDto resetBoard(int roomNumber) {
        Board resetBoard = BoardFactory.create();
        chessDao.resetTurnOwner(roomNumber);
        chessDao.resetBoard(resetBoard, roomNumber);
        return BoardDto.of(resetBoard);
    }

    public BoardDto getSavedBoardInfo(int roomNumber) {
//        BoardDto boardDto = chessDao.getSavedBoardInfo(roomNumber);
//        TurnDto turnDto = chessDao.getSavedTurnOwner(roomNumber);
//
//        chessGame.loadSavedBoardInfo(boardDto.getBoardInfo(), turnDto.getTurn());
//        return boardDto;
        return chessDao.getSavedBoardInfo(roomNumber);
    }

    public ResponseDto move(MoveInfoDto moveInfoDto, int roomNumber) {
        try {
            Board board = getSavedBoardInfo(roomNumber).toBoard();
            Position target = Position.from(moveInfoDto.getTarget());
            Piece targetPiece = board.getBoard().get(target);
            Team turn = chessSingleMove(board, moveInfoDto, roomNumber);

//            chessGame.move(moveInfoDto.getTarget(), moveInfoDto.getDestination());
            chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece, roomNumber);
            chessDao.renewTurnOwnerAfterMove(turn, roomNumber);
            return new ResponseDto("200", "성공");
        } catch (Exception e) {
            return new ResponseDto("400", e.getMessage());
        }
    }

    private Team chessSingleMove(Board board, MoveInfoDto moveInfoDto, int roomNumber) {
        String target = moveInfoDto.getTarget();
        String destination = moveInfoDto.getDestination();
        return board.movePiece(Position.from(target), Position.from(destination), findTurnAfterMove(roomNumber));
    }

    private Team findTurnAfterMove(int roomNumber) {
        TurnDto previousTurn = chessDao.getSavedTurnOwner(roomNumber);
        return Team.convertStringToTeam(previousTurn.getTurn());
    }

    public BoardDto getCurrentBoard(int roomNumber) {
//        Board board = chessGame.getBoard();
//        return BoardDto.of(board);
        return chessDao.getSavedBoardInfo(roomNumber);
    }

    public RoomsDto getRoomList() {
        return chessDao.getRoomList();
    }

    public ScoreDto score(int roomNumber) {
        Board board = getSavedBoardInfo(roomNumber).toBoard();
        board.getBoard().forEach((key, value) -> {
            String unicode = value != null ? value.getUnicode() : "";
            System.out.println(key.convertToString() + "    " + unicode);
        });

        double whiteScore = board.calculateScore(Team.WHITE);
        double blackScore = board.calculateScore(Team.BLACK);
        return ScoreDto.of(whiteScore, blackScore);
    }
}
