package chess.controller;

import chess.controller.dto.RoomDto;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/list")
    public String loadRoomList(final Model model) {
        model.addAttribute("list", roomService.loadList());
        return "mainPage";
    }

    @PostMapping("/create")
    public String createRoom(@Valid final RoomDto roomDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final String errorMsg = OutputView.getErrorMessage(bindingResult.getFieldErrors());
            throw new IllegalArgumentException(errorMsg);
        }

        final Long roomId = roomService.save(roomDto.getRoomName());
        return "redirect:/game/load/" + roomId;
    }

    @ResponseBody
    @DeleteMapping
    public void deleteRoom(final long roomId) {
        roomService.delete(roomId);
    }
}