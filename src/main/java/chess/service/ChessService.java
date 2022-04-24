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
import chess.dto.BoardElementDto;
import chess.dto.ChessGameDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static chess.dao.ChessDao.GAME_ID;

@Service
public final class ChessService {

    private static final int PIECE_NAME_INDEX = 0;
    private static final int PIECE_COLOR_INDEX = 1;

    private final ChessDao chessDao;
    private ChessGame chessGame;

    public ChessService(ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public ChessGameDto newGame() {
//        chessDao.initBoard(toBoardDtos(GAME_ID, new BoardInitializer().init()));
        deleteOldData(GAME_ID);
        initNewChessGame(GAME_ID);
        chessGame = new ChessGame(new Running(Color.from(chessDao.findCurrentColor(GAME_ID)), convertToBoard(chessDao.findBoard(GAME_ID))));
        return new ChessGameDto(chessDao.findBoard(GAME_ID), chessGame.status());
    }

    private void deleteOldData(int gameId) {
        chessDao.deleteAllPiece(gameId);
        chessDao.deleteGame(gameId);
    }

    private void initNewChessGame(int gameId) {
        chessDao.initGame(GAME_ID);
        chessDao.updateTurn("WHITE", gameId);
        chessDao.savePiece(toBoardDtos(gameId, new BoardInitializer().init()));
    }

    private List<BoardElementDto> toBoardDtos(int gameId, Map<Position, Piece> board) {
        return board.entrySet()
                .stream()
                .map(it -> toBoardDto(gameId, it.getKey(), it.getValue()))
                .collect(Collectors.toList());
    }

    private BoardElementDto toBoardDto(int gameId, Position position, Piece piece) {
        return new BoardElementDto(gameId, position, piece);
    }

    /*private Board convertToBoard(final Map<String, List<String>> boardData) {
        final Map<Position, Piece> board = new HashMap<>();
        for (final Map.Entry<String, List<String>> entry : boardData.entrySet()) {
            board.put(Position.from(entry.getKey()), PieceFactory.of(entry.getValue().get(PIECE_NAME_INDEX), entry.getValue().get(PIECE_COLOR_INDEX)));
        }
        return new Board(() -> board);
    }*/

    private Board convertToBoard(final List<BoardElementDto> boardDatas) {
        Map<Position, Piece> board = boardDatas.stream()
                .collect(Collectors.toMap(it -> Position.from(it.getPosition()), it -> PieceFactory.of(it.getPieceName(), it.getPieceColor())));
        return new Board(() -> board);
    }

    public ChessGameDto move(final String from, final String to) {
        chessGame.move(Position.from(from), Position.from(to));
        final var nextColor = Color.from(chessDao.findCurrentColor(GAME_ID)).next();
        updateBoard(from, to, GAME_ID, nextColor.name());
        return new ChessGameDto(chessDao.findBoard(GAME_ID), chessGame.status());
    }

    private void updateBoard(String from, String to, int gameId, String color) {
        chessDao.deletePiece(to);
        chessDao.updatePiece(from, to, gameId);
        chessDao.updateTurn(color, gameId);
    }

    public ChessGameDto loadGame() {
        chessGame = new ChessGame(StateFactory.of(Color.from(chessDao.findCurrentColor(GAME_ID)), convertToBoard(chessDao.findBoard(GAME_ID))));
        return new ChessGameDto(chessDao.findBoard(GAME_ID), chessGame.status());
    }
}
