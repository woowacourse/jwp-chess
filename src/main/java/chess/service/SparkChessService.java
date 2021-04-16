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
    private final SparkChessDao sparkChessDao;

    public SparkChessService() {
        sparkChessDao = new SparkChessDao();
    }

    public BoardDto initiateBoard(ChessGame chessGame) throws SQLException {
        sparkChessDao.resetTurnOwner(chessGame.getTurnOwner());
        chessGame.settingBoard();
        sparkChessDao.resetBoard(chessGame.getBoard());
        return BoardDto.of(chessGame.getBoard());
    }

    public BoardDto getSavedBoardInfo(ChessGame chessGame) throws SQLException {
        BoardDto boardDto = sparkChessDao.getSavedBoardInfo();
        TurnDto turnDto = sparkChessDao.getSavedTurnOwner();

        chessGame.loadSavedBoardInfo(boardDto.getBoardInfo(), turnDto.getTurn());
        return boardDto;
    }

    public BoardDto move(ChessGame chessGame, MoveInfoDto moveInfoDto) throws SQLException {
        Board board = chessGame.getBoard();
        Position target = Position.convertStringToPosition(moveInfoDto.getTarget());

        Piece targetPiece = board.getBoard().get(target);

        chessGame.move(moveInfoDto.getTarget(), moveInfoDto.getDestination());

        sparkChessDao.renewBoardAfterMove(moveInfoDto.getTarget(), moveInfoDto.getDestination(), targetPiece);
        sparkChessDao.renewTurnOwnerAfterMove(chessGame.getTurnOwner());
        return BoardDto.of(board);
    }
}
