package wooteco.chess.domain.entity;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Game {
	@Id
	private Long id;
	private String turn;
	private List<Piece> pieces;
}
