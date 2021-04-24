package chess.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.dto.*;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chess.domain.board.Position.convertStringToPosition;
import static chess.domain.piece.Team.*;

@Service
public class ChessService {
    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public BoardDto resetGame(String roomName) {
        Board initiatedBoard = BoardFactory.create();
        chessDao.resetTurnOwner(roomName);
        resetBoard(initiatedBoard, roomName);
        return BoardDto.of(initiatedBoard);
    }

    private void resetBoard(Board board, String roomName) {
        for (Map.Entry<Position, Piece> entry : board.getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            chessDao.resetEachBoardPosition(unicode, position.convertToString(), roomName);
        }
    }

    public BoardDto getSavedBoard(String roomName) {
        List<Map<String, Object>> resultList = chessDao.getSavedBoard(roomName);
        Map<String, String> boardInfo = new HashMap<>();
        for (Map<String, Object> result : resultList) {
            String position = (String) result.get("position");
            String piece = (String) result.get("piece");
            boardInfo.put(position, piece);
        }
        return BoardDto.of(boardInfo);
    }

    public ScoreDto score(String roomName) {
        Board board = convertBoardDtoToBoard(getSavedBoard(roomName));
        double whiteScore = board.calculateScore(WHITE);
        double blackScore = board.calculateScore(BLACK);
        return ScoreDto.of(whiteScore, blackScore);
    }

    public BoardDto move(MoveInfoDto moveInfoDto, String roomName) {
        Board board = convertBoardDtoToBoard(getSavedBoard(roomName));
        Team turnOwnerAfterMove = chessGameMove(board, moveInfoDto.getTarget(), moveInfoDto.getDestination(), roomName);
        Piece targetPiece = getTargetPiece(moveInfoDto, board);

        chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece, roomName);
        chessDao.renewTurnOwnerAfterMove(turnOwnerAfterMove, roomName);
        return BoardDto.of(board);
    }

    public RoomListDto getRoomList() {
        return chessDao.getRoomList();
    }

    public void addGame(String roomName) {
        chessDao.initializeGameTable(roomName);
        initializeGameBoard(roomName);
    }

    private void initializeGameBoard(String roomName) {
        for (Map.Entry<Position, Piece> entry : BoardFactory.create().getBoard().entrySet()) {
            Position position = entry.getKey();
            Piece piece = entry.getValue();
            String unicode = piece != null ? piece.getUnicode() : "";
            chessDao.initializeEachBoardPosition(unicode, position.convertToString(), roomName);
        }
    }

    private Piece getTargetPiece(MoveInfoDto moveInfoDto, Board board) {
        Position target = convertStringToPosition(moveInfoDto.getDestination());
        return board.getBoard().get(target);
    }

    private Team getSavedTurnOwner(String roomName) {
        TurnDto savedTurnOwner = chessDao.getSavedTurnOwner(roomName);
        return convertStringToTeam(savedTurnOwner.getTurn());
    }

    public Team chessGameMove(Board board, String target, String destination, String roomName) {
        return board.movePiece(convertStringToPosition(target),
                convertStringToPosition(destination), getSavedTurnOwner(roomName));
    }

    private Board convertBoardDtoToBoard(BoardDto boardDto) {
        return boardDto.toBoard();
    }
}
