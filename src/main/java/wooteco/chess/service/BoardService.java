package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.strategy.NormalInitStrategy;
import wooteco.chess.repository.ChessEntity;
import wooteco.chess.repository.ChessRepository;
import wooteco.chess.utils.BoardConverter;

@Service
public class BoardService {
    private final ChessRepository chessRepository;

    public BoardService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public void init(Long roomId) {
        NormalInitStrategy normalInitStrategy = new NormalInitStrategy();
        Board board = new Board(normalInitStrategy.init());
        ChessEntity chessEntity = new ChessEntity(roomId, BoardConverter.convertToString(board));
        chessRepository.save(chessEntity);
    }

    public void play(Long roomId, String source, String target) {
        ChessEntity entity = chessRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다."));
        Board board = BoardConverter.convertToBoard(entity.getBoard(), entity.getIsWhite());
        board.moveIfPossible(Position.of(source), Position.of(target));

        chessRepository.update(entity.getRoomId(), BoardConverter.convertToString(board), board.isTurnWhite());
    }

    public boolean isFinished(Long roomId) {
        ChessEntity entity = chessRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다."));
        Board board = BoardConverter.convertToBoard(entity.getBoard(), entity.getIsWhite());
        return board.isFinished();
    }

    public boolean isTurnWhite(Long roomId) {
        ChessEntity entity = chessRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다."));
        Board board = BoardConverter.convertToBoard(entity.getBoard(), entity.getIsWhite());
        return board.getTurn() == Team.WHITE;
    }

    public Board loadBoard(Long roomId) {
        ChessEntity entity = chessRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다."));
        System.out.println("보드 로딩");
        return BoardConverter.convertToBoard(entity.getBoard(), entity.getIsWhite());
    }
}
