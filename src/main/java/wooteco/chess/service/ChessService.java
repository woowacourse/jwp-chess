package wooteco.chess.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.gameinfo.GameInfo;
import wooteco.chess.entity.Room;
import wooteco.chess.dto.LineDto;
import wooteco.chess.dto.RowsDtoConverter;
import wooteco.chess.repository.RoomRepository;

@Service
public class ChessService {

    private RoomRepository roomRepository;
    private Map<Room, GameInfo> games;

    public ChessService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        games = new HashMap<>();
    }

    public GameInfo findByRoom(Room room) {
        if (!roomRepository.findByName(room.getName()).isPresent()) {
            room.addBoard(BoardFactory.createInitialBoard());
            room.updateTurn(0);
            roomRepository.save(room);
        }
        Room saved = roomRepository.findByName(room.getName()).orElseGet(() -> roomRepository.save(new Room(room.getName(), 0)));
        Board board = saved.createBoard();
        int turn = saved.getTurn();
        GameInfo gameInfo = GameInfo.from(board, turn);
        games.put(room, gameInfo);
        return gameInfo;
    }

    public void move(Room room, String source, String target) {
        GameInfo gameInfo = games.get(room)
                .move(source, target);
        games.put(room, gameInfo);
    }

    public void save(Room room) {
        GameInfo gameInfo = games.get(room);
        Room saved = roomRepository.findByName(room.getName()).orElseGet(() -> new Room(room.getName(), 0));
        saved.updateBoard(gameInfo.getBoard());
        saved.updateTurn(gameInfo.getStatus().getTurn());
        roomRepository.save(saved);
        games.remove(room);
    }

    public void delete(Room room) {
        Room saved = roomRepository.findByName(room.getName()).orElseGet(() -> new Room(room.getName(), 0));
        roomRepository.delete(saved);
        games.remove(room);
    }

    public List<String> searchPath(Room room, String sourceInput) {
        return games.get(room)
                .searchPath(sourceInput)
                .stream()
                .map(Position::getName)
                .collect(Collectors.toList());
    }

    public List<LineDto> getEmptyRowsDto() {
        return RowsDtoConverter.convertFrom(BoardFactory.EMPTY_BOARD);
    }

    public List<LineDto> getRowsDto(Room room) {
        GameInfo gameInfo = findByRoom(room);
        return RowsDtoConverter.convertFrom(gameInfo.getBoard());
    }

    public int getTurn(Room room) {
        return games.get(room)
                .getStatus()
                .getTurn();
    }

    public double calculateWhiteScore(Room room) {
        return games.get(room)
                .getWhiteScore()
                .getScore();
    }

    public double calculateBlackScore(Room room) {
        return games.get(room)
                .getBlackScore()
                .getScore();
    }

    public boolean checkGameNotFinished(Room room) {
        return games.get(room)
                .getStatus()
                .isNotFinished();
    }

    public Board getBoard(Room room) {
        return games.get(room)
                .getBoard();
    }
}
