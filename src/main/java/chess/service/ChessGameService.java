package chess.service;

import chess.dao.BoardDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.Winner;
import chess.domain.board.Position;
import chess.dto.ChessBoardDto;
import chess.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final PieceDao pieceDao;
    private final BoardDao boardDao;
    private ChessGame chessGame;

    public ChessGameService(PieceDao pieceDao, BoardDao boardDao) {
        this.pieceDao = pieceDao;
        this.boardDao = boardDao;
    }

    public void start() {
        chessGame = new ChessGame();
        if (pieceDao.existPieces()) {
            chessGame.load(pieceDao.load(), boardDao.findTurn());
            return;
        }
        chessGame.start();
    }

    public ResponseCode move(String rawSource, String rawTarget) {
        final Position source = Position.from(rawSource);
        final Position target = Position.from(rawTarget);
        chessGame.move(source, target);
        savePieces(source, target);
        if (!isRunning()) {
            end();
            return ResponseCode.MOVED_PERMANENTLY;
        }
        return ResponseCode.FOUND;
    }

    private void savePieces(Position source, Position target) {
        if (pieceDao.existPieces()) {
            updatePosition(source, target, chessGame.getTurn());
            return;
        }
        save(chessGame.getTurn());
    }

    private void updatePosition(Position source, Position target, Color turn) {
        boardDao.save(turn);
        pieceDao.updatePosition(source.stringName(), target.stringName());
    }

    private void save(Color turn) {
        boardDao.save(turn);
        pieceDao.save(chessGame.getBoard().getPiecesByPosition());
    }

    public void end() {
        chessGame.end();
        pieceDao.delete();
        boardDao.deleteBoard();
    }

    public ChessBoardDto getBoard() {
        return ChessBoardDto.from(chessGame.getBoard().getPiecesByPosition());
    }

    public boolean isRunning() {
        return chessGame.isRunning();
    }

    public double statusOfWhite() {
        return chessGame.statusOfWhite();
    }

    public double statusOfBlack() {
        return chessGame.statusOfBlack();
    }

    public Winner findWinner() {
        return chessGame.findWinner();
    }

    public Color getTurn() {
        return chessGame.getTurn();
    }
}
