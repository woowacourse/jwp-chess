package wooteco.chess.domain.entity;

import org.springframework.data.annotation.Id;

public class Piece {
	@Id
	private Long id;
	private String symbol;
	private String position;
	private String team;
}
