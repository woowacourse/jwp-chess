package chess;

import chess.dto.requestdto.MoveRequestDto;
import chess.dto.requestdto.StartRequestDto;
import chess.dto.response.Response;
import chess.dto.response.ResponseCode;
import chess.dto.responsedto.GridAndPiecesResponseDto;
import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@SpringBootApplication
@RestController
public class SpringChessApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(SpringChessApplication.class, args);
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private ChessService chessService;

	@Override
	public void run(String... strings) {
		chessService = new ChessService(jdbcTemplate);
	}

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@PostMapping("/move")
	public Response move(@RequestBody MoveRequestDto moveRequestDto) throws SQLException {
		return chessService.move(moveRequestDto);
	}

	@GetMapping("/grid/{roomName}")
	public Response getRoom(@PathVariable("roomName") String roomName) {
		StartRequestDto startRequestDto = new StartRequestDto(roomName);
		GridAndPiecesResponseDto gridAndPiecesResponseDto = chessService.getGridAndPieces(startRequestDto);
		return new Response(ResponseCode.OK, gridAndPiecesResponseDto);
	}

	@PostMapping("/grid/{gridId}/start")
	public Response start(@PathVariable("gridId") String gridId) throws SQLException {
		chessService.start(Long.parseLong(gridId));
		return new Response(ResponseCode.NO_CONTENT);
	}

	@PostMapping("/grid/{gridId}/finish")
	public Response finish(@PathVariable("gridId") String gridId) throws SQLException {
		chessService.finish(Long.parseLong(gridId));
		return new Response(ResponseCode.NO_CONTENT);
	}

	@GetMapping("/room/{roomId}/restart")
	public Response restart(@PathVariable("roomId") String roomId) throws SQLException {
		return new Response(ResponseCode.OK, chessService.restart(Long.parseLong(roomId)));
	}

	@GetMapping("/room")
	public Response getRooms() {
		return new Response(ResponseCode.OK, chessService.getAllRooms());
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
