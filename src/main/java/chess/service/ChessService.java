package chess.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.PieceFactory;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.game.Score;
import chess.domain.game.Turn;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.dto.BoardsDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.StatusResponseDto;
import chess.entity.BoardEntity;
import chess.entity.RoomEntity;
import chess.exception.RoomNotFoundException;
import chess.repository.BoardRepository;
import chess.repository.RoomRepository;
import chess.util.PasswordSha256Encoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ChessService {

    private final RoomRepository roomRepository;
    private final BoardRepository boardRepository;

    public ChessService(final RoomRepository roomRepository, final BoardRepository boardRepository) {
        this.roomRepository = roomRepository;
        this.boardRepository = boardRepository;
    }

    public RoomResponseDto createRoom(final RoomRequestDto roomRequestDto) {
        final RoomEntity room = new RoomEntity(PasswordSha256Encoder.encode(roomRequestDto.getPassword()),
            roomRequestDto.getName(), "white", false);
        validateCreateRoom(room);
        final RoomEntity createdRoom = roomRepository.insert(room);
        boardRepository.batchInsert(createBoards(createdRoom));
        return RoomResponseDto.of(createdRoom);
    }

    private void validateCreateRoom(final RoomEntity roomEntity) {
        if (roomEntity.getId() != null) {
            throw new IllegalArgumentException("[ERROR] 잘못된 방 생성 요청입니다.");
        }
    }

    private List<BoardEntity> createBoards(final RoomEntity room) {
        final Map<Position, Piece> board = BoardFactory.initialize();
        return board.entrySet().stream()
            .map(entry -> new BoardEntity(room.getId(),
                entry.getKey().convertPositionToString(),
                entry.getValue().convertPieceToString()))
            .collect(Collectors.toList());
    }

    public GameResponseDto getCurrentBoards(final Long roomId) {
        final RoomEntity targetRoom = getValidRoom(roomId);
        validateGameOver(targetRoom);
        final List<BoardEntity> boards = boardRepository.findBoardByRoomId(roomId);
        return GameResponseDto.of(targetRoom, BoardsDto.of(boards));
    }

    private RoomEntity getValidRoom(final Long id) {
        final RoomEntity targetRoom;
        try {
            targetRoom = roomRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RoomNotFoundException(1);
        }
        return targetRoom;
    }

    private void validateGameOver(final RoomEntity room) {
        if (room.isGameOver()) {
            throw new IllegalArgumentException("[ERROR] 이미 종료된 게임입니다.");
        }
    }

    public GameResponseDto move(final Long id, final MoveRequestDto moveRequestDto) {
        final ChessGame chessGame = new ChessGame(getBoard(id), new Turn(Team.of(getValidRoom(id).getTeam())));

        final String sourcePosition = moveRequestDto.getSource();
        final String targetPosition = moveRequestDto.getTarget();

        chessGame.move(sourcePosition, targetPosition);

        final BoardEntity sourceBoardEntity = new BoardEntity(id, sourcePosition,
            chessGame.getPieceName(sourcePosition));
        final BoardEntity targetBoardEntity = new BoardEntity(id, targetPosition,
            chessGame.getPieceName(targetPosition));
        boardRepository.batchUpdatePositions(List.of(sourceBoardEntity, targetBoardEntity));

        final String turnAfterMove = chessGame.getCurrentTurn().getValue();
        roomRepository.updateTeam(id, turnAfterMove);
        updateGameOver(id, chessGame);
        return GameResponseDto.of(roomRepository.findById(id), BoardsDto.of(boardRepository.findBoardByRoomId(id)));
    }

    private void updateGameOver(final Long id, final ChessGame chessGame) {
        if (!chessGame.isOn()) {
            roomRepository.updateGameOver(id);
        }
    }

    private Board getBoard(final Long id) {
        final List<BoardEntity> boardEntity = boardRepository.findBoardByRoomId(id);
        final Map<Position, Piece> board = boardEntity.stream()
            .collect(Collectors.toMap(it -> Position.valueOf(it.getPosition()),
                it -> PieceFactory.createPiece(it.getPiece())));
        return new Board(board);
    }

    @Transactional(readOnly = true)
    public RoomsResponseDto findRooms() {
        final List<RoomEntity> rooms = roomRepository.findRooms();
        return RoomsResponseDto.of(rooms);
    }

    public void endRoom(final Long id, final RoomRequestDto roomRequestDto) {
        final RoomEntity targetRoom = getValidRoom(id);
        validatePassword(roomRequestDto, targetRoom);
        if (!targetRoom.isGameOver()) {
            roomRepository.updateGameOver(targetRoom.getId());
        }
    }

    public StatusResponseDto calculateStatus(final Long id) {
        final Board board = getBoard(id);
        return StatusResponseDto.of(new Score(board.getBoard()));
    }

    public RoomResponseDto updateRoom(final Long id, final RoomRequestDto roomRequestDto) {
        final RoomEntity targetRoom = getValidRoom(id);
        validateGameOver(targetRoom);
        validatePassword(roomRequestDto, targetRoom);
        final RoomEntity roomEntity = roomRequestDto.toEntity();
        targetRoom.update(roomEntity);
        final RoomEntity updatedRoom = roomRepository.update(targetRoom);
        return RoomResponseDto.of(updatedRoom);
    }

    private void validatePassword(final RoomRequestDto roomRequestDto, final RoomEntity targetRoom) {
        if (roomRequestDto.getPassword() == null) {
            return;
        }
        final String requestPassword = PasswordSha256Encoder.encode(roomRequestDto.getPassword());
        final String encodedPassword = targetRoom.getPassword();
        if (!requestPassword.equals(encodedPassword)) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 틀렸습니다.");
        }
    }

    public RoomResponseDto recreateRoom(final Long id) {
        RoomEntity targetRoom = roomRepository.findById(id);
        targetRoom = new RoomEntity(id, targetRoom.getPassword(), targetRoom.getName(), "white", false);
        roomRepository.update(targetRoom);

        boardRepository.delete(id);
        boardRepository.batchInsert(createBoards(targetRoom));

        return RoomResponseDto.of(targetRoom);
    }
}
