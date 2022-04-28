package chess.service;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.position.Position;
import chess.dto.ChessPieceMapper;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomDeleteRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.PieceResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.entity.PieceEntity;
import chess.entity.RoomEntity;
import chess.exception.IllegalCommandException;
import chess.exception.NotExistRoomException;
import chess.result.MoveResult;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    public ChessService(final PieceDao chessPieceDao, final RoomDao roomDao) {
        this.pieceDao = chessPieceDao;
        this.roomDao = roomDao;
    }

    public RoomResponseDto createRoom(final RoomRequestDto roomRequestDto) {
        final RoomEntity room = new RoomEntity(roomRequestDto.getName(), roomRequestDto.getPassword(),
                GameStatus.PLAYING.getValue(), Color.WHITE.getValue());

        final RoomEntity createdRoom = roomDao.save(room);
        initializePieces(createdRoom.getId());

        return RoomResponseDto.from(createdRoom);
    }

    private void initializePieces(final Long roomId) {
        final Map<Position, ChessPiece> pieces = ChessBoardFactory.createInitPieceByPosition();
        pieceDao.saveAllByRoomId(roomId, pieces);
    }

    public List<RoomResponseDto> loadAllRoom() {
        final List<RoomEntity> rooms = roomDao.findAll();
        return rooms.stream()
                .map(RoomResponseDto::from)
                .collect(Collectors.toList());
    }

    public RoomResponseDto loadRoom(final long id) {
        final RoomEntity room = roomDao.findById(id);
        return RoomResponseDto.from(room);
    }

    public void deleteRoom(final long id,  final RoomDeleteRequestDto roomDeleteRequestDto) {
        final RoomEntity room = roomDao.findById(id);

        if (!room.isSamePassword(roomDeleteRequestDto.getPassword())) {
            throw new IllegalCommandException("비밀번호가 일치하지 않습니다.");
        }
        if (!room.isEnd()) {
            throw new IllegalCommandException("게임이 진행중인 방은 삭제할 수 없습니다.");
        }

        roomDao.deleteById(id);
    }

    public List<PieceResponseDto> loadAllPiece(final Long roomId) {
        if (!roomDao.isExistName(roomId)) {
            throw new NotExistRoomException("방이 존재하지 않아 기물 정보를 불러올 수 없습니다.");
        }

        final List<PieceEntity> pieces = pieceDao.findAllByRoomId(roomId);
        return pieces.stream()
                .map(PieceResponseDto::from)
                .collect(Collectors.toList());
    }

    public void movePiece(final long roomId, MoveRequestDto requestDto) {
        if (!roomDao.isExistName(roomId)) {
            throw new NotExistRoomException("방이 존재하지 않아 기물을 움직일 수 없습니다.");
        }

        final ChessGame chessGame = generateGame(roomId);
        final Position from = requestDto.getFrom();
        final Position to = requestDto.getTo();

        final MoveResult moveResult = chessGame.move(from, to);
        updatePosition(roomId, from, to);
        updateRoom(roomId, moveResult.getGameStatus(), moveResult.getCurrentTurn());
    }

    private ChessGame generateGame(final long roomId) {
        final RoomEntity room = roomDao.findById(roomId);
        final Map<Position, ChessPiece> pieces = pieceDao.findAllByRoomId(roomId)
                .stream()
                .collect(Collectors.toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> ChessPieceMapper.toChessPiece(piece.getType(), piece.getColor())
                ));

        return new ChessGame(new ChessBoard(pieces, Color.from(room.getTurn())), GameStatus.from(room.getStatus()));
    }

    private void updatePosition(final long roomId, final Position from, final Position to) {
        pieceDao.deleteByRoomIdAndPosition(roomId, to.getValue());
        pieceDao.updatePositionByRoomId(roomId, from.getValue(), to.getValue());
    }

    private void updateRoom(final long roomId, final GameStatus status, final Color turn) {
        roomDao.updateStatusById(roomId, status.getValue());
        roomDao.updateTurnById(roomId, turn.getValue());
    }

    public ScoreResponseDto loadScore(final long roomId) {
        if (!roomDao.isExistName(roomId)) {
            throw new NotExistRoomException("방이 존재하지 않아 점수를 불러올 수 없습니다.");
        }

        final ChessGame chessGame = generateGame(roomId);
        final Score score = chessGame.calculateScore();
        return ScoreResponseDto.from(score);
    }

    public void end(final long roomId) {
        if (!roomDao.isExistName(roomId)) {
            throw new NotExistRoomException("방이 존재하지 않아 게임을 종료할 수 없습니다.");
        }

        final ChessGame chessGame = generateGame(roomId);
        chessGame.end();
        roomDao.updateStatusById(roomId, GameStatus.END.getValue());
    }
}
