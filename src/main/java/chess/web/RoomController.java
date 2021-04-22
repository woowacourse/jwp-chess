package chess.web;

import chess.dto.RoomRequestDto;
import chess.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class RoomController {
    private final RoomService service;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rooms", service.findAllDesc());
        return "index";
    }

    @DeleteMapping("/rooms/{id}")
    public int delete(@PathVariable int id) {
        service.delete(id);
        return id;
    }

    @PostMapping("/rooms")
    public String add(@RequestBody RoomRequestDto dto){
        service.add(dto.getName());
        return "redirect:/";
    }

/*    @GetMapping("/rooms/{id}")
    public String startGame(@PathVariable int id, Model model) {
        BoardDto dto = service.findBoardById(id);
    }*/
}
