package wooteco.chess.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import wooteco.chess.dto.AuthorizeDto;
import wooteco.chess.dto.RoomRequestDto;
import wooteco.chess.dto.RoomResponseDto;
import wooteco.chess.service.SpringRoomService;

@Controller
@RequestMapping("/rooms")
public class SpringRoomController {

    private final SpringRoomService roomService;

    private SpringRoomController(final SpringRoomService roomService) {
        this.roomService = roomService;
    }

    // TODO: 2020/04/30 Room 목록을 동기방식이 아닌 비동기 방식으로 수정해야됨
    @GetMapping
    public String getAllRooms(Model model) {
        List<RoomResponseDto> rooms = roomService.findAllRoom();
        model.addAttribute("rooms", rooms);

        return "index";
    }

    @GetMapping("/enter/{id}")
    public String enterRoom(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "game";
    }

    @PostMapping("/create")
    public String createRoom(@Valid RoomRequestDto roomRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("nameError", bindingResult.getFieldError("name"));
            model.addAttribute("passwordError", bindingResult.getFieldError("password"));
            return getAllRooms(model);
        }
        roomService.addRoom(roomRequestDto);
        return "redirect:/rooms";
    }

    @DeleteMapping("/remove/{id}")
    public void removeRoom(@PathVariable Long id) {
        roomService.removeRoom(id);
    }

    @PostMapping("/authorize")
    @ResponseBody
    public boolean authorize(@Valid @RequestBody AuthorizeDto authorizeDto) {
        return roomService.authorize(authorizeDto.getId(), authorizeDto.getPassword());
    }
}
