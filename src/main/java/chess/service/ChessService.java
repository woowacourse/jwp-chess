package chess.service;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.dto.*;
import chess.domain.piece.Team;
import chess.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final RoomRepository roomRepository;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public BoardDto initializeByName(RoomNameDto roomNameDto) {
        String roomName = roomNameDto.getRoomName();
        validateRoomName(roomName);
        return roomRepository.initializeByName(roomNameDto.getRoomName());
    }

    private void validateRoomName(String roomName) {
        if (roomName.isEmpty()) {
            throw new IllegalArgumentException("이름을 최소 1자 이상 입력해주세요.");
        }
        if (!roomRepository.exists(roomName)) {
            throw new IllegalArgumentException("이미 존재하는 방이름입니다.");
        }
    }

    public BoardDto resetBoard(int roomId) {
        Board resetBoard = BoardFactory.create();
        roomRepository.resetBoard(resetBoard, roomId);
        return BoardDto.of(resetBoard);
    }

    public BoardDto getSavedBoardInfo(int roomId) {
        return roomRepository.findBoardByRoomId(roomId);
    }

    public BoardDto move(MoveInfoDto moveInfoDto, int roomId) {
        Board board = getSavedBoardInfo(roomId).toBoard();
        roomRepository.updateBoard(moveInfoDto, board, roomId);
        return BoardDto.of(board);
    }

    public RoomsDto getRoomList() {
        return roomRepository.findAll();
    }

    public ScoreDto score(int roomId) {
        Board board = getSavedBoardInfo(roomId).toBoard();

        double whiteScore = board.calculateScore(Team.WHITE);
        double blackScore = board.calculateScore(Team.BLACK);
        return ScoreDto.of(whiteScore, blackScore);
    }
}
