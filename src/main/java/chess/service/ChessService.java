package chess.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.dto.*;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

import static chess.domain.board.Position.convertStringToPosition;
import static chess.domain.piece.Team.*;

@Service
public class ChessService {
    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public BoardDto resetBoard(String roomName) {
        Board initiatedBoard = BoardFactory.create();
        chessDao.resetTurnOwner();
        chessDao.resetBoard(initiatedBoard, roomName);
        return BoardDto.of(initiatedBoard);
    }

    public BoardDto getSavedBoard(String roomName) {
        return chessDao.getSavedBoard(roomName);
    }

    public ScoreDto score(String roomName) {
        Board board = convertBoardDtoToBoard(getSavedBoard(roomName));
        double whiteScore = board.calculateScore(WHITE);
        double blackScore = board.calculateScore(BLACK);
        return ScoreDto.of(whiteScore, blackScore);
    }

    public BoardDto move(MoveInfoDto moveInfoDto, String roomName) {
        Board board = convertBoardDtoToBoard(getSavedBoard(roomName));
        Piece targetPiece = getTargetPiece(moveInfoDto, board);
        Team turnOwnerAfterMove = chessGameMove(board, moveInfoDto.getTarget(), moveInfoDto.getDestination());

        chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece);
        chessDao.renewTurnOwnerAfterMove(turnOwnerAfterMove);
        return BoardDto.of(board);
    }

    public RoomListDto getRoomList() {
        return chessDao.getRoomList();
    }

    public void addRoom(String roomName) {
        chessDao.addRoom(roomName);
    }

    private Piece getTargetPiece(MoveInfoDto moveInfoDto, Board board) {
        Position target = convertStringToPosition(moveInfoDto.getTarget());
        return board.getBoard().get(target);
    }

    private Team getSavedTurnOwner() {
        TurnDto savedTurnOwner = chessDao.getSavedTurnOwner();
        return convertStringToTeam(savedTurnOwner.getTurn());
    }

    public Team chessGameMove(Board board, String target, String destination) {
        return board.movePiece(convertStringToPosition(target),
                convertStringToPosition(destination), getSavedTurnOwner());
    }

    private Board convertBoardDtoToBoard(BoardDto boardDto) {
        return BoardFactory.loadSavedBoardInfo(boardDto.getBoardInfo());
    }
}
