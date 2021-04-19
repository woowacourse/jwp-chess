package chess.controller;

import chess.controller.dto.RoomDto;
import chess.service.GameService;
import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    private final GameService gameService;

    public RoomController(final RoomService roomService, final GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/list")
    public String loadRoomList(final Model model) {
        model.addAttribute("list", roomService.loadList());
        return "mainPage";
    }

    @PostMapping("/create")
    public String createRoom(@Valid final RoomDto roomDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("방 길이는 3 ~ 12 글자 입니다.");
        }

        final Long roomId = roomService.save(roomDto.getRoomName());
        return "redirect:/game/load/" + roomId;
    }

    @PostMapping("/delete")
    public String deleteRoom(final Long roomId) {
        roomService.delete(roomId);
        return "redirect:/room/list";
    }
}