package wooteco.chess.service;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import chess.dao.BoardDAO;
import chess.dao.TurnDAO;
import chess.domain.board.ChessBoard;
import chess.domain.command.MoveCommand;
import chess.dto.BoardDTO;
import chess.dto.TurnDTO;

@Service
public class ChessGameService {
    private BoardDAO boardDAO = BoardDAO.getInstance();
    private TurnDAO turnDAO = TurnDAO.getInstance();

    public ChessBoard createNewChessGame() throws SQLException {
        ChessBoard chessBoard = new ChessBoard();
        turnDAO.deleteAll();
        boardDAO.saveBoard(BoardDTO.from(chessBoard));
        turnDAO.saveTurn(TurnDTO.from(chessBoard));
        return chessBoard;
    }

    public ChessBoard movePiece(String source, String target) throws SQLException {
        BoardDTO boardDTO = boardDAO.findBoard();
        TurnDTO turnDTO = turnDAO.findTurn();
        ChessBoard chessBoard = new ChessBoard(boardDTO.createBoard(), turnDTO.createTeam());
        chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

        this.boardDAO.deleteAll();
        this.boardDAO.saveBoard(BoardDTO.from(chessBoard));
        this.turnDAO.updateTurn(TurnDTO.from(chessBoard));

        return chessBoard;
    }

    public void endGame() throws SQLException {
        this.boardDAO.deleteAll();
        this.turnDAO.deleteAll();
        createNewChessGame();
    }

    public ChessBoard loadBoard() throws SQLException {
        BoardDTO boardDTO = boardDAO.findBoard();
        TurnDTO turnDTO = turnDAO.findTurn();
        return new ChessBoard(boardDTO.createBoard(), turnDTO.createTeam());
    }
}
