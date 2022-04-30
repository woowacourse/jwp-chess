package chess.service;

import chess.dao.ChessPieceDao;
import chess.dao.RoomDao;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.position.Position;
import chess.domain.result.EndResult;
import chess.domain.result.MoveResult;
import chess.dto.ChessPieceMapper;
import chess.dto.request.MoveRequestDto;
import chess.dto.response.ChessPieceDto;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomStatusDto;
import chess.repository.ChessGameRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessGameRepository chessGameRepository;
    private final ChessPieceDao chessPieceDao;
    private final RoomDao roomDao;

    public ChessService(final ChessGameRepository chessGameRepository, final ChessPieceDao chessPieceDao,
                        final RoomDao roomDao) {
        this.chessGameRepository = chessGameRepository;
        this.chessPieceDao = chessPieceDao;
        this.roomDao = roomDao;
    }

    public List<ChessPieceDto> findAllPiece(final int roomId) {
        final ChessGame chessGame = chessGameRepository.get(roomId);
        final ChessBoard chessBoard = chessGame.getChessBoard();
        return chessBoard.findAllPiece()
                .entrySet()
                .stream()
                .map(it -> ChessPieceDto.of(
                        it.getKey(),
                        ChessPieceMapper.toPieceType(it.getValue()),
                        it.getValue().color()))
                .collect(Collectors.toList());
    }

    private void updateRoomStatusTo(final int roomId, final GameStatus gameStatus) {
        roomDao.updateStatusById(roomId, gameStatus);
    }

    public void move(final int roomId, MoveRequestDto requestDto) {
        final ChessGame chessGame = findGameByRoomId(roomId);
        final Position from = Position.from(requestDto.getFrom());
        final Position to = Position.from(requestDto.getTo());

        final MoveResult moveResult = chessGame.move(from, to);
        updatePosition(roomId, from, to);
        updateRoom(roomId, moveResult.getGameStatus(), moveResult.getCurrentTurn());
    }

    private void updatePosition(final int roomId, final Position from, final Position to) {
        chessPieceDao.deleteByRoomIdAndPosition(roomId, to);
        chessPieceDao.updateByRoomIdAndPosition(roomId, from, to);
    }

    private void updateRoom(final int roomId, final GameStatus gameStatus, final Color currentTurn) {
        roomDao.updateById(roomId, gameStatus, currentTurn);
    }

    public Score findScore(final int roomId) {
        final ChessGame chessGame = findGameByRoomId(roomId);

        return chessGame.calculateScore();
    }

    public EndResult result(final int roomId) {
        final ChessGame chessGame = findGameByRoomId(roomId);

        final EndResult result = chessGame.end();
        updateRoomStatusTo(roomId, GameStatus.END);

        return result;
    }

    private ChessGame findGameByRoomId(final int roomId) {
        Map<Position, ChessPiece> pieceByPosition = findPieceByPosition(roomId);
        Color currentTurn = findCurrentTurn(roomId);
        GameStatus gameStatus = findGameStatus(roomId);

        return new ChessGame(new ChessBoard(pieceByPosition, currentTurn), gameStatus);
    }

    private Map<Position, ChessPiece> findPieceByPosition(final int roomId) {
        final List<ChessPieceDto> dtos = chessPieceDao.findAllByRoomId(roomId);
        if (dtos.isEmpty()) {
            throw new IllegalArgumentException("방이 존재하지 않습니다.");
        }

        return dtos.stream()
                .collect(Collectors.toMap(
                        chessPieceDto -> Position.from(chessPieceDto.getPosition()),
                        chessPieceDto -> ChessPieceMapper.toChessPiece(chessPieceDto.getPieceType(),
                                chessPieceDto.getColor())
                ));
    }

    private Color findCurrentTurn(final int roomId) {
        final CurrentTurnDto dto = roomDao.findCurrentTurnById(roomId);
        return dto.getCurrentTurn();
    }

    private GameStatus findGameStatus(final int roomId) {
        final RoomStatusDto dto = roomDao.findStatusById(roomId);
        return dto.getGameStatus();
    }
}
