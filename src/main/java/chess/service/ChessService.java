package chess.service;

import chess.dao.BackupBoardDao;
import chess.dao.RoomDao;
import chess.domain.Game;
import chess.domain.Rooms;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
import chess.dto.MoveRequestDto;
import chess.dto.MoveResponseDto;
import chess.dto.RoomNameDto;
import chess.dto.SquareDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final RoomDao roomDao;
    private final BackupBoardDao backupBoardDao;
    private final Rooms rooms;

    public ChessService(RoomDao roomDao, BackupBoardDao backupBoardDao) {
        this.roomDao = roomDao;
        this.backupBoardDao = backupBoardDao;
        this.rooms = new Rooms();
    }

    public Game restartGame(String name, String password) {
        Game currentGame = currentGame(name, password);
        currentGame.init();
        return currentGame;
    }

    public Game currentGame(String name, String password) {
        Optional<Game> game = rooms.findGame(name);
        return game.orElseGet(() -> getGame(name, password));
    }

    private Game getGame(String name, String password) {
        if (roomDao.existsRoom(name)) {
            Game findGame = Game.game(new Board(playingBoard(name)),
                roomDao.findRoomTurnColor(name), password);
            rooms.addRoom(name, findGame);
            return findGame;
        }

        Game newGame = Game.newGame(password);
        rooms.addRoom(name, newGame);
        roomDao.addRoom(name, PieceColor.WHITE);
        return newGame;
    }

    public void setBlackPassword(String name, String password) {
        Optional<Game> game = rooms.findGame(name);
        game.ifPresent(existingGame -> {
            addBlackPassword(password, existingGame);
        });
    }

    private void addBlackPassword(String password, Game existingGame) {
        if (existingGame.emptyBlackPlayer()) {
            existingGame.setBlackPassword(password);
        }
    }

    private Map<Position, Piece> playingBoard(String name) {
        List<SquareDto> boardInfo = backupBoardDao.findPlayingBoardByRoom(name);

        return boardInfo.stream()
            .collect(Collectors.toMap(this::position, this::piece));
    }

    private Position position(SquareDto squareDto) {
        return Position.from(squareDto.getPosition());
    }

    private Piece piece(SquareDto squareDto) {
        List<String> pieceInfo = Arrays.asList(squareDto.getPiece().split(("_")));
        return new Piece(pieceInfo.get(1), pieceInfo.get(0));
    }

    public MoveResponseDto move(MoveRequestDto moveRequestDto, String password) {
        String name = moveRequestDto.getRoomName();
        String source = moveRequestDto.getSource();
        String target = moveRequestDto.getTarget();

        Game currentGame = currentGame(name, password);
        currentGame.move(source, target, password);
        if (currentGame.isEnd()) {
            deleteRoom(name);
            return new MoveResponseDto(source, target,
                currentGame.winnerColor().getSymbol());
        }

        return new MoveResponseDto(source, target,
            currentGame.turnColor().getName());
    }

    public void savePlayingBoard(String name, String password) {
        Game currentGame = currentGame(name, password);
        backupBoardDao.deleteExistingBoard(name);
        roomDao.deleteRoom(name);
        roomDao.addRoom(name, currentGame.turnColor());
        backupBoardDao.addPlayingBoard(name, currentGame.getBoard());
    }

    public void deleteRoomFromList(String roomName) {
        backupBoardDao.deleteExistingBoard(roomName);
        roomDao.deleteRoom(roomName);
    }

    public void deleteRoom(String roomName) {
        backupBoardDao.deleteExistingBoard(roomName);
        roomDao.deleteRoom(roomName);
        rooms.deleteRoom(roomName);
    }

    public List<RoomNameDto> roomNames() {
        return roomDao.findRoomNames();
    }
}
