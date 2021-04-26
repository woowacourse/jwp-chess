package chess.web;

import chess.dto.RoomRequestDto;
import chess.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/rooms")
@Controller
public class RoomController {

    private final RoomService service;

    @PostMapping
    public String add(@RequestBody RoomRequestDto dto) {
        service.add(dto.getName());
        return "redirect:/";
    }

    @PostMapping("/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);
        return "redirect:/";
    }
}
