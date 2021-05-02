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
import dto.*;
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
    public RoomCreateResponse create(final RoomCreateRequest roomCreateRequest) {
        Long gameId = chessGameRepository.create(new ChessGame(new WhiteTeam(), new BlackTeam()));

        Long roomId = chessRoomRepository.create(new Room(null,
                new RoomInfo(roomCreateRequest.getName(), roomCreateRequest.getPw(), gameId),
                new Players(roomCreateRequest.getUser())));

        return new RoomCreateResponse(roomId);
    }

    @Override
    public List<RoomDto> rooms() {
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

    private List<RoomDto> generateEnterableRoomDataList(List<Room> rooms) {
        List<Room> enterableRooms = rooms.stream()
                .filter(Room::enterable)
                .collect(Collectors.toList());

       return enterableRooms.stream()
               .map(RoomDto::new)
               .collect(Collectors.toList());
    }

    @Override
    public RoomDto enter(RoomRequestDto roomRequestDto) {
        validateEnterStatus(roomRequestDto);
        chessRoomRepository.join(roomRequestDto.getUser(), roomRequestDto.getId());
        Room room = chessRoomRepository.room(roomRequestDto.getId());
        return new RoomDto(room);
    }

    @Override
    public RoomLoadResponse load(final Long roomId) {
        Room room = chessRoomRepository.room(roomId);
        ChessGame chessGame = chessGameRepository.chessGame(room.getGameId());
        return new RoomLoadResponse(new RoomDto(room),
                new ChessGameDto(room.getGameId(), chessGame));
    }

    @Override
    public void exit(final Long roomId, final String userName) {
        userDao.setRoomId(null,userName);
        chessRoomRepository.deleteUserFormRoom(roomId, userName);
    }

    @Override
    public ChessGameDto exitReturnEndChessGame(final RoomRequestDto roomRequestDto, final String userName) {
        userDao.setRoomId(null,userName);
        chessRoomRepository.deleteUserFormRoom(roomRequestDto.getId(), userName);
        ChessGame chessGame = chessGameRepository.chessGame(roomRequestDto.getGameId());
        chessGame.finish();
        return new ChessGameDto(roomRequestDto.getGameId(), chessGame);
    }

    public void validateEnterStatus(RoomRequestDto roomRequestDto) {
        RoomInfo roomInfo = new RoomInfo (roomRequestDto.getName(), roomRequestDto.getPw(), roomRequestDto.getGameId());
        Room savedRoom = chessRoomRepository.room(roomRequestDto.getId());
        User user = userDao.findByName(roomRequestDto.getUser());

        if (!savedRoom.checkPassword(roomInfo)){
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
