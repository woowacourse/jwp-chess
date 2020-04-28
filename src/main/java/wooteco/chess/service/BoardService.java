package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.BoardDAO;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.GameResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class BoardService {

    private BoardDAO boardDAO;
    private RoomService roomService;

    public BoardService() throws SQLException {
        this.boardDAO = new BoardDAO();
        roomService = new RoomService();
    }

    public Board movePiece(final Long roomId, final String fromPosition, final String toPosition) throws SQLException {
        Map<String, String> boardDto = boardDAO.findAllById(roomId);
        Board board = Board.createLoadedBoard(boardDto);
        Piece piece = board.findBy(Position.of(fromPosition));

        if (piece.isNotSameTeam(roomService.getCurrentTurn(roomId))) {
            throw new IllegalArgumentException("체스 게임 순서를 지켜주세요.");
        }

        if(board.isMovable(fromPosition, toPosition)){
            boardDAO.updatePiece(roomId, fromPosition, PieceType.BLANK.name());
            boardDAO.updatePiece(roomId, toPosition, piece.getNextPiece().getName());
        }

        roomService.updateTurn(roomId);
        return board;
    }

    public Map<String, String> showScoreStatus(final Long roomId) throws SQLException {
        GameResult gameResult = new GameResult();
        Board board = Board.createLoadedBoard(boardDAO.findAllById(roomId));

        double blackScore = gameResult.calculateScore(board, Team.BLACK);
        double whiteScore = gameResult.calculateScore(board, Team.WHITE);

        Map<String, String> model = new HashMap<>();
        model.put("black", String.valueOf(blackScore));
        model.put("white", String.valueOf(whiteScore));
        return model;
    }

    public String receiveWinner(final Long roomId) throws SQLException {
        roomService.updateTurn(roomId);
        Team team = roomService.getCurrentTurn(roomId);
        return team.toString();
    }

    public boolean isFinish(final Board board) {
        return board.isFinished();
    }

}
