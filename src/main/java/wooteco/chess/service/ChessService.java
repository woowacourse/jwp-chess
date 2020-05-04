package wooteco.chess.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.ResultDto;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.exception.DuplicateRoomNameException;
import wooteco.chess.repository.PieceEntity;
import wooteco.chess.repository.RoomEntity;
import wooteco.chess.repository.RoomRepository;

@Service
public class ChessService {
    private final RoomRepository roomRepository;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void initBoard(UUID roomId) {
        Set<PieceEntity> pieceEntities = new HashSet<>();
        for (Piece alivePiece : new Board().getPieces().getAlivePieces()) {
            pieceEntities.add(new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()));
        }
        roomRepository.save(new RoomEntity(roomId, findNameById(roomId), Team.WHITE, pieceEntities));
    }

    private boolean isPresentRoom(String name) {
        return roomRepository.findIdByName(name).isPresent();
    }

    public List<String> getAllRoomNames() {
        return roomRepository.findAllRoomNames();
    }

    public UUID createBoard(String name) {
        validateRoomName(name);
        Set<PieceEntity> pieceEntities = new HashSet<>();
        for (Piece alivePiece : new Board().getPieces().getAlivePieces()) {
            pieceEntities.add(new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()));
        }
        RoomEntity savedRoom = roomRepository.save(new RoomEntity(name, Team.WHITE, pieceEntities));
        return savedRoom.getId();
    }

    private void validateRoomName(String name) {
        if (isPresentRoom(name)) {
            throw new DuplicateRoomNameException("존재하는 방 이름입니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("방 이름을 입력해주세요.");
        }
    }

    public Board getSavedBoard(UUID roomId) {
        RoomEntity roomEntity = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 존재하지 않습니다."));
        return new Board(roomEntity.getPieces(), roomEntity.getTurn());
    }

    public void processMoveInput(Board board, String source, String destination, UUID roomId) {
        board.movePiece(new Position(source), new Position(destination));
        Set<PieceEntity> pieceEntities = board.getPieces()
            .getAlivePieces()
            .stream()
            .map(alivePiece -> new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()))
            .collect(Collectors.toSet());
        roomRepository.save(new RoomEntity(roomId, findNameById(roomId), board.getTurn(), pieceEntities));
    }

    public UUID findIdByName(String name) {
        return roomRepository.findIdByName(name)
            .orElseThrow(() -> new IllegalArgumentException("해당 이름의 방이 존재하지 않습니다."));
    }

    public String findNameById(UUID roomId) {
        return roomRepository.findById(roomId)
            .map(RoomEntity::getName)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 존재하지 않습니다."));
    }

    public RoomDto createGame(String name) {
        UUID roomId = createBoard(name);
        Board board = getSavedBoard(roomId);
        return RoomDto.of(board, roomId, name);
    }

    public RoomDto findGame(UUID roomId) {
        Board board = getSavedBoard(roomId);
        return RoomDto.of(board, roomId, findNameById(roomId));
    }

    public boolean isGameEnd(UUID roomId) {
        Board board = getSavedBoard(roomId);
        return !board.isBothKingAlive();
    }

    public ResultDto endGame(UUID roomId) {
        Board board = getSavedBoard(roomId);
        return ResultDto.of(board, roomId);
    }
}
