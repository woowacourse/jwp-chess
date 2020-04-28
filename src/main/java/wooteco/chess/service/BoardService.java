package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.BoardDAO;

import java.sql.SQLException;

@Service
public class BoardService {

    private BoardDAO boardDAO;
    private RoomService roomService;

    public BoardService() throws SQLException {
        this.boardDAO = new BoardDAO();
        roomService = new RoomService();
    }

//    public Map<String, Object> initializeBoard(final Long roomId) throws SQLException {
//        Board board = BoardFactory.initializeBoard();
//        boardDAO.deleteAllById(roomId);
//
//        for (Position position : board.getBoard().keySet()) {
//            boardDAO.insertPiece(roomId, board, position);
//        }
//        return createBoardModel(board);
//    }
//
//    public Map<String, Object> loadBoard(final Long roomId) throws SQLException {
//        Board board = new Board(boardDAO.findAllById(roomId));
//        return createBoardModel(board);
//    }
//
//    public Board movePiece(final Long roomId, final String fromPosition, final String toPosition) throws SQLException {
//        Board board = new Board(boardDAO.findAllById(roomId));
//        Piece piece = board.findBy(Position.of(fromPosition));
//
//        if (piece.isNotSameTeam(roomService.getCurrentTurn())) {
//            throw new IllegalArgumentException("체스 게임 순서를 지켜주세요.");
//        }
//
//        if(board.isMovable(fromPosition, toPosition)){
//            boardDAO.updatePiece(roomId, fromPosition, PieceType.BLANK.name());
//            boardDAO.updatePiece(roomId, toPosition, piece.getName());
//        }
//
//        roomService.updateTurn();
//        return board;
//    }
//
//    private Map<String, Object> createBoardModel(final Board board) {
//        Map<String, Object> model = new HashMap<>();
//        Map<Position, Piece> rawBoard = board.getBoard();
//        for (Position position : rawBoard.keySet()) {
//            model.put(position.toString(), rawBoard.get(position).getName());
//        }
//        return model;
//    }
//
//    public Map<String, Object> showScoreStatus(final Long roomId) throws SQLException {
//        GameResult gameResult = new GameResult();
//        Board board = new Board(boardDAO.findAllById(roomId));
//
//        double blackScore = gameResult.calculateScore(board, Team.BLACK);
//        double whiteScore = gameResult.calculateScore(board, Team.WHITE);
//
//        Map<String, Object> model = createBoardModel(board);
//        model.put("black", blackScore);
//        model.put("white", whiteScore);
//        return model;
//    }
//
//    public boolean isFinish(final Board board) {
//        return board.isFinished();
//    }

}
