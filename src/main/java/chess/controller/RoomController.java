package chess.controller;

import chess.service.CommandService;
import chess.service.RoomService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoomController {

    private final RoomService roomService;
    private final CommandService commandService;

    public RoomController(RoomService roomService, CommandService commandService) {
        this.roomService = roomService;
        this.commandService = commandService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("rooms", roomService.findAllRooms());
        return modelAndView;
    }

    @GetMapping("/room/create")
    public String createRoomPage() {
        return "createRoom";
    }

    @PostMapping("/room")
    public String createRoom(@RequestParam String name, @RequestParam String password) {
        roomService.create(name, password);
        return "redirect:/";
    }

    @GetMapping("/room/{id}/delete")
    public ModelAndView deleteRoomPage(@PathVariable Long id, @RequestParam(required = false) String failMessage) {
        ModelAndView modelAndView = new ModelAndView("deleteRoom");
        modelAndView.addObject("id", id);
        modelAndView.addObject("failMessage", failMessage);
        if (commandService.getCurrentState(commandService.findAllByRoomID(id)).isRunning()) {
            modelAndView.setViewName("index");
            modelAndView.addObject("message", "아직 게임 중인 방입니다.");
            modelAndView.addObject("rooms", roomService.findAllRooms());
        }
        return modelAndView;
    }

    @PostMapping("/room/{id}")
    public String deleteRoom(RedirectAttributes redirectAttributes, @PathVariable Long id, @RequestParam String password) {
        if (roomService.delete(id, password)) {
            return "redirect:/";
        }
        redirectAttributes.addAttribute("failMessage", "다시 입력해주세요.");
        return "redirect:/room/" + id + "/delete";
    }
}
