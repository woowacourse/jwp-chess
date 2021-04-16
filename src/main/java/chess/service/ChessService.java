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
        BoardDto boardDTO = chessDao.getSavedBoardInfo();
        TurnDto turnDTO = chessDao.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDTO.getBoardInfo(), turnDTO.getTurn());
        return boardDTO;
    }

    public BoardDto move(ChessGame chessGame, MoveInfoDto moveInfoDTO) {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDTO.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDTO.getTarget(), moveInfoDTO.getDestination());

        chessDao.renewBoardAfterMove(moveInfoDTO.getTarget(), moveInfoDTO.getDestination(), targetPiece);
        chessDao.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDto.of(board);
    }
}
