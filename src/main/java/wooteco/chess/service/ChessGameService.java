package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.BoardDAO;
import wooteco.chess.dao.TurnDAO;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.GameResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChessGameService {
    private BoardDAO boardDAO;
    private TurnDAO turnDAO;

    public ChessGameService() throws SQLException {
        this.boardDAO = new BoardDAO();
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
        Board board = BoardFactory.initializeBoard();
        boardDAO.deletePieces();
        for (Position position : board.getBoard().keySet()) {
            boardDAO.insertPiece(board, position);
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

    public Board receiveMovedBoard(final String fromPosition, final String toPosition) throws SQLException {
        Board board = new Board(boardDAO.findAllPieces());
        Piece piece = board.findBy(Position.of(fromPosition));

        if (piece.isNotSameTeam(getCurrentTurn())) {
            throw new IllegalArgumentException("체스 게임 순서를 지켜주세요.");
        }

        if(board.isMovable(fromPosition, toPosition)){
            boardDAO.updatePiece(fromPosition, PieceType.BLANK.name());
            boardDAO.updatePiece(toPosition, piece.getName());
        }

        updateTurn();
        return board;
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
            return;
        }
        turnDAO.updateTurn(Team.WHITE);
    }

    public Team getCurrentTurn() throws SQLException {
        return turnDAO.findTurn();
    }

    public boolean isFinish(final Board board) {
        return board.isFinished();
    }

    public String receiveWinner() throws SQLException {
        updateTurn();
        Team team = turnDAO.findTurn();
        return team.toString();
    }

}
