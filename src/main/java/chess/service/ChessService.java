package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.ResponseDto;
import chess.domain.dto.TurnDto;
import chess.domain.piece.Piece;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final ChessDao chessDao;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public BoardDto initiateBoard(ChessGame chessGame) {
        chessDao.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        chessDao.resetBoard(chessGame.getBoard());
        return BoardDto.of(chessGame.getBoard());
    }

    public BoardDto getSavedBoardInfo(ChessGame chessGame) {
        BoardDto boardDto = chessDao.getSavedBoardInfo();
        TurnDto turnDto = chessDao.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDto.getBoardInfo(), turnDto.getTurn());
        return boardDto;
    }

    public ResponseDto move(ChessGame chessGame, MoveInfoDto moveInfoDto) {
        try {
            Board board = chessGame.getBoard();
            Position target = Position.convertStringToPosition(moveInfoDto.getTarget());

            Piece targetPiece = board.getBoard().get(target);

            chessGame.move(moveInfoDto.getTarget(), moveInfoDto.getDestination());
            chessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece);
            chessDao.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
            return new ResponseDto("200", "성공");
        } catch (Exception e) {
            return new ResponseDto("400", e.getMessage());
        }
    }

    public BoardDto getCurrentBoard(ChessGame chessGame) {
        Board board = chessGame.getBoard();
        return BoardDto.of(board);
    }
}
