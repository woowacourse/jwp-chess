package chess.service;

import chess.dao.ChessDao;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.Running;
import chess.domain.state.StateFactory;
import chess.dto.ChessGameDto;
import chess.dto.PieceAndPositionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static chess.dao.ChessDaoImpl.DEFAULT_GAME_ID;

@Service
public class ChessService {

    private static final String FIRST_COLOR = "WHITE";

    private final ChessDao chessDao;
    private ChessGame chessGame;

    public ChessService(final ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public ChessGameDto newGame() {
        deleteOldData(DEFAULT_GAME_ID);
        initNewChessGame(DEFAULT_GAME_ID);
        chessGame = new ChessGame(new Running(Color.from(chessDao.findCurrentColor(DEFAULT_GAME_ID)), convertToBoard(chessDao.findAllPiece(DEFAULT_GAME_ID))));
        return new ChessGameDto(chessDao.findAllPiece(DEFAULT_GAME_ID), chessGame.status());
    }

    private void deleteOldData(final int gameId) {
        chessDao.deleteAllPiece(gameId);
        chessDao.deleteGame(gameId);
    }

    private void initNewChessGame(final int gameId) {
        chessDao.initGame(gameId);
        chessDao.updateTurn(FIRST_COLOR, gameId);
        for (final Map.Entry<Position, Piece> entry : new BoardInitializer().init().entrySet()) {
            chessDao.savePiece(gameId, new PieceAndPositionDto(entry.getKey(), entry.getValue()));
        }
    }

    private Board convertToBoard(final List<PieceAndPositionDto> pieceAndPositionDtos) {
        final Map<Position, Piece> board = pieceAndPositionDtos.stream()
                .collect(Collectors.toMap(it -> Position.from(it.getPosition()), it -> PieceFactory.of(it.getPieceName(), it.getPieceColor())));
        return new Board(() -> board);
    }

    public ChessGameDto move(final String from, final String to) {
        chessGame.move(Position.from(from), Position.from(to));
        final var nextColor = Color.from(chessDao.findCurrentColor(DEFAULT_GAME_ID)).next();
        updateBoard(from, to, DEFAULT_GAME_ID, nextColor.name());
        return new ChessGameDto(chessDao.findAllPiece(DEFAULT_GAME_ID), chessGame.status());
    }

    private void updateBoard(final String from, final String to, final int gameId, final String color) {
        chessDao.deletePiece(gameId, to);
        chessDao.updatePiece(from, to, gameId);
        chessDao.updateTurn(color, gameId);
    }

    public ChessGameDto loadGame() {
        chessGame = new ChessGame(StateFactory.of(Color.from(chessDao.findCurrentColor(DEFAULT_GAME_ID)), convertToBoard(chessDao.findAllPiece(DEFAULT_GAME_ID))));
        return new ChessGameDto(chessDao.findAllPiece(DEFAULT_GAME_ID), chessGame.status());
    }

}
