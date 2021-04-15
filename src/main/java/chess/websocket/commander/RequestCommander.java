package chess.websocket.commander;

import chess.controller.ChessController;
import chess.controller.dto.PieceDto;
import chess.controller.dto.RoundStatusDto;
import chess.dao.ChessDAOSql2o;
import chess.domain.TeamColor;
import chess.service.ChessServiceImpl;
import chess.websocket.ResponseForm;
import chess.websocket.ResponseForm.Form;
import chess.websocket.exception.FullRoomException;
import chess.websocket.util.ChatSender;
import chess.websocket.util.ResourceSender;
import java.util.List;
import java.util.Optional;
import org.springframework.web.socket.WebSocketSession;

public class RequestCommander {

    private ChessRoom chessRoom = new ChessRoom();
    private ChatSender chatSender = new ChatSender();
    ChessController chessController = new ChessController(new ChessServiceImpl(new ChessDAOSql2o()));

    public void enterRoom(String[] contents, WebSocketSession player) {
        try {
            String roomId = contents[2];
            TeamColor teamColor = chessRoom.enter(Long.parseLong(roomId), player);
            ResponseForm<String> response = new ResponseForm<>(Form.COLOR, teamColor.name());
            ResourceSender.send(player, response);
        } catch (FullRoomException e) {
            ResponseForm<String> response = new ResponseForm<>(Form.ERROR, e.getMessage());
            ResourceSender.send(player, response);
        }
    }

    public void leaveRoom(WebSocketSession session) {
        chessRoom.remove(session);
    }

    public void initialPieces(String[] contents, WebSocketSession player) {
        Long gameId = chessRoom.keyBySession(player);
        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(player);

        List<PieceDto> pieces = chessController.startGame(gameId);
        ResponseForm<List<PieceDto>> responseForm = new ResponseForm<>(Form.PIECES, pieces);

        ResourceSender.send(player, responseForm);
        otherPlayer.ifPresent(pl -> ResourceSender.send(pl, responseForm));

        roundStatus(contents, player);
    }

    public void roundStatus(String[] contents, WebSocketSession player) {
        Long gameId = chessRoom.keyBySession(player);
        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(player);

        RoundStatusDto roundStatusDTO = chessController.roundStatus(gameId);
        ResponseForm<RoundStatusDto> roundStatus =
            new ResponseForm<>(Form.STATUS, roundStatusDTO);

        ResourceSender.send(player, roundStatus);
        otherPlayer.ifPresent(pl -> ResourceSender.send(pl, roundStatus));
    }

    public void move(String[] contents, WebSocketSession player) {
        Long gameId = chessRoom.keyBySession(player);
        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(player);

        String currentPosition = contents[2];
        String targetPosition = contents[3];
        chessController.move(gameId, currentPosition, targetPosition);

        ResponseForm<PositionDto> positions =
            new ResponseForm<>(Form.MOVE, new PositionDto(currentPosition, targetPosition));

        otherPlayer.ifPresent(pl -> ResourceSender.send(pl, positions));
        roundStatus(contents, player);
    }


    public void sendMessage(WebSocketSession user, String message) {
        chatSender.sendMessage(user, "나", message);
        Optional<WebSocketSession> otherPlayer = chessRoom.otherPlayer(user);
        otherPlayer.ifPresent(pl -> chatSender.sendMessage(pl, "상대방", message));
    }
}
