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

    public ResponseDto start() {
        chessGame = new ChessGame();
        try {
            if (pieceDao.existPieces()) {
                chessGame.load(pieceDao.load(), boardDao.findTurn());
                return new ResponseDto(200, "");
            }
            chessGame.start();
            return new ResponseDto(200, "");
        } catch (Exception e) {
            return new ResponseDto(501, e.getMessage());
        }
    }

    public ResponseDto move(String rawSource, String rawTarget) {
        try {
            final Position source = Position.from(rawSource);
            final Position target = Position.from(rawTarget);
            chessGame.move(source, target);
            savePieces(source, target);
            if (!isRunning()) {
                end();
                return new ResponseDto(301, "");
            }
            return new ResponseDto(302, "");
        } catch (Exception e) {
            return new ResponseDto(501, e.getMessage());
        }
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

    public ResponseDto end() {
        try {
            chessGame.end();
            pieceDao.delete();
            boardDao.deleteBoard();
            return new ResponseDto(200, "");
        } catch (Exception e) {
            return new ResponseDto(501, e.getMessage());
        }
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
