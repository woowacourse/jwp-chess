package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.TurnDto;
import chess.domain.piece.Piece;
import chess.repository.ChessDao;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final ChessDao chessDao;
    private final ChessGame chessGame;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
        this.chessGame = new ChessGame();
    }

    public BoardDto initiateBoard() {
        chessDao.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        chessDao.resetBoard(chessGame.getBoard());
        return BoardDto.of(chessGame.getBoard());
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
}
