package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.GameResult;
import wooteco.chess.domain.repository.BoardEntity;
import wooteco.chess.domain.repository.BoardRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private RoomService roomService;

    public BoardService(BoardRepository boardRepository, RoomService roomService) {
        this.boardRepository = boardRepository;
        this.roomService = roomService;
    }

    public Board movePiece(final Long roomId, final String fromPosition, final String toPosition) throws IllegalArgumentException {
        List<BoardEntity> boardEntities = boardRepository.findByRoomId(roomId);
        Board board = new Board(createLoadedBoard(boardEntities));
        Piece piece = board.findBy(Position.of(fromPosition));

        if (piece.isNotSameTeam(Team.of(roomService.findTurnById(roomId)))) {
            throw new IllegalArgumentException("체스 게임 순서를 지켜주세요.");
        }

        if (board.isMovable(fromPosition, toPosition)) {
            BoardEntity fromBoardEntity = boardRepository.findByRoomIdAndPosition(roomId, fromPosition);
            fromBoardEntity.setPiece(PieceType.BLANK.name());
            boardRepository.save(fromBoardEntity);

            BoardEntity toBoardEntity = boardRepository.findByRoomIdAndPosition(roomId, toPosition);
            toBoardEntity.setPiece(piece.getNextPiece().getName());
            boardRepository.save(toBoardEntity);
        }
        roomService.updateTurn(roomId);
        return board;
    }

    public Map<String, String> showScoreStatus(final Long roomId) {
        GameResult gameResult = new GameResult();
        List<BoardEntity> boardEntities = boardRepository.findByRoomId(roomId);
        Board board = new Board(createLoadedBoard(boardEntities));

        double blackScore = gameResult.calculateScore(board, Team.BLACK);
        double whiteScore = gameResult.calculateScore(board, Team.WHITE);

        Map<String, String> model = new HashMap<>();
        model.put("black", String.valueOf(blackScore));
        model.put("white", String.valueOf(whiteScore));
        return model;
    }

    public String receiveWinner(final Long roomId) {
        roomService.updateTurn(roomId);
        return roomService.findTurnById(roomId);
    }

    public boolean isFinish(final Board board) {
        return board.isFinished();
    }

    private Map<Position, Piece> createLoadedBoard(final List<BoardEntity> boardEntities) {
        Map<Position, Piece> board = new HashMap<>();
        for (BoardEntity boardEntity : boardEntities) {
            board.put(Position.of(boardEntity.getPosition()),
                    Piece.of(PieceType.valueOf(boardEntity.getPiece())));
        }
        return board;
    }
}
