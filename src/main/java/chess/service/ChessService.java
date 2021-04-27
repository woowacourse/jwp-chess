package chess.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.dto.*;
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

    public BoardDto initializeByName(String roomName) {
        return chessDao.initializeByName(roomName);
    }

    public BoardDto resetBoard(int roomId) {
        Board resetBoard = BoardFactory.create();
        chessDao.resetTurnOwner(roomId);
        chessDao.resetBoard(resetBoard, roomId);
        return BoardDto.of(resetBoard);
    }

    public BoardDto getSavedBoardInfo(int roomId) {
        return chessDao.getSavedBoardInfo(roomId);
    }

    public BoardDto move(MoveInfoDto moveInfoDto, int roomId) {
        Board board = getSavedBoardInfo(roomId).toBoard();
        Position target = Position.from(moveInfoDto.getTarget());
        Piece targetPiece = board.getBoard().get(target);
        Team turn = chessSingleMove(board, moveInfoDto, roomId);

        chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece, roomId);
        chessDao.renewTurnOwnerAfterMove(turn, roomId);
        return BoardDto.of(board);
    }

    private Team chessSingleMove(Board board, MoveInfoDto moveInfoDto, int roomId) {
        String target = moveInfoDto.getTarget();
        String destination = moveInfoDto.getDestination();
        return board.movePiece(Position.from(target), Position.from(destination), findTurnAfterMove(roomId));
    }

    private Team findTurnAfterMove(int roomId) {
        TurnDto previousTurn = chessDao.getSavedTurnOwner(roomId);
        return Team.convertStringToTeam(previousTurn.getTurn());
    }

    public RoomsDto getRoomList() {
        return chessDao.getRoomList();
    }

    public ScoreDto score(int roomId) {
        Board board = getSavedBoardInfo(roomId).toBoard();

        double whiteScore = board.calculateScore(Team.WHITE);
        double blackScore = board.calculateScore(Team.BLACK);
        return ScoreDto.of(whiteScore, blackScore);
    }
}
