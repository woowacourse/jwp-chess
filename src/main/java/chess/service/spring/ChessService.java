package chess.service.spring;

import chess.dao.jdbctemplate.BoardDao;
import chess.dao.jdbctemplate.GameDao;
import chess.domain.chessboard.ChessBoard;
import chess.domain.command.GameCommand;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.generator.NormalPiecesGenerator;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.PieceDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private final GameDao gameDao;
    private final BoardDao boardDao;

    @Autowired
    public ChessService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public BoardDto selectBoard() {
        ChessBoard chessBoard = boardDao.find();
        Map<Position, Piece> pieces = chessBoard.getPieces();
        Map<String, PieceDto> board = new HashMap<>();
        for (Position position : pieces.keySet()) {
            String key = position.toString();
            Piece piece = pieces.get(Position.of(key));
            PieceDto pieceDto = new PieceDto(piece.getSymbol().name(), piece.getColor().name());
            board.put(key, pieceDto);
        }
        return new BoardDto(board);
    }

    @Transactional
    public void insertGame() {
        ChessGame chessGame = new ChessGame(new NormalPiecesGenerator());
        chessGame.playGameByCommand(GameCommand.of("start"));

        gameDao.save(chessGame.getState().toString());
        boardDao.save(chessGame.getChessBoard(), gameDao.findId());
    }
}
