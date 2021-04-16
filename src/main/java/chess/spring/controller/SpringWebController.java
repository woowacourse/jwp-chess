//package chess.spring.controller;
//
//
//import chess.spring.controller.dto.request.MoveRequestDTO;
//import chess.spring.controller.dto.response.BoardResponseDTO;
//import chess.spring.controller.dto.response.ChessGameResponseDTO;
//import chess.spring.controller.dto.response.MoveResponseDTO;
//import chess.spring.controller.dto.response.ResponseDTO;
//import chess.web.service.ChessWebService;
//import java.sql.SQLException;
//import java.util.List;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//
//@Controller
//public class SpringWebController {
//
//    private final ChessWebService chessWebService;
//
//    public SpringWebController(ChessWebService chessWebService) {
//        this.chessWebService = chessWebService;
//    }
//
//    @GetMapping("/")
//    public String home(Model model) throws SQLException {
//        List<ChessGameResponseDTO> allRoomsIdAndTitle = chessWebService.getAllRoomsIdAndTitle();
//        model.addAttribute("allChessGameRooms", allRoomsIdAndTitle);
//        return "index";
//    }
//
//    @PostMapping("/rooms")
//    public String createChessRoomRequest(@RequestParam("room-title") String roomTitle) throws SQLException {
//        Long createdChessGameId = chessWebService.createNewChessGame(roomTitle);
//        return "redirect:/rooms?id=" + createdChessGameId;
//    }
//
//    @GetMapping("/rooms")
//    public String getChessBoardRequest(@RequestParam("id") Long gameId, Model model) throws SQLException {
//        ResponseDTO responseDTO = chessWebService.getGameStatus(gameId);
//        model.addAttribute("responseDTO", responseDTO);
//        putBoardRanksToModel(model, responseDTO.getBoardResponseDTO());
//        return "chess-board";
//    }
//
//    private void putBoardRanksToModel(Model model, BoardResponseDTO boardResponseDTO) {
//        model.addAttribute("rank8", boardResponseDTO.getRank8());
//        model.addAttribute("rank7", boardResponseDTO.getRank7());
//        model.addAttribute("rank6", boardResponseDTO.getRank6());
//        model.addAttribute("rank5", boardResponseDTO.getRank5());
//        model.addAttribute("rank4", boardResponseDTO.getRank4());
//        model.addAttribute("rank3", boardResponseDTO.getRank3());
//        model.addAttribute("rank2", boardResponseDTO.getRank2());
//        model.addAttribute("rank1", boardResponseDTO.getRank1());
//    }
//
//    @PostMapping("/move")
//    @ResponseBody
//    public MoveResponseDTO moveRequest(@RequestBody MoveRequestDTO moveRequestDTO) throws SQLException {
//        return chessWebService.requestMove(moveRequestDTO);
//    }
//
//    @GetMapping("/delete")
//    public String deleteRequest(@RequestParam("id") Long gameId) throws SQLException {
//        chessWebService.deleteGame(gameId);
//        return "redirect:/";
//    }
//}
