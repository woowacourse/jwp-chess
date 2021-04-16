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
    private final SpringChessDao springChessDAO;

    public SpringChessService(SpringChessDao springChessDAO) {
        this.springChessDAO = springChessDAO;
    }

    public BoardDto initiateBoard(ChessGame chessGame) {
        springChessDAO.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        springChessDAO.resetBoard(chessGame.getBoard());
        return BoardDto.of(chessGame.getBoard());
    }

    public BoardDto getSavedBoardInfo(ChessGame chessGame) {
        BoardDto boardDTO = springChessDAO.getSavedBoardInfo();
        TurnDto turnDTO = springChessDAO.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDTO.getBoardInfo(), turnDTO.getTurn());
        return boardDTO;
    }

    public BoardDto move(ChessGame chessGame, MoveInfoDto moveInfoDTO) {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDTO.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDTO.getTarget(), moveInfoDTO.getDestination());

        springChessDAO.renewBoardAfterMove(moveInfoDTO.getTarget(), moveInfoDTO.getDestination(), targetPiece);
        springChessDAO.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDto.of(board);
    }
}
