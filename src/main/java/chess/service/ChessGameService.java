package chess.service;

import chess.dao.BoardDao;
import chess.dao.PieceDao;
import chess.domain.Color;
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

    public boolean isGameEnd(int id){
        return boardDao.isGameEnd(id);
    }

    public Map<Position, Piece> getBoard(int boardId) {
        return pieceDao.load(boardId);
    }

    public Color getTurn(int id) {
        return boardDao.findTurn(id);
    }

}
