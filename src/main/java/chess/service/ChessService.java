package chess.service;

import chess.dao.ChessDao;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.Ready;
import chess.domain.state.Running;
import chess.domain.state.StateFactory;
import chess.dto.ChessGameDto;
import chess.dto.GameRoomDto;
import chess.dto.MoveDto;
import chess.dto.PieceAndPositionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private static final String FIRST_COLOR = "WHITE";

    private final ChessDao chessDao;
    private ChessGame chessGame = new ChessGame(new Ready());

    public ChessService(final ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public ChessGameDto newGame(final int gameId) {
        deleteOldData(gameId);
        initNewChessGame(gameId);
        chessGame = new ChessGame(new Running(Color.from(chessDao.findCurrentColor(gameId)), convertToBoard(chessDao.findAllPiece(gameId))));
        return new ChessGameDto(chessDao.findAllPiece(gameId), chessGame.status());
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

    public ChessGameDto move(final MoveDto moveDto) {
        chessGame.move(Position.from(moveDto.getFrom()), Position.from(moveDto.getTo()));
        final var nextColor = Color.from(chessDao.findCurrentColor(moveDto.getGameId())).next();
        updateBoard(moveDto.getFrom(), moveDto.getTo(), moveDto.getGameId(), nextColor.name());
        return new ChessGameDto(chessDao.findAllPiece(moveDto.getGameId()), chessGame.status());
    }

    private void updateBoard(final String from, final String to, final int gameId, final String color) {
        chessDao.deletePiece(gameId, to);
        chessDao.updatePiece(from, to, gameId);
        chessDao.updateTurn(color, gameId);
    }

    public ChessGameDto loadGame(final int gameId) {
        chessGame = new ChessGame(StateFactory.of(Color.from(chessDao.findCurrentColor(gameId)), convertToBoard(chessDao.findAllPiece(gameId))));
        return new ChessGameDto(chessDao.findAllPiece(gameId), chessGame.status());
    }

    public Number createRoom(final GameRoomDto gameRoomDto) {
        return chessDao.initGame(gameRoomDto.getTitle(), gameRoomDto.getPassword());
    }

    public ChessGameDto findGame(final int gameId) {
        return new ChessGameDto(chessDao.findAllPiece(gameId), chessGame.status());
    }

    public void deleteGame(final GameRoomDto gameRoomDto) {
        final var actual = chessDao.findPassword(gameRoomDto.getGameId());
        if (!actual.equals(gameRoomDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        chessDao.deleteGame(gameRoomDto.getGameId());
    }

    public List<GameRoomDto> findAllGame() {
        return chessDao.findAllGame();
    }
}
