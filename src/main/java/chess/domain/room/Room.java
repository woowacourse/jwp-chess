package chess.domain.room;

import chess.domain.ChessGame;
import chess.domain.TeamColor;
import chess.exception.MaxPlayerSizeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Room {
    private static final int MAX_PLAYER_SIZE = 2;

    private Long id;
    private RoomInformation roomInformation;
    private ChessGame chessGame;
    private List<User> users;

    public Room(Long id, RoomInformation roomInformation, ChessGame chessGame,
        List<User> users) {
        this.id = id;
        this.roomInformation = roomInformation;
        this.chessGame = chessGame;
        this.users = users;
    }

    public Room(Long id, RoomInformation roomInformation, ChessGame chessGame) {
        this(id, roomInformation, chessGame, new ArrayList<>());
    }

    public void enterAsPlayer(User user, TeamColor teamColor) {
        long playerCount = users.stream().filter(User::isPlayer).count();
        if(playerCount >= MAX_PLAYER_SIZE) {
            throw new MaxPlayerSizeException();
        }
        user.setAsPlayer(teamColor);
        users.add(user);
    }

    public void enterAsParticipant(User user) {
        user.setAsNotPlayer();
        users.add(user);
    }

    public boolean hasSameId(Long roomId) {
        return this.id.equals(roomId);
    }

    public Long id() {
        return id;
    }

    public String title() {
        return roomInformation.title();
    }

    public int count() {
        return users.size();
    }

    public boolean isLocked() {
        return roomInformation.isLocked();
    }

    public boolean passwordIncorrect(String password) {
        return roomInformation.passwordIncorrect(password);
    }

    public ChessGame chessGame() {
        return chessGame;
    }

    public List<User> players() {
        return users.stream().filter(User::isPlayer).collect(Collectors.toList());
    }

    public Optional<User> whitePlayer() {
        return users.stream().filter(User::isWhite).findAny();
    }

    public Optional<User> blackPlayer() {
        return users.stream().filter(User::isBlack).findAny();
    }

    public void leaveRoom(User user) {
        users.remove(user);
    }

    public List<User> users() {
        return users;
    }
}
