package wooteco.chess.dto.response;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.GameResult;
import wooteco.chess.domain.board.ChessBoard;
import wooteco.chess.dto.CellManager;

public class ChessGameParser {
	private final ChessBoard chessBoard;
	private final Long roomId;

	public ChessGameParser(ChessBoard chessBoard, Long roomId) {
		this.chessBoard = chessBoard;
		this.roomId = roomId;
	}

	public Map<String, Object> parseModel() {
		Map<String, Object> model = new HashMap<>();
		GameResult gameResult = chessBoard.createGameResult();
		CellManager cellManager = new CellManager();

		model.put("cells", cellManager.createCells(chessBoard));
		model.put("currentTeam", chessBoard.getTeam().getName());
		model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());
		model.put("roomId", roomId);

		return model;
	}
}
