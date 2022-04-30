package chess.service;

import chess.dao.BoardDao;
import chess.dao.PieceDao;
import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.BoardInfoDto;
import chess.dto.CreateBoardDto;
import chess.dto.ResultDto;
import chess.dto.StatusDto;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final PieceDao pieceDao;
    private final BoardDao boardDao;

    public ChessGameService(PieceDao pieceDao, BoardDao boardDao) {
        this.pieceDao = pieceDao;
        this.boardDao = boardDao;
    }

    public List<BoardInfoDto> getBoards() {
        return boardDao.getAllBoardInfo();
    }

    public int start(CreateBoardDto createBoardDto) {
        Board board = new Board(BoardInitializer.createBoard(), Color.WHITE, createBoardDto);
        int id = boardDao.makeBoard(board);
        pieceDao.save(BoardInitializer.createBoard(), id);
        return id;
    }

    public HttpStatus move(Position source, Position target, int id) {
        Board board = getBoard(id);
        board.move(source, target);

        if (board.hasKingCaptured()) {
            return HttpStatus.ACCEPTED;
        }

        boardDao.updateTurn(board.getTurn(), id);
        pieceDao.updatePosition(source.stringName(), target.stringName(), id);
        return HttpStatus.OK;
    }

    public StatusDto status(int id) {
        Board board = getBoard(id);
        return StatusDto.of(board.scoreOfWhite(), board.scoreOfBlack());
    }

    public Board getBoard(int boardId) {
        return new Board(getPieces(boardId), getTurn(boardId), getName(boardId), getPassword(boardId));
    }

    public ResultDto result(int boardId) {
        Board board = getBoard(boardId);
        return ResultDto.of(board.scoreOfWhite(), board.scoreOfBlack(), board.findWinner());
    }

    public void end(int id) {
        boardDao.end(id);
    }

    public Map<Position, Piece> getPieces(int boardId) {
        return pieceDao.load(boardId);
    }

    public Color getTurn(int id) {
        return boardDao.findTurn(id);
    }

    private String getName(int boardId) {
        return boardDao.getName(boardId);
    }

    private String getPassword(int boardId) {
        return boardDao.getPassword(boardId);
    }

    public void deleteBoard(int id, String password) {
        Board board = getBoard(id);
        board.delete(password);

        pieceDao.delete(id);
        boardDao.deleteBoard(id, password);
    }
}
