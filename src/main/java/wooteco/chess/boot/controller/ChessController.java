package wooteco.chess.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import wooteco.chess.boot.entity.RoomEntity;
import wooteco.chess.boot.repository.RoomRepository;
import wooteco.chess.boot.service.ChessService;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.util.ModelParser;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class ChessController {
    private static final String INDEX = "boot_index";
    private static final String BOARD = "boot_board";

    @Autowired
    private ChessService chessService;

    @Autowired
    RoomRepository roomRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<RoomEntity> allRooms = roomRepository.findAll();
        model.addAttribute("rooms", allRooms);
        return INDEX;
    }

    @PostMapping("/remove")
    public String remove(ServletRequest request, Model model) {
        roomRepository.deleteByRoomNumber(findRoomNumber(request));

        List<RoomEntity> allRooms = roomRepository.findAll();
        model.addAttribute("rooms", allRooms);
        return INDEX;
    }

    private Long findRoomNumber(ServletRequest request) {
        String roomNumber = request.getParameter("room_number");
        return Long.parseLong(roomNumber);
    }

    @PostMapping("/new_game")
    public String newGame(Model model) {
        Long roomNumber = chessService.newGame();
        Board board = chessService.readBoard(roomNumber);
        model.addAllAttributes(ModelParser.parseBoard(board, roomNumber));
        return BOARD;
    }

    @GetMapping("/load_game")
    public String loadGame(ServletRequest request, Model model) {
        Long roomNumber = findRoomNumber(request);
        Board board = chessService.readBoard(roomNumber);
        model.addAllAttributes(ModelParser.parseBoard(board, roomNumber));
        return BOARD;
    }

    @GetMapping("/show_movable")
    public String showMovable(ServletRequest request, Model model) {
        String start = request.getParameter("start");
        model.addAllAttributes(parseMovablePositionsModel(findRoomNumber(request), start));
        return BOARD;
    }

    private Map<String, Object> parseMovablePositionsModel(Long roomNumber, String start) {
        List<Position> movablePositions = chessService.findMovablePlaces(roomNumber, Position.of(start));
        Map<String, Object> objects = ModelParser.parseBoard(chessService.readBoard(roomNumber), movablePositions, roomNumber);

        if (movablePositions.size() != 0) {
            objects.put("start", start);
        }
        return objects;
    }

    @PostMapping("/move")
    public String move(ServletRequest request, Model model) {
        Long roomNumber = findRoomNumber(request);
        String start = request.getParameter("start");
        String end = request.getParameter("end");

        chessService.move(roomNumber, Position.of(start), Position.of(end));
        Board board = chessService.readBoard(roomNumber);
        model.addAllAttributes(ModelParser.parseBoard(board, roomNumber));
        return BOARD;
    }

    @GetMapping("/score")
    public String score(ServletRequest request, Model model) {
        Long roomNumber = findRoomNumber(request);
        Board board = chessService.readBoard(roomNumber);

        model.addAllAttributes(ModelParser.parseBoard(board, roomNumber));
        model.addAttribute("player1_info", "WHITE: " + chessService.calculateScore(roomNumber, Team.WHITE));
        model.addAttribute("player2_info", "BLACK: " + chessService.calculateScore(roomNumber, Team.BLACK));
        return BOARD;
    }
}
