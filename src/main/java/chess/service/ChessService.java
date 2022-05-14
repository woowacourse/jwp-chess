package chess.service;

import chess.dao.ChessDao;
import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.position.Position;
import chess.domain.state.StateFactory;
import chess.dto.ChessGameDto;
import chess.dto.GameRoomDto;
import chess.dto.MoveDto;
import chess.dto.PieceAndPositionDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private static final String FIRST_COLOR = "WHITE";

    private final ChessDao chessDao;

    public ChessService(final ChessDao chessDao) {
        this.chessDao = chessDao;
    }

    public int createRoom(final GameRoomDto gameRoomDto) {
        return chessDao.initGame(gameRoomDto.getTitle(), gameRoomDto.getPassword());
    }

    @Transactional
    public ChessGameDto resetGame(final int gameId) {
        chessDao.deleteAllPiece(gameId);
        chessDao.saveGame(gameId, new Board(new BoardInitializer()));
        chessDao.updateTurn(FIRST_COLOR, gameId);

        return ChessGameDto.from(findChessGameById(gameId));
    }

    public List<GameRoomDto> findAllGame() {
        return chessDao.findAllGame();
    }

    public ChessGame findChessGameById(int gameId) {
        return new ChessGame(
                StateFactory.of(Color.from(chessDao.findCurrentColor(gameId)),
                        convertToBoard(chessDao.findAllPiece(gameId)))
        );
    }

    private Board convertToBoard(final List<PieceAndPositionDto> pieceAndPositionDtos) {
        final Map<Position, Piece> board = pieceAndPositionDtos.stream()
                .collect(Collectors.toMap(
                        it -> Position.from(it.getPosition()),
                        it -> PieceFactory.of(it.getPieceName(), it.getPieceColor()))
                );
        return new Board(() -> board);
    }

    @Transactional
    public ChessGameDto move(final MoveDto moveDto) {
        final var gameId = moveDto.getGameId();
        final var chessGame = findChessGameById(gameId);
        final var from = moveDto.getFrom();
        final var to = moveDto.getTo();

        chessGame.move(Position.from(from), Position.from(to));

        updatePieces(gameId, from, to);

        var currentColor = chessDao.findCurrentColor(gameId);
        var nextColor = Color.from(currentColor).next().toString();
        chessDao.updateTurn(nextColor, gameId);

        return ChessGameDto.from(findChessGameById(gameId));
    }

    private void updatePieces(int gameId, String from, String to) {
        chessDao.deletePiece(gameId, to);
        chessDao.updatePiece(from, to, gameId);
    }

    @Transactional
    public void deleteGame(final GameRoomDto gameRoomDto) {
        final var gameId = gameRoomDto.getGameId();
        final var chessGame = findChessGameById(gameId);
        final var room = chessDao.findRoomById(gameId);

        room.checkPassword(gameRoomDto.getPassword());

        if (chessGame.isRunning()) {
            throw new IllegalArgumentException("게임이 종료되지 않았습니다.");
        }

        chessDao.deleteAllPiece(gameId);
        chessDao.deleteGame(gameId);
    }
}
