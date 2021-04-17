package chess.service;

import org.springframework.stereotype.Service;

import chess.domain.board.BoardDto;
import chess.domain.chess.Chess;
import chess.domain.chess.ChessDao;
import chess.domain.chess.ChessDto;
import chess.domain.piece.PieceDao;
import chess.domain.position.MovePosition;

@Service
public class ChessService {

    private final ChessDao chessDAO;
    private final PieceDao pieceDAO;

    public ChessService(ChessDao chessDAO, PieceDao pieceDAO) {
        this.chessDAO = chessDAO;
        this.pieceDAO = pieceDAO;
    }

    public ChessDto getChessGame(Long chessId) {
        final Chess chess = chessDAO.findChessById(chessId);
        return new ChessDto(chess);
    }

    public Long insert() {
        final Chess chess = Chess.createWithEmptyBoard()
                                 .start();
        final BoardDto boardDto = BoardDto.from(chess);

        final long chessId = chessDAO.insert();
        pieceDAO.insert(chessId, boardDto);

        return chessId;
    }

    public void move(Long chessId, MovePosition movePosition) {
        final Chess chess = chessDAO.findChessById(chessId)
                                    .move(movePosition);
        pieceDAO.move(chessId, movePosition);
        chessDAO.updateChess(chessId, chess.status(), chess.color());
    }
}
