package chess.service;

import chess.dao.BoardDAO;
import chess.dao.TurnDAO;
import chess.domain.GameResult;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.board.PositionFactory;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDTO;
import chess.dto.Cell;
import chess.dto.TurnDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChessGameService {
    private static final char MIN_Y_POINT = '1';
    private static final char MAX_Y_POINT = '8';
    private static final char MIN_X_POINT = 'a';
    private static final char MAX_X_POINT = 'h';

    private BoardDAO boardDAO = BoardDAO.getInstance();
    private TurnDAO turnDAO = TurnDAO.getInstance();
    private Board board;
    private GameResult gameResult;

    public ChessGameService() throws SQLException {
        BoardDTO boardDto = boardDAO.getBoard();
        TurnDTO turnDto = turnDAO.findTurn();
        if (boardDto == null || turnDto == null) {
            this.board = new Board();
            turnDAO.saveTurn(TurnDTO.from(this.board));
        } else {
            this.board = new Board(boardDto.createBoard(), turnDto.createTeam());
        }

        this.gameResult = this.board.createGameResult();
    }

    public void setNewChessGame() {
        this.board = new Board();
        this.gameResult = this.board.createGameResult();
    }

    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();

        Map<Position, Piece> boardData = this.board.get();

        for (char yPoint = MAX_Y_POINT; yPoint >= MIN_Y_POINT; yPoint--) {
            for (char xPoint = MIN_X_POINT; xPoint <= MAX_X_POINT; xPoint++) {
                Position position = PositionFactory.of(xPoint, yPoint);
                Piece piece = boardData.get(position);

                cells.add(Cell.from(position, piece));
            }
        }

        return cells;
    }

    public String getCurrentTeam() {
        return this.board.getTeam().getName();
    }

    public Double getBlackPieceScore() {
        return this.gameResult.getAliveBlackPieceScoreSum();
    }

    public Double getWhitePieceScore() {
        return this.gameResult.getAliveWhitePieceScoreSum();
    }

    public void movePiece(String source, String target) {
        this.board.move(new MoveCommand(String.format("move %s %s", source, target)));
        this.gameResult = this.board.createGameResult();
    }

    public boolean isGameOver() {
        return this.board.isGameOver();
    }

    public String getWinner() {
        return gameResult.getWinner();
    }

    public String getLoser() {
        return gameResult.getLoser();
    }

    public void endGame() throws SQLException {
        this.boardDAO.deletePreviousBoard();
        this.turnDAO.deletePreviousTurn();
        setNewChessGame();
    }

    public void proceedGame() throws SQLException {
        this.boardDAO.deletePreviousBoard();
        this.boardDAO.saveBoard(BoardDTO.from(this.board));
        this.turnDAO.updateTurn(turnDAO.findTurn());
    }
}
