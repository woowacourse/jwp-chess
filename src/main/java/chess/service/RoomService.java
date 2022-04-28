package chess.service;

import chess.dto.GameDeleteResponseDto;
import chess.dto.RoomDto;
import chess.dto.RoomsDto;
import chess.model.board.Board;
import chess.model.member.Member;
import chess.model.room.Room;
import chess.repository.BoardRepository;
import chess.repository.ChessBoardRepository;
import chess.repository.ChessMemberRepository;
import chess.repository.ChessRoomRepository;
import chess.repository.MemberRepository;
import chess.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository<Room> chessRoomRepository;
    private final MemberRepository<Member> chessMemberRepository;
    private final BoardRepository<Board> chessBoardRepository;

    public RoomService(RoomRepository<Room> chessRoomRepository,
                       MemberRepository<Member> chessMemberRepository,
                       BoardRepository<Board> chessBoardRepository) {
        this.chessRoomRepository = chessRoomRepository;
        this.chessMemberRepository = chessMemberRepository;
        this.chessBoardRepository = chessBoardRepository;
    }

    public RoomsDto getRooms() {
        List<RoomDto> roomsDto = new ArrayList<>();
        List<Room> rooms = chessRoomRepository.findAll();
        for (Room room : rooms) {
            List<Member> membersByRoom = chessMemberRepository.findMembersByRoomId(room.getId());
            boolean isEnd = chessBoardRepository.getStatusById(room.getBoardId()).isEnd();
            roomsDto.add(
                    new RoomDto(room.getId(), room.getTitle(), room.getPassword(), membersByRoom, isEnd));
        }
        return new RoomsDto(roomsDto);
    }

    public GameDeleteResponseDto deleteRoom(int id, String password) {
        Room room = chessRoomRepository.getById(id);
        if (room.getPassword().equals(password)) {
            chessRoomRepository.deleteById(id);
            return new GameDeleteResponseDto(true, "삭제되었습니다.");
        }
        return new GameDeleteResponseDto(false, "삭제할 수 없습니다.");
    }

}
