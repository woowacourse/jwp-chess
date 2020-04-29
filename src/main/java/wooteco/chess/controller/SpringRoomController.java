package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.dto.AuthorizeDto;
import wooteco.chess.dto.RoomRequestDto;
import wooteco.chess.dto.RoomResponseDto;
import wooteco.chess.service.SpringRoomService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

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

    @GetMapping("/enter")
    public String enterRoom(@RequestParam(value = "roomId") Integer roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "game";
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

    @GetMapping("/remove")
    public String removeRoom(@RequestParam(value = "roomId") RoomResponseDto roomResponseDto) throws SQLException {
        roomService.removeRoom(roomResponseDto);
        return "redirect:/rooms";
    }

    @PostMapping("/authorize")
    @ResponseBody
    public boolean authorize(@Valid @RequestBody AuthorizeDto authorizeDto, Errors errors) {
//        ModelAndView model = new ModelAndView("index");
//
//        if(errors.hasErrors()){
//            model.addObject("errors", errors);
//            return model;
//        }
        return roomService.authorize(authorizeDto.getId(), authorizeDto.getPassword());
//        model.addObject("result", result);
//        if (!result) {
//            errors.rejectValue("password", "wrongPassword", "wrong Password");
//            model.addObject("authorizeError", errors.getFieldValue("password"));
//        }
//        return model;

    }
}
