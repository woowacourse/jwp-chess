package chess.service;

import chess.domain.board.BoardDto;
import chess.domain.chess.Chess;
import chess.domain.chess.ChessDao;
import chess.domain.chess.ChessDto;
import chess.domain.piece.PieceDao;
import chess.domain.position.MovePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessDao chessDAO;
    private final PieceDao pieceDAO;

    @Autowired
    public ChessService(ChessDao chessDAO, PieceDao pieceDAO) {
        this.chessDAO = chessDAO;
        this.pieceDAO = pieceDAO;
    }

    public ChessDto getChessGame(Long chessId) {
        final Chess chess = chessDAO.findChessById(chessId);
        return new ChessDto(chess);
    }

    public Long insert() {
        final Chess chess = Chess.createWithEmptyBoard().start();
        final BoardDto boardDTO = BoardDto.from(chess);

        final long chessId = chessDAO.insert();
        pieceDAO.insert(chessId, boardDTO);

        return chessId;
    }

    public void move(Long chessId, MovePosition movePosition) {
        final Chess chess = chessDAO.findChessById(chessId).move(movePosition);
        pieceDAO.move(chessId, movePosition);
        chessDAO.updateChess(chessId, chess.status(), chess.color());
    }
}
