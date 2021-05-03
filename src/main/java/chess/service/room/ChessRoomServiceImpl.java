package chess.service.room;

import chess.dao.UserDao;
import chess.domain.User;
import chess.domain.game.ChessGame;
import chess.domain.game.ChessGameRepository;
import chess.domain.room.ChessRoomRepository;
import chess.domain.room.Players;
import chess.domain.room.Room;
import chess.domain.room.RoomInfo;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.dto.ChessGameDto;
import chess.dto.RoomDto;
import chess.dto.request.RoomCreateRequest;
import chess.dto.request.RoomEnterRequest;
import chess.dto.request.RoomExitRequest;
import chess.dto.response.ChessRoomStatusResponse;
import chess.dto.response.RoomEnterResponse;
import chess.dto.response.RoomListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChessRoomServiceImpl implements ChessRoomService {
    private final ChessRoomRepository chessRoomRepository;
    private final ChessGameRepository chessGameRepository;
    private final UserDao userDao;

    @Autowired
    public ChessRoomServiceImpl(final ChessRoomRepository chessRoomRepository, final ChessGameRepository ChessGameRepository, final UserDao userDao) {
        this.chessRoomRepository = chessRoomRepository;
        this.chessGameRepository = ChessGameRepository;
        this.userDao = userDao;
    }

    @Override
    public RoomEnterResponse create(final RoomCreateRequest roomCreateRequest) {
        Long gameId = chessGameRepository.create(new ChessGame(new WhiteTeam(), new BlackTeam()));

        Long roomId = chessRoomRepository.create(new Room(null,
                new RoomInfo(roomCreateRequest.getName(), roomCreateRequest.getPw(), gameId),
                new Players(roomCreateRequest.getUserName())));

        return new RoomEnterResponse(roomId);
    }

    @Override
    public List<RoomListResponse> rooms() {
        List<Room> rooms = chessRoomRepository.rooms();
        deleteEmptyRoom(rooms);
        return  generateEnterableRoomDataList(rooms);
    }


    private void deleteEmptyRoom(List<Room> rooms) {
        List<Room> roomsToDelete = rooms.stream()
                .filter(Room::isEmpty)
                .collect(Collectors.toList());

        roomsToDelete.forEach(chessRoomRepository::deleteRoom);
    }

    private List<RoomListResponse> generateEnterableRoomDataList(List<Room> rooms) {
        List<Room> enterableRooms = rooms.stream()
                .filter(Room::enterable)
                .collect(Collectors.toList());

       return enterableRooms.stream()
               .map(room -> new RoomListResponse(room.getId(), room.getName()))
               .collect(Collectors.toList());
    }

    @Override
    public RoomDto enter(RoomEnterRequest request) {
        validateEnterStatus(request);
        chessRoomRepository.join(request.getUserName(), request.getRoomId());
        Room room = chessRoomRepository.room(request.getRoomId());
        return new RoomDto(room);
    }

    @Override
    public ChessRoomStatusResponse load(final Long roomId) {
        Room room = chessRoomRepository.room(roomId);
        ChessGame chessGame = chessGameRepository.chessGame(room.getGameId());
        return new ChessRoomStatusResponse(new RoomDto(room),
                new ChessGameDto(room.getGameId(), chessGame));
    }

    @Override
    public void exit(final Long roomId, final String userName) {
        userDao.setRoomId(null,userName);
        chessRoomRepository.deleteUserFormRoom(roomId, userName);
    }

    @Override
    public ChessRoomStatusResponse exitReturnEndChessGame(final RoomExitRequest request) {
        Room room = chessRoomRepository.room(request.getRoomId());
        userDao.setRoomId(null,request.getUserName());
        chessRoomRepository.deleteUserFormRoom(request.getRoomId(), request.getUserName());
        ChessGame chessGame = chessGameRepository.chessGame(request.getGameId());
        chessGame.finish();
        return new ChessRoomStatusResponse(new RoomDto(room),
                new ChessGameDto(request.getGameId(), chessGame));
    }

    public void validateEnterStatus(RoomEnterRequest request) {
        Room savedRoom = chessRoomRepository.room(request.getRoomId());
        User user = userDao.findByName(request.getUserName());

        if (!savedRoom.checkPassword(request.getPw())){
            throw new IllegalArgumentException("비밀번호가 잘 못되었습니다.");
        }

        if (!savedRoom.enterable()) {
            throw new IllegalArgumentException("방에 입장할 수 없습니다.");
        }

        if (user.inGame()) {
            throw new IllegalArgumentException("이미 게임 중 인 사용자 입니다.");
        }
    }
}
