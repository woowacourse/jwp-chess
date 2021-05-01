package chess.websocket.commander;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import chess.controller.dto.PieceDto;
import chess.controller.dto.PositionDto;
import chess.controller.dto.RoomRequestDto;
import chess.controller.dto.RoundStatusDto;
import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.TeamColor;
import chess.domain.converter.StringPositionConverter;
import chess.domain.piece.Piece;
import chess.domain.room.Room;
import chess.domain.room.User;
import chess.service.RoomService;
import chess.websocket.ResponseForm;
import chess.websocket.ResponseForm.Form;
import chess.websocket.commander.dto.EnterRoomRequestDto;
import chess.websocket.commander.dto.GameRoomResponseDto;
import chess.websocket.commander.dto.MessageDto;
import chess.websocket.commander.dto.NameDto;
import chess.websocket.commander.dto.RoomResponseDto;
import chess.websocket.domain.Lobby;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("socket")
public class RequestCommander {

    private final RoomService roomService;
    private final ObjectMapper objectMapper;
    private final Lobby lobby;
    private final StringPositionConverter positionConverter;

    public RequestCommander(RoomService roomService,
        ObjectMapper objectMapper, Lobby lobby) {
        this.roomService = roomService;
        this.objectMapper = objectMapper;
        this.lobby = lobby;
        this.positionConverter = new StringPositionConverter();
    }

    public void enterLobby(User user) {
        lobby.enter(user);
    }

    public void leaveLobby(User user) {
        lobby.leave(user);
    }

    public void createRoom(Map<String, Object> data, User user) throws Exception {
        RoomRequestDto roomRequestDto = objectMapper.convertValue(data, RoomRequestDto.class);
        final Long roomId = roomService.createRoom(
            roomRequestDto.getTitle(),
            roomRequestDto.isLocked(),
            roomRequestDto.getPassword(),
            roomRequestDto.getNickname(),
            user
        );
        updateRoomForLobbyUsers(data);

        loadChessGameRoom(roomId, user);
    }

    private void updateRoomForLobbyUsers(Map<String, Object> data) {
        lobby.users().forEach(lobbyUser -> {
            try {
                updateRoom(data, lobbyUser);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void updateRoom(Map<String, Object> data, User user) throws Exception {
        final List<RoomResponseDto> rooms =
            roomService.rooms()
                .stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());

        String response = objectMapper
            .writeValueAsString(new ResponseForm<>(Form.UPDATE_ROOM, rooms));

        user.sendData(response);
    }

    public void enterRoomAsPlayer(Map<String, Object> data, User user) throws Exception {
        final EnterRoomRequestDto enterRoomRequestDto = objectMapper
            .convertValue(data, EnterRoomRequestDto.class);
        roomService.enterRoomAsPlayer(enterRoomRequestDto.getRoomId(),
            enterRoomRequestDto.getPassword(), TeamColor.BLACK, enterRoomRequestDto.getNickname(),
            user);

        loadChessGameRoom(enterRoomRequestDto.getRoomId(), user);

        updateNameForRoomUsers(user.roomId());
        updateRoomForLobbyUsers(data);
    }

    public void enterRoomAsParticipant(Map<String, Object> data, User user) throws Exception {
        final EnterRoomRequestDto enterRoomRequestDto = objectMapper
            .convertValue(data, EnterRoomRequestDto.class);
        roomService.enterRoomAsParticipant(enterRoomRequestDto.getRoomId(),
            enterRoomRequestDto.getPassword(), enterRoomRequestDto.getNickname(), user);

        loadChessGameRoom(enterRoomRequestDto.getRoomId(), user);
    }


    private void loadChessGameRoom(Long roomId, User user) throws Exception {
        Room room = roomService.findRoom(roomId);
        List<PieceDto> pieces = room.chessGame()
            .pieces()
            .asList()
            .stream()
            .map(PieceDto::new)
            .collect(Collectors.toList());

        final String whitePlayer = room.whitePlayer().map(User::name).orElse("");
        final String blackPlayer = room.blackPlayer().map(User::name).orElse("");

        GameRoomResponseDto gameRoomResponseDto = new GameRoomResponseDto(
            pieces, user.isPlayer(), user.teamColor(), whitePlayer, blackPlayer
        );

        user.sendData(objectMapper
            .writeValueAsString(new ResponseForm<>(Form.LOAD_GAME, gameRoomResponseDto)));
        user.sendData(objectMapper
            .writeValueAsString(new ResponseForm<>(Form.UPDATE_STATUS, roundStatus(roomId))));
    }

    private void updateNameForRoomUsers(Long roomId) throws Exception {
        NameDto nameDto = new NameDto();
        Room room = roomService.findRoom(roomId);

        room.whitePlayer().ifPresent(user -> nameDto.setWhiteName(user.name()));
        room.blackPlayer().ifPresent(user -> nameDto.setBlackName(user.name()));

        final String response = objectMapper
            .writeValueAsString(new ResponseForm<>(Form.NEW_USER_NAME, nameDto));

        room.users()
            .forEach(user -> user.sendData(response));
    }

    public void leaveRoom(User user) {
        roomService.leaveRoom(user);
        updateRoomForLobbyUsers(new HashMap<>());
    }

    private RoundStatusDto roundStatus(Long roomId) {
        final ChessGame chessGame = roomService.findRoom(roomId).chessGame();
        List<Piece> pieces = chessGame.currentColorPieces();
        return new RoundStatusDto(
            mapMovablePositions(pieces),
            chessGame.currentColor(),
            chessGame.gameResult(),
            chessGame.isChecked(),
            chessGame.isKingDead()
        );
    }

    private Map<String, List<String>> mapMovablePositions(List<Piece> pieces) {
        return pieces.stream()
            .collect(toMap(
                piece -> piece.currentPosition().columnAndRow(),
                piece -> piece.movablePositions()
                    .stream()
                    .map(Position::columnAndRow)
                    .collect(toList())
            ));
    }

    public void move(Map<String, Object> data, User user) throws Exception {
        PositionDto positionDto = objectMapper.convertValue(data, PositionDto.class);
        final Position currentPosition = positionConverter
            .convert(positionDto.getCurrentPosition());
        final Position targetPosition = positionConverter.convert(positionDto.getTargetPosition());
        final Room room = roomService.findRoom(user.roomId());

        room.chessGame().movePiece(currentPosition, targetPosition);
        final String roundStatus = objectMapper.writeValueAsString(
            new ResponseForm<>(Form.UPDATE_STATUS, roundStatus(user.roomId())));

        String movedResponse = objectMapper
            .writeValueAsString(new ResponseForm<>(Form.MOVE_PIECE, positionDto));

        room.users().stream().filter(roomUser -> !roomUser.equals(user))
            .forEach(roomUser -> roomUser.sendData(movedResponse));

        room.users().forEach(roomUser -> roomUser.sendData(roundStatus));
    }

    public void chat(Map<String, Object> data, User user) throws Exception {
        String message = (String) data.get("message");
        final String messageResponse = objectMapper.writeValueAsString(
            new ResponseForm<>(Form.MESSAGE, new MessageDto(user.name(), message)));

        roomService.findRoom(user.roomId())
            .users()
            .forEach(roomUser -> roomUser.sendData(messageResponse));
    }
}
