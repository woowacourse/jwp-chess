package chess.service;

import chess.domain.ChessGame;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.TurnDto;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.repository.SpringChessDao;
import org.springframework.stereotype.Service;

@Service
public class SpringChessService {
    private final SpringChessDao springChessDao;

    public SpringChessService(SpringChessDao springChessDao) {
        this.springChessDao = springChessDao;
    }

    public BoardDto initiateBoard(ChessGame chessGame) {
        springChessDao.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        springChessDao.resetBoard(chessGame.getBoard());
        return BoardDto.of(chessGame.getBoard());
    }

    public BoardDto getSavedBoardInfo(ChessGame chessGame) {
        BoardDto boardDto = springChessDao.getSavedBoardInfo();
        TurnDto turnDto = springChessDao.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDto.getBoardInfo(), turnDto.getTurn());
        return boardDto;
    }

    public BoardDto move(ChessGame chessGame, MoveInfoDto moveInfoDto) {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDto.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDto.getTarget(), moveInfoDto.getDestination());

        springChessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece);
        springChessDao.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDto.of(board);
    }
}
