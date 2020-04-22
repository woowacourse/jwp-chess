package chess.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.dao.BoardDAO;
import chess.domain.dao.FinishDAO;
import chess.domain.dao.TurnDAO;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.domain.position.Position;
import chess.domain.result.GameResult;
import chess.exception.TakeTurnException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChessGameService {
    private BoardDAO boardDAO;
    private FinishDAO finishDAO;
    private TurnDAO turnDAO;

    public ChessGameService() throws SQLException {
        this.boardDAO = new BoardDAO();
        this.finishDAO = new FinishDAO();
        this.turnDAO = new TurnDAO();
        if (getCurrentTurn() == Team.BLANK) {
            turnDAO.updateTurn(Team.WHITE);
        }
    }

    public Map<String, Object> receiveEmptyBoard() throws SQLException {
        boardDAO.deletePieces();
        return new HashMap<>();
    }

    public Map<String, Object> receiveInitializedBoard() throws SQLException {
        Board board = BoardFactory.createBoard();
        board.initialize();

        boardDAO.deletePieces();
        for (Position position : board.getBoard().keySet()) {
            boardDAO.placePiece(board, position);
        }
        return createBoardModel(board);
    }

    public Map<String, Object> receiveLoadedBoard() throws SQLException {
        if (boardDAO.findAllPieces().isEmpty()) {
            return receiveEmptyBoard();
        }
        Board board = new Board(boardDAO.findAllPieces());
        return createBoardModel(board);
    }

    public void receiveMovedBoard(final String fromPiece, final String toPiece) throws SQLException {
        Board board = new Board(boardDAO.findAllPieces());
        Piece piece = board.findBy(Position.of(fromPiece));

        if (!piece.isSameTeam(getCurrentTurn())) {
            throw new TakeTurnException("체스 게임 순서를 지켜주세요.");
        }
        board.move(fromPiece, toPiece);
        updateFinish(board.isFinished());
        updateTurn();

        for (Position position : board.getBoard().keySet()) {
            boardDAO.placePiece(board, position);
        }
    }

    private Map<String, Object> createBoardModel(final Board board) {
        Map<String, Object> model = new HashMap<>();
        Map<Position, Piece> rawBoard = board.getBoard();
        for (Position position : rawBoard.keySet()) {
            model.put(position.toString(), rawBoard.get(position).getName());
        }
        return model;
    }

    public Map<String, Object> receiveScoreStatus() throws SQLException {
        GameResult gameResult = new GameResult();
        Board board = new Board(boardDAO.findAllPieces());

        double blackScore = gameResult.calculateScore(board, Team.BLACK);
        double whiteScore = gameResult.calculateScore(board, Team.WHITE);

        Map<String, Object> model = createBoardModel(board);
        model.put("black", blackScore);
        model.put("white", whiteScore);
        return model;
    }

    public void initializeTurn() throws SQLException {
        turnDAO.deleteTurn();
        turnDAO.insertTurn(Team.WHITE);
    }

    public void updateTurn() throws SQLException {
        if (turnDAO.findTurn() == Team.WHITE) {
            turnDAO.updateTurn(Team.BLACK);
        } else {
            turnDAO.updateTurn(Team.WHITE);
        }
    }

    public Team getCurrentTurn() throws SQLException {
        return turnDAO.findTurn();
    }

    public String receiveWinner() throws SQLException {
        updateTurn();
        Team team = turnDAO.findTurn();
        return team.toString();
    }

    public void initializeFinish() throws SQLException {
        finishDAO.deleteFinish();
        finishDAO.insertFinish(false);
    }

    public void updateFinish(final boolean isFinish) throws SQLException {
        finishDAO.updateFinish(isFinish);
    }

    public boolean isFinish() throws SQLException {
        return finishDAO.getIsFinish().equals("true");
    }
}
