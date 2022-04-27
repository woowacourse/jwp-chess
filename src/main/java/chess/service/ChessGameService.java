package chess.service;

import chess.dao.BoardDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.Winner;
import chess.domain.board.Position;
import chess.dto.BoardInfoDto;
import chess.dto.CreateBoardDto;
import chess.dto.ChessBoardDto;
import java.util.List;
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

    public int makeBoard(CreateBoardDto boardInfoDto) {
        return boardDao.makeBoard(Color.WHITE, boardInfoDto);
    }

    public List<BoardInfoDto> getBoards() {
         return boardDao.getAllBoardInfo();
    }

    public void start(int id) {
        chessGame = new ChessGame();
        if (boardDao.existBoard(id)) {
            chessGame.load(pieceDao.load(), boardDao.findTurn(id));
            return;
        }
        chessGame.start();
    }

    public ResponseCode move(String rawSource, String rawTarget, int id) {
        final Position source = Position.from(rawSource);
        final Position target = Position.from(rawTarget);
        chessGame.move(source, target);
        savePieces(source, target, id);
        if (!isRunning()) {
            end(1);
            return ResponseCode.MOVED_PERMANENTLY;
        }
        return ResponseCode.FOUND;
    }

    private void savePieces(Position source, Position target, int id) {
        if (pieceDao.existPieces()) {
            updatePosition(source, target, chessGame.getTurn(), id);
            return;
        }
        save(chessGame.getTurn(), id);
    }

    private void updatePosition(Position source, Position target, Color turn, int id) {
        boardDao.updateTurn(turn, id);
        pieceDao.updatePosition(source.stringName(), target.stringName());
    }

    private void save(Color turn, int id) {
        boardDao.updateTurn(turn, id);
        pieceDao.save(chessGame.getBoard().getPiecesByPosition());
    }

    public void end(int id) {
        chessGame.end();
        pieceDao.delete();
        boardDao.deleteBoard(id);
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
