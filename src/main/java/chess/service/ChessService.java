package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.dto.*;
import chess.domain.piece.Piece;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public BoardDto initiateBoard(ChessGame chessGame, int roomNumber) {
        chessDao.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        chessDao.resetBoard(chessGame.getBoard(), roomNumber);
        return BoardDto.of(chessGame.getBoard());
    }

    public BoardDto getSavedBoardInfo(ChessGame chessGame, int roomNumber) {
        BoardDto boardDto = chessDao.getSavedBoardInfo(roomNumber);
        TurnDto turnDto = chessDao.getSavedTurnOwner(roomNumber);

        chessGame.loadSavedBoardInfo(boardDto.getBoardInfo(), turnDto.getTurn());
        return boardDto;
    }

    public ResponseDto move(ChessGame chessGame, MoveInfoDto moveInfoDto, int roomNumber) {
        try {
            Board board = chessGame.getBoard();
            Position target = Position.convertStringToPosition(moveInfoDto.getTarget());

            Piece targetPiece = board.getBoard().get(target);

            chessGame.move(moveInfoDto.getTarget(), moveInfoDto.getDestination());
            chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece, roomNumber);
            chessDao.renewTurnOwnerAfterMove(chessGame.getTurnOwner(), roomNumber);
            return new ResponseDto("200", "성공");
        } catch (Exception e) {
            return new ResponseDto("400", e.getMessage());
        }
    }

    public BoardDto getCurrentBoard(ChessGame chessGame, int roomNumber) {
        Board board = chessGame.getBoard();
        return BoardDto.of(board);
    }

    public RoomsDto getRoomList() {
        return chessDao.getRoomList();
    }
}
