package chess.service;

import chess.dao.BoardDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame2;
import chess.domain.Color;
import chess.domain.Room;
import chess.domain.Winner;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.ChessBoardDto;
import chess.dto.ResponseDto;
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

    public ResponseDto create(String title, String password) {
        final Room room = new Room(title, password);
        try {
            if (boardDao.existsBoardByName(title)) {
                return new ResponseDto(401, "이미 존재하는 방입니다.");
            }
            final ChessGame2 chessGame2 = ChessGame2.create(room);
            final Long boardId = boardDao.save(chessGame2);
            pieceDao.save(chessGame2.getBoard().getPiecesByPosition(), boardId);
            return new ResponseDto(200, "");
        } catch (Exception e) {
            return new ResponseDto(501, e.getMessage());
        }
    }

    public ResponseDto move(Long boardId, String rawSource, String rawTarget) {
        try {
            final Position source = Position.from(rawSource);
            final Position target = Position.from(rawTarget);
            final Map<Position, Piece> loadedBoard = pieceDao.load(boardId);
            final Color turn = boardDao.findTurn(boardId);
            final Board board = new Board(loadedBoard, turn);
            final ChessGame2 chessGame2 = new ChessGame2(boardId, board);
            chessGame2.move(source, target);
            savePieces(chessGame2, source, target);
            if(chessGame2.hasKingCaptured()){
                boardDao.updateTurn(boardId, Color.NONE);
                return new ResponseDto(301, "");
            }
            return new ResponseDto(302, "");
        } catch (Exception e) {
            return new ResponseDto(501, e.getMessage());
        }
    }

    private void savePieces(ChessGame2 chessGame2, Position source, Position target) {
        boardDao.updateTurn(chessGame2.getId(), chessGame2.getBoard().getTurn());
        pieceDao.updatePosition(chessGame2.getId(), source.stringName(), target.stringName());
    }

    public ChessBoardDto getBoard(Long boardId) {
        return ChessBoardDto.from(pieceDao.load(boardId));
    }

    public double statusOfWhite(Long boardId) {
        final Map<Position, Piece> loadedBoard = pieceDao.load(boardId);
        final Color turn = boardDao.findTurn(boardId);
        final Board board = new Board(loadedBoard, turn);
        final ChessGame2 chessGame2 = new ChessGame2(boardId, board);

        return chessGame2.scoreOfWhite();
    }

    public double statusOfBlack(Long boardId) {
        final Map<Position, Piece> loadedBoard = pieceDao.load(boardId);
        final Color turn = boardDao.findTurn(boardId);
        final Board board = new Board(loadedBoard, turn);
        final ChessGame2 chessGame2 = new ChessGame2(boardId, board);

        return chessGame2.scoreOfBlack();
    }

    public Winner findWinner(Long boardId) {
        final Map<Position, Piece> loadedBoard = pieceDao.load(boardId);
        final Color turn = boardDao.findTurn(boardId);
        final Board board = new Board(loadedBoard, turn);
        final ChessGame2 chessGame2 = new ChessGame2(boardId, board);

        return chessGame2.findWinner();
    }

    public ResponseDto end(Long boardId) {
        try {
            boardDao.updateTurn(boardId, Color.NONE);
            return new ResponseDto(200, "");
        } catch (Exception e){
            return new ResponseDto(501, e.getMessage());
        }
    }

    public Color getTurn(Long boardId) {
        return boardDao.findTurn(boardId);
    }

    public ResponseDto restart(Long boardId) {
        try {
            boardDao.updateTurn(boardId, Color.WHITE);
            final Board board = new Board();
            pieceDao.updateAll(board.getPiecesByPosition(), boardId);
            return new ResponseDto(301, "");
        } catch (Exception e) {
            return new ResponseDto(501, e.getMessage());
        }
    }

    public ResponseDto delete(Long boardId) {
        try {
            final Color turn = boardDao.findTurn(boardId);
            if (!(turn == Color.NONE)) {
                return new ResponseDto(401, "종료하지 않은 게임은 삭제할 수 없습니다.");
            }
            pieceDao.delete(boardId);
            boardDao.deleteBoard(boardId);
            return new ResponseDto(201, "");
        } catch (Exception e){
            return new ResponseDto(501, e.getMessage());
        }
    }
}
