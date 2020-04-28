package wooteco.chess.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
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

    public void initBoard(int roomId) {
        Set<PieceEntity> pieceEntities = new HashSet<>();
        for (Piece alivePiece : new Board().getPieces().getAlivePieces()) {
            pieceEntities.add(new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()));
        }
        roomRepository.save(new RoomEntity((long)roomId, findNameById(roomId), Team.WHITE, pieceEntities));
    }

    public boolean isPresentRoom(String name) {
        return roomRepository.findIdByName(name).isPresent();
    }

    public int createBoard(String name) {
        validateRoomName(name);
        Set<PieceEntity> pieceEntities = new HashSet<>();
        for (Piece alivePiece : new Board().getPieces().getAlivePieces()) {
            pieceEntities.add(new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()));
        }
        RoomEntity savedRoom = roomRepository.save(new RoomEntity(name, Team.WHITE, pieceEntities));
        return savedRoom.getId().intValue();
    }

    private void validateRoomName(String name) {
        if (isPresentRoom(name)) {
            throw new DuplicateRoomNameException("존재하는 방 이름입니다.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("방 이름을 입력해주세요.");
        }
    }

    public Board getSavedBoard(int roomId) {
        RoomEntity roomEntity = roomRepository.findById((long)roomId)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 존재하지 않습니다."));
        return new Board(new Pieces(roomEntity.getPieceEntities()), roomEntity.getTurn());
    }

    public void processMoveInput(Board board, String source, String destination, int roomId) {
        board.movePiece(new Position(source), new Position(destination));
        Pieces pieces = board.getPieces();
        Piece destinationPiece = pieces.findByPosition(new Position(destination));
        Set<PieceEntity> pieceEntities = new HashSet<>();
        for (Piece alivePiece : board.getPieces().getAlivePieces()) {
            pieceEntities.add(new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()));
        }
        roomRepository.save(new RoomEntity((long)roomId, findNameById(roomId), board.getTurn(), pieceEntities));
    }

    public int findIdByName(String name) {
        return roomRepository.findIdByName(name)
            .orElseThrow(() -> new IllegalArgumentException("해당 이름의 방이 존재하지 않습니다."));
    }

    public String findNameById(int roomId) {
        return roomRepository.findById((long)roomId)
            .map(RoomEntity::getName)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 방이 존재하지 않습니다."));
    }
}
