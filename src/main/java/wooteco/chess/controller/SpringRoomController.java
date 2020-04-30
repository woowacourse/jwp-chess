package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wooteco.chess.dto.AuthorizeDto;
import wooteco.chess.dto.LoginRequest;
import wooteco.chess.dto.RoomRequestDto;
import wooteco.chess.dto.RoomResponseDto;
import wooteco.chess.service.SpringRoomService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/rooms")
public class SpringRoomController {

    private final SpringRoomService roomService;

    private SpringRoomController(final SpringRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String getAllRooms(Model model) throws SQLException {
        List<RoomResponseDto> rooms = roomService.findAllRoom();
        model.addAttribute("rooms", rooms);

        return "index";
    }

    @PostMapping("/enter")
    public String enterRoom(LoginRequest loginRequest,
                            Model model, RedirectAttributes redirectAttributes) throws SQLException {
        if(loginRequest.getLoginSuccess()) {
            model.addAttribute("roomId", loginRequest.getId());
            return "game";
        }
        redirectAttributes.addFlashAttribute("authorizeError", "wrongPassword");
        return "redirect:/rooms";
    }

    // TODO: 2020/04/22 valid 에러페이지 이동 문제
    @PostMapping("/create")
    public String createRoom(@Valid RoomRequestDto roomRequestDto, BindingResult bindingResult, Model model) throws SQLException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("nameError", bindingResult.getFieldError("name"));
            model.addAttribute("passwordError", bindingResult.getFieldError("password"));
            return getAllRooms(model);
        }
        roomService.addRoom(roomRequestDto);
        return "redirect:/rooms";
    }

    @PostMapping("/remove")
    public String removeRoom(LoginRequest loginRequest, Model model, RedirectAttributes redirectAttributes) throws SQLException {
        if(loginRequest.getLoginSuccess()){
            roomService.removeRoom(loginRequest.getId());
            return "redirect:/rooms";
        }
        redirectAttributes.addFlashAttribute("authorizeError", "wrongPassword");
        return "redirect:/rooms";
    }

    @PostMapping("/authorize")
    @ResponseBody
    public boolean authorize(@Valid @RequestBody AuthorizeDto authorizeDto, Errors errors) {
        if(Objects.nonNull(authorizeDto.getPassword())) {
            return roomService.authorize(authorizeDto.getId(), authorizeDto.getPassword());
        }
        return false;
    }
}
