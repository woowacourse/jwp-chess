package chess.service;

import chess.domain.ChessGame;
import chess.domain.DTO.BoardDTO;
import chess.domain.DTO.MoveInfoDTO;
import chess.domain.DTO.TurnDTO;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.repository.SpringChessDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class SpringChessService {
    private final SpringChessDAO springChessDAO;

    public SpringChessService(SpringChessDAO springChessDAO) {
        this.springChessDAO = springChessDAO;
    }

    public BoardDTO initiateBoard(ChessGame chessGame) {
        springChessDAO.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        springChessDAO.resetBoard(chessGame.getBoard());
        return BoardDTO.of(chessGame.getBoard());
    }

    public BoardDTO getSavedBoardInfo(ChessGame chessGame) {
        BoardDTO boardDTO = springChessDAO.getSavedBoardInfo();
        TurnDTO turnDTO = springChessDAO.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDTO.getBoardInfo(), turnDTO.getTurn());
        return boardDTO;
    }

    public BoardDTO move(ChessGame chessGame, MoveInfoDTO moveInfoDTO) {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDTO.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDTO.getTarget(), moveInfoDTO.getDestination());

        springChessDAO.renewBoardAfterMove(moveInfoDTO.getTarget(), moveInfoDTO.getDestination(), targetPiece);
        springChessDAO.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDTO.of(board);
    }
}
