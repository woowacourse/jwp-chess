package chess.service;

import chess.dao.BackupBoardDao;
import chess.dao.RoomDao;
import chess.domain.Game;
import chess.domain.Rooms;
import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceColor;
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

    private RoomDao roomDao;
    private BackupBoardDao backupBoardDao;
    private Rooms rooms;

    public ChessService(RoomDao roomDao, BackupBoardDao backupBoardDao) {
        this.roomDao = roomDao;
        this.backupBoardDao = backupBoardDao;
        this.rooms = new Rooms();
    }

    public void savePlayingBoard(String name, Board board, PieceColor turnColor) {
        backupBoardDao.deleteExistingBoard(name);
        roomDao.deleteRoom(name);
        roomDao.addRoom(name, turnColor);
        backupBoardDao.addPlayingBoard(name, board);
    }

    public Game currentGame(String name) {
        Optional<Game> game = rooms.findGame(name);
        if (!game.isPresent()) {
            if (roomDao.existsRoom(name)) {
                Game findGame = Game.game(new Board(playingBoard(name)), roomDao.findRoomTurnColor(name));
                rooms.addRoom(name, findGame);
                return findGame;
            }

            Game newGame = Game.newGame();
            rooms.addRoom(name, newGame);
            return newGame;
        }
        return game.get();
    }

    public Map<Position, Piece> playingBoard(String name) {
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

    public void deleteRoom(String roomName) {
        backupBoardDao.deleteExistingBoard(roomName);
        roomDao.deleteRoom(roomName);
        rooms.deleteRoom(roomName);
    }

    public List<RoomNameDto> roomNames() {
        return roomDao.findRoomNames();
    }
}
