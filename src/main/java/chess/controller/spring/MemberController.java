package chess.controller.spring;

import chess.domain.Member;
import chess.dto.GameResultDto;
import chess.service.GameService;
import chess.service.MemberService;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final GameService gameService;

    @Autowired
    public MemberController(final MemberService memberService, final GameService gameService) {
        this.memberService = memberService;
        this.gameService = gameService;
    }

    @GetMapping("/{memberId}/history")
    public ModelAndView renderMemberHistory(@PathVariable("memberId") final Long memberId) {
        final List<GameResultDto> history = gameService.findHistoriesByMemberId(memberId);
        final Map<String, Object> model = new HashMap<>();
        model.put("history", history);
        return new ModelAndView("history", model);
    }

    @GetMapping("/management")
    public ModelAndView renderMemberManagement() {
        final List<Member> members = memberService.findAllMembers();
        final Map<String, Object> model = new HashMap<>();
        model.put("members", members);
        return new ModelAndView("member-management", model);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Long> addMember(@RequestBody final Member member) {
        final Long memberId = memberService.addMember(member.getName());
        return ResponseEntity.created(URI.create("/members/" + memberId)).body(memberId);
    }

    @GetMapping("/{memberId}")
    @ResponseBody
    public ResponseEntity<Member> getMember(@PathVariable("memberId") final Long memberId) {
        return ResponseEntity.ok().body(memberService.findById(memberId));
    }

    @DeleteMapping("/{memberId}")
    @ResponseBody
    public ResponseEntity<Long> deleteMember(@PathVariable final Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok().body(memberId);
    }
}
