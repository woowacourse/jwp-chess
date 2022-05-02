package chess.controller;

import chess.service.CommandService;
import chess.service.RoomService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping
    public String index(Model model) {
        model.addAttribute("rooms", roomService.findAllRooms());
        return "index";
    }

    @GetMapping("/form/room-create")
    public String createRoomPage() {
        return "createRoom";
    }

    @PostMapping("/room")
    public String createRoom(@RequestParam String name, @RequestParam String password) {
        roomService.create(name, password);
        return "redirect:/";
    }

    @GetMapping("/form/room-delete/{roomId}")
    public ModelAndView deleteRoomPage(@PathVariable Long roomId, @RequestParam(required = false) String failMessage) {
        ModelAndView modelAndView = new ModelAndView("deleteRoom");
        modelAndView.addObject("id", roomId);
        modelAndView.addObject("failMessage", failMessage);
        if (commandService.getCurrentState(commandService.findAllByRoomID(roomId)).isRunning()) {
            modelAndView.setViewName("index");
            modelAndView.addObject("message", "아직 게임 중인 방입니다.");
            modelAndView.addObject("rooms", roomService.findAllRooms());
        }
        return modelAndView;
    }

    @PostMapping("/room/{roomId}")
    public String deleteRoom(RedirectAttributes redirectAttributes, @PathVariable Long roomId, @RequestParam String password) {
        if (roomService.delete(roomId, password)) {
            return "redirect:/";
        }
        redirectAttributes.addAttribute("failMessage", "다시 입력해주세요.");
        return "redirect:/room/" + roomId + "/delete";
    }
}
