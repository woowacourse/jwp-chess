package chess.websocket.commander;

import chess.controller.dto.RoomRequestDto;
import chess.domain.TeamColor;
import chess.domain.room.User;
import chess.service.RoomService;
import chess.websocket.ResponseForm;
import chess.websocket.ResponseForm.Form;
import chess.websocket.commander.dto.EnterRoomRequestDto;
import chess.websocket.commander.dto.RoomResponseDto;
import chess.websocket.domain.Lobby;
import chess.websocket.util.ChatSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RequestCommander {

    private final ChatSender chatSender = new ChatSender();

    private final RoomService roomService;
    private final ObjectMapper objectMapper;
    private final Lobby lobby;

    public RequestCommander(RoomService roomService,
        ObjectMapper objectMapper, Lobby lobby) {
        this.roomService = roomService;
        this.objectMapper = objectMapper;
        this.lobby = lobby;
    }

    public void enterLobby(User user) {
        lobby.enter(user);
    }

    public void leaveLobby(User user) {
        lobby.leave(user);
    }

    public void createRoom(Map<String, Object> data, User user) throws Exception {
        RoomRequestDto roomRequestDto = objectMapper.convertValue(data, RoomRequestDto.class);
        System.out.println(roomRequestDto);
        System.out.println(roomRequestDto.getTitle());
        System.out.println(roomRequestDto.isLocked());
        System.out.println(roomRequestDto.getPassword());
        roomService.createRoom(
            roomRequestDto.getTitle(),
            roomRequestDto.isLocked(),
            roomRequestDto.getPassword(),
            user
        );

        lobby.users().forEach(lobbyUser -> {
            try {
                updateRoom(data, lobbyUser);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        // TODO: 자동 들어가는 로직 처리
        updateRoom(data, user);
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
            enterRoomRequestDto.getPassword(), TeamColor.BLACK, user);

    }


    private void updateRoomPage() {

    }
//    public void enterRoom(String[] contents, WebSocketSession player) {
//        try {
//            String roomId = contents[2];
//            TeamColor teamColor = chessRoom.enter(Long.parseLong(roomId), player);
//            ResponseForm<String> response = new ResponseForm<>(Form.COLOR, teamColor.name());
//            ResourceSender.send(player, response);
//        } catch (FullRoomException e) {
//            ResponseForm<String> response = new ResponseForm<>(Form.ERROR, e.getMessage());
//            ResourceSender.send(player, response);
//        }
//    }
//
//    public void leaveRoom(WebSocketSession session) {
//        chessRoom.remove(session);
//    }
//
//    public void initialPieces(String[] contents, WebSocketSession player) {
//        Long gameId = chessRoom.keyBySession(player);
//        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(player);
//
//        List<PieceDto> pieces = chessController.startGame(gameId);
//        ResponseForm<List<PieceDto>> responseForm = new ResponseForm<>(Form.PIECES, pieces);
//
//        ResourceSender.send(player, responseForm);
//        otherPlayer.ifPresent(pl -> ResourceSender.send(pl, responseForm));
//
//        roundStatus(contents, player);
//    }
//
//    public void roundStatus(String[] contents, WebSocketSession player) {
//        Long gameId = chessRoom.keyBySession(player);
//        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(player);
//
//        RoundStatusDto roundStatusDTO = chessController.roundStatus(gameId);
//        ResponseForm<RoundStatusDto> roundStatus =
//            new ResponseForm<>(Form.STATUS, roundStatusDTO);
//
//        ResourceSender.send(player, roundStatus);
//        otherPlayer.ifPresent(pl -> ResourceSender.send(pl, roundStatus));
//    }
//
//    public void move(String[] contents, WebSocketSession player) {
//        Long gameId = chessRoom.keyBySession(player);
//        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(player);
//
//        String currentPosition = contents[2];
//        String targetPosition = contents[3];
//        chessController.move(gameId, currentPosition, targetPosition);
//
//        ResponseForm<PositionDto> positions =
//            new ResponseForm<>(Form.MOVE, new PositionDto(currentPosition, targetPosition));
//
//        otherPlayer.ifPresent(pl -> ResourceSender.send(pl, positions));
//        roundStatus(contents, player);
//    }
//
//
//    public void sendMessage(WebSocketSession user, String message) {
//        chatSender.sendMessage(user, "나", message);
//        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(user);
//        otherPlayer.ifPresent(pl -> chatSender.sendMessage(pl, "상대방", message));
//    }
}
