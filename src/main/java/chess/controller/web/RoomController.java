package chess.controller.web;

import chess.controller.web.dto.room.RoomResponseDto;
import chess.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final ModelMapper modelMapper;

    public RoomController(RoomService roomService, ModelMapper modelMapper) {
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/rooms")
    public String rooms() {
        return "roomList";
    }

    @GetMapping("/rooms/{id}")
    public String findRoomById(@PathVariable Long id, Model model) {
        RoomResponseDto roomResponseDto = modelMapper.map(roomService.findById(id), RoomResponseDto.class);
        model.addAttribute(roomResponseDto);
        return "board";
    }
}
