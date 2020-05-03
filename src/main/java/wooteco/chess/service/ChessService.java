package wooteco.chess.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.gameinfo.GameInfo;
import wooteco.chess.dto.LineDto;
import wooteco.chess.dto.RowsDtoConverter;
import wooteco.chess.entity.GameEntity;
import wooteco.chess.entity.Room;
import wooteco.chess.repository.GameRepository;
import wooteco.chess.repository.RoomRepository;

@Service
public class ChessService {

    private RoomRepository roomRepository;
    private GameRepository gameRepository;

    public ChessService(RoomRepository roomRepository, GameRepository gameRepository) {
        this.roomRepository = roomRepository;
        this.gameRepository = gameRepository;
    }

    public GameInfo findByRoom(Room room) {
        if (!roomRepository.findByName(room.getName()).isPresent()) {
            GameEntity gameEntity = new GameEntity(0);
            gameEntity.addBoard(BoardFactory.createInitialBoard());
            room.updateGame(gameEntity);
            roomRepository.save(room);
        }
        Room saved = roomRepository.findByName(room.getName()).orElseThrow(AssertionError::new);
        Board board = saved.getGame()
                .createBoard();
        int turn = saved.getGame()
                .getTurn();
        GameInfo gameInfo = GameInfo.from(board, turn);
        gameRepository.save(saved, gameInfo);
        return gameInfo;
    }

    public void move(Room room, String source, String target) {
        GameInfo gameInfo = gameRepository.findByRoom(room)
                .move(source, target);
        gameRepository.save(room, gameInfo);
    }

    public void save(Room room) {
        GameInfo gameInfo = gameRepository.findByRoom(room);
        GameEntity gameEntity = new GameEntity(gameInfo.getStatus().getTurn());
        gameEntity.updateBoard(gameInfo.getBoard());
        Room saved = roomRepository.findByName(room.getName()).orElseThrow(AssertionError::new);
        saved.updateGame(gameEntity);
        roomRepository.save(saved);
        gameRepository.delete(room);
    }

    public void delete(Room room) {
        Room saved = roomRepository.findByName(room.getName()).orElseThrow(AssertionError::new);
        roomRepository.delete(saved);
        gameRepository.delete(room);
    }

    public List<String> searchPath(Room room, String sourceInput) {
        return gameRepository.findByRoom(room)
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
        return gameRepository.findByRoom(room)
                .getStatus()
                .getTurn();
    }

    public double calculateWhiteScore(Room room) {
        return gameRepository.findByRoom(room)
                .getWhiteScore()
                .getScore();
    }

    public double calculateBlackScore(Room room) {
        return gameRepository.findByRoom(room)
                .getBlackScore()
                .getScore();
    }

    public boolean checkGameNotFinished(Room room) {
        return gameRepository.findByRoom(room)
                .getStatus()
                .isNotFinished();
    }

    public Board getBoard(Room room) {
        return gameRepository.findByRoom(room)
                .getBoard();
    }
}
