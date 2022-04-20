package springchess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import springchess.model.room.Room;
import springchess.service.ChessService;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rooms", chessService.getRooms());
        return "home";
    }

    @PostMapping("/room")
    public String room(@RequestBody String messageBody) {
        final List<String> createRoomInput = Arrays.stream(messageBody.strip().split("\n"))
                .map(s -> s.split("=")[1])
                .collect(Collectors.toList());

        Room room = chessService.init(
                createRoomInput.get(0),
                createRoomInput.get(1),
                createRoomInput.get(2));

        System.out.println(messageBody);
        return "redirect:/" + room.getId();
    }
}
