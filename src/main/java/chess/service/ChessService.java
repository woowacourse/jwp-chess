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
import chess.dto.StatusDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.entity.BoardEntity;
import chess.entity.RoomEntity;
import chess.repository.BoardRepository;
import chess.repository.RoomRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        final RoomEntity room = new RoomEntity(roomRequestDto.getName(), "white", false);
        final RoomEntity createdRoom = roomRepository.insert(room);
        boardRepository.batchInsert(createBoards(createdRoom));
        return RoomResponseDto.of(createdRoom);
    }

    private List<BoardEntity> createBoards(final RoomEntity createdRoom) {
        final Map<Position, Piece> board = BoardFactory.initialize();
        return board.entrySet().stream()
            .map(entry -> new BoardEntity(createdRoom.getId(),
                entry.getKey().convertPositionToString(),
                entry.getValue().convertPieceToString()))
            .collect(Collectors.toList());
    }

    public GameResponseDto enterRoom(final Long roomId) {
        final RoomEntity room = roomRepository.findById(roomId);
        validateGameOver(room);
        final List<BoardEntity> boards = boardRepository.findBoardByRoomId(roomId);
        return GameResponseDto.of(room, BoardsDto.of(boards));
    }

    public GameResponseDto move(final Long id, final MoveRequestDto moveRequestDto) {
        final RoomEntity room = roomRepository.findById(id);
        validateGameOver(room);
        final List<BoardEntity> boardEntity = boardRepository.findBoardByRoomId(id);

        final ChessGame chessGame = new ChessGame(toBoard(boardEntity), new Turn(Team.of(room.getTeam())));
        final String sourcePosition = moveRequestDto.getSource();
        final String targetPosition = moveRequestDto.getTarget();

        chessGame.move(sourcePosition, targetPosition);

        final BoardEntity sourceBoardEntity = new BoardEntity(id, sourcePosition,
            chessGame.getPieceName(sourcePosition));
        final BoardEntity targetBoardEntity = new BoardEntity(id, targetPosition,
            chessGame.getPieceName(targetPosition));
        boardRepository.updateBatchPositions(List.of(sourceBoardEntity, targetBoardEntity));

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

    private Board toBoard(final List<BoardEntity> boardEntity) {
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

    public void endRoom(final Long id) {
        final RoomEntity room = roomRepository.findById(id);
        validateGameOver(room);
        roomRepository.updateGameOver(id);
    }

    public StatusDto createStatus(final Long id) {
        final Board board = toBoard(boardRepository.findBoardByRoomId(id));
        return StatusDto.of(new Score(board.getBoard()));
    }

    private void validateGameOver(final RoomEntity room) {
        if (room.isGameOver()) {
            throw new IllegalArgumentException("[ERROR] 이미 종료된 게임입니다.");
        }
    }
}
