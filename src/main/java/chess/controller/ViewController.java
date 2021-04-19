package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ViewController {
    @GetMapping("/")
    public ModelAndView mainView() {
        System.out.println("roomList get method enter");
        ModelAndView mv = new ModelAndView();
        Map<String, String> map = new HashMap<>();
        map.put("asdf", "adsf");
        map.put("zxcv", "zxcv");
        map.put("qewr", "qwer");
        mv.setViewName("roomList");
        mv.addObject("roomList", map);
        return mv;
    }

    @PostMapping("/room")
    public String roomList(Model model, @RequestParam("roomName") String roomName) {
        System.out.println("roomList post method enter");
        Map<String, String> map = new HashMap<>();
        map.put("roo", roomName);
        model.addAttribute("roomList", map);
        return "roomList";
    }
}
