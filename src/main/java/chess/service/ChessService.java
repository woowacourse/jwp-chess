package chess.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.piece.Team;
import chess.dto.*;
import chess.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final RoomRepository roomRepository;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public BoardDto createRoom(RoomNameDto roomNameDto) {
        String roomName = roomNameDto.getRoomName();
        validateRoomName(roomName);
        return roomRepository.createRoom(roomNameDto.getRoomName());
    }

    private void validateRoomName(String roomName) {
        if (roomName.isEmpty()) {
            throw new IllegalArgumentException("이름을 최소 1자 이상 입력해주세요.");
        }
        if (!roomRepository.exists(roomName)) {
            throw new IllegalArgumentException("이미 존재하는 방이름입니다.");
        }
        if (roomName.length() > 12) {
            throw new IllegalArgumentException("이름은 12자 이하로 입력해주세요.");
        }
    }

    public BoardDto resetBoard(int roomId) {
        Board resetBoard = BoardFactory.create();
        roomRepository.resetBoard(resetBoard, roomId);
        return BoardDto.of(resetBoard);
    }

    public BoardDto findBoardByRoomId(int roomId) {
        return roomRepository.findBoardByRoomId(roomId);
    }

    public BoardDto move(MoveInfoDto moveInfoDto, int roomId) {
        Board board = findBoardByRoomId(roomId).toBoard();
        roomRepository.updateBoard(moveInfoDto, board, roomId);
        return BoardDto.of(board);
    }

    public RoomsDto findAll() {
        return roomRepository.findAll();
    }

    public ScoreDto score(int roomId) {
        Board board = findBoardByRoomId(roomId).toBoard();

        double whiteScore = board.calculateScore(Team.WHITE);
        double blackScore = board.calculateScore(Team.BLACK);
        return ScoreDto.of(whiteScore, blackScore);
    }
}
