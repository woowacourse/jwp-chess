package chess.service;

import chess.domain.ChessGame;
import chess.domain.DTO.BoardDTO;
import chess.domain.DTO.MoveInfoDTO;
import chess.domain.DTO.TurnDTO;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.repository.SparkChessDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

public class SparkChessService {
    private final SparkChessDAO sparkChessDAO;

    public SparkChessService() {
        sparkChessDAO = new SparkChessDAO();
    }

    public BoardDTO initiateBoard(ChessGame chessGame) throws SQLException {
        sparkChessDAO.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        sparkChessDAO.resetBoard(chessGame.getBoard());
        return BoardDTO.of(chessGame.getBoard());
    }

    public BoardDTO getSavedBoardInfo(ChessGame chessGame) throws SQLException {
        BoardDTO boardDTO = sparkChessDAO.getSavedBoardInfo();
        TurnDTO turnDTO = sparkChessDAO.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDTO.getBoardInfo(), turnDTO.getTurn());
        return boardDTO;
    }

    public BoardDTO move(ChessGame chessGame, MoveInfoDTO moveInfoDTO) throws SQLException {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDTO.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDTO.getTarget(), moveInfoDTO.getDestination());

        sparkChessDAO.renewBoardAfterMove(moveInfoDTO.getTarget(), moveInfoDTO.getDestination(), targetPiece);
        sparkChessDAO.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDTO.of(board);
    }
}
