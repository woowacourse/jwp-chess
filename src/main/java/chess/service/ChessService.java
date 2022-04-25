package chess.service;

import chess.dao.ChessPieceDao;
import chess.dao.RoomDao;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.position.Position;
import chess.domain.result.EndResult;
import chess.domain.result.MoveResult;
import chess.domain.result.StartResult;
import chess.dto.ChessPieceMapper;
import chess.dto.request.MoveRequestDto;
import chess.dto.response.ChessPieceDto;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomStatusDto;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final ChessPieceDao chessPieceDao;
    private final RoomDao roomDao;

    public ChessService(final ChessPieceDao chessPieceDao, final RoomDao roomDao) {
        this.chessPieceDao = chessPieceDao;
        this.roomDao = roomDao;
    }

    public List<ChessPieceDto> findAllPiece(final int roomId) {
        checkRoomExist(roomId);
        return chessPieceDao.findAllByRoomId(roomId);
    }

    public void initPiece(final int roomId) {
        checkRoomExist(roomId);
        final ChessGame chessGame = findGameByRoomId(roomId);

        final StartResult startResult = chessGame.start();
        updateChessPiece(roomId, startResult.getPieceByPosition());
        updateRoomStatusTo(roomId, GameStatus.PLAYING);
    }

    public MoveResult move(final int roomId, MoveRequestDto requestDto) {
        checkRoomExist(roomId);
        final ChessGame chessGame = findGameByRoomId(roomId);
        final Position from = requestDto.getFrom();
        final Position to = requestDto.getTo();

        final MoveResult moveResult = chessGame.move(from, to);
        updatePosition(roomId, from, to);
        updateRoom(roomId, moveResult.getGameStatus(), moveResult.getCurrentTurn());

        return moveResult;
    }

    private void updatePosition(final int roomId, final Position from, final Position to) {
        chessPieceDao.deleteByRoomIdAndPosition(roomId, to);
        chessPieceDao.updateByRoomIdAndPosition(roomId, from, to);
    }

    public Score findScore(final int roomId) {
        checkRoomExist(roomId);
        final ChessGame chessGame = findGameByRoomId(roomId);

        return chessGame.calculateScore();
    }

    public EndResult result(final int roomId) {
        checkRoomExist(roomId);
        final ChessGame chessGame = findGameByRoomId(roomId);

        final EndResult result = chessGame.end();
        updateRoomStatusTo(roomId, GameStatus.END);

        return result;
    }

    private void checkRoomExist(final int roomId) {
        if (!roomDao.isExistId(roomId)) {
            throw new IllegalArgumentException("존재하지 않는 방 입니다.");
        }
    }

    private ChessGame findGameByRoomId(final int roomId) {
        Map<Position, ChessPiece> pieceByPosition = initAllPiece(roomId);
        Color currentTurn = initCurrentTurn(roomId);
        GameStatus gameStatus = initGameStatus(roomId);

        return new ChessGame(new ChessBoard(pieceByPosition, currentTurn), gameStatus);
    }

    private Map<Position, ChessPiece> initAllPiece(final int roomId) {
        final List<ChessPieceDto> dtos = chessPieceDao.findAllByRoomId(roomId);
        if (dtos.isEmpty()) {
            return ChessBoardFactory.createInitPieceByPosition();
        }

        return dtos.stream()
                .collect(Collectors.toMap(
                        chessPieceDto -> Position.from(chessPieceDto.getPosition()),
                        chessPieceDto -> ChessPieceMapper.toChessPiece(chessPieceDto.getPieceType(),
                                chessPieceDto.getColor())
                ));
    }

    private Color initCurrentTurn(final int roomId) {
        final CurrentTurnDto dto = roomDao.findCurrentTurnById(roomId);
        if (Objects.isNull(dto)) {
            return Color.WHITE;
        }
        return dto.getCurrentTurn();
    }

    private GameStatus initGameStatus(final int roomId) {
        final RoomStatusDto dto = roomDao.findStatusById(roomId);
        if (Objects.isNull(dto)) {
            return GameStatus.READY;
        }
        return dto.getGameStatus();
    }

    private void updateChessPiece(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        chessPieceDao.deleteAllByRoomId(roomId);
        chessPieceDao.saveAll(roomId, pieceByPosition);
    }

    private void updateRoom(final int roomId, final GameStatus gameStatus, final Color currentTurn) {
        roomDao.updateById(roomId, gameStatus, currentTurn);
    }

    private void updateRoomStatusTo(final int roomId, final GameStatus gameStatus) {
        roomDao.updateStatusById(roomId, gameStatus);
    }
}
