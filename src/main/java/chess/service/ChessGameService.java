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
import java.util.List;
import java.util.Map;
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

    public int start(CreateBoardDto boardInfoDto) {
        int id = boardDao.makeBoard(Color.WHITE, boardInfoDto);
        pieceDao.save(BoardInitializer.createBoard(), id);
        return id;
    }

    public void updatePosition(Position source, Position target, Color turn, int id) {
        boardDao.updateTurn(turn, id);
        pieceDao.updatePosition(source.stringName(), target.stringName(), id);
    }

    public void end(int id) {
        boardDao.end(id);
    }

    public boolean isGameEnd(int id) {
        return boardDao.isGameEnd(id);
    }

    public Board getBoard(int boardId) {
        return new Board(getPieces(boardId), getTurn(boardId));
    }

    public Map<Position, Piece> getPieces(int boardId) {
        return pieceDao.load(boardId);
    }

    public Color getTurn(int id) {
        return boardDao.findTurn(id);
    }

    public void deleteBoard(int id, String password) {
        if (!boardDao.getPassword(id).equals(password)) {
            throw new IllegalArgumentException("잘못된 비밀번호 입력입니다.");
        }
        if (boardDao.findTurn(id) != Color.END) {
            throw new IllegalStateException("종료된 게임만 삭제할 수 있습니다");
        }
        pieceDao.delete(id);
        boardDao.deleteBoard(id, password);
    }
}
