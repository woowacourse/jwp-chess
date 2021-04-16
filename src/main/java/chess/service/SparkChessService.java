package chess.service;

import chess.domain.ChessGame;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.TurnDto;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.repository.SparkChessDao;

import java.sql.SQLException;

public class SparkChessService {
    private final SparkChessDao sparkChessDAO;

    public SparkChessService() {
        sparkChessDAO = new SparkChessDao();
    }

    public BoardDto initiateBoard(ChessGame chessGame) throws SQLException {
        sparkChessDAO.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        sparkChessDAO.resetBoard(chessGame.getBoard());
        return BoardDto.of(chessGame.getBoard());
    }

    public BoardDto getSavedBoardInfo(ChessGame chessGame) throws SQLException {
        BoardDto boardDTO = sparkChessDAO.getSavedBoardInfo();
        TurnDto turnDTO = sparkChessDAO.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDTO.getBoardInfo(), turnDTO.getTurn());
        return boardDTO;
    }

    public BoardDto move(ChessGame chessGame, MoveInfoDto moveInfoDTO) throws SQLException {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDTO.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDTO.getTarget(), moveInfoDTO.getDestination());

        sparkChessDAO.renewBoardAfterMove(moveInfoDTO.getTarget(), moveInfoDTO.getDestination(), targetPiece);
        sparkChessDAO.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDto.of(board);
    }
}
