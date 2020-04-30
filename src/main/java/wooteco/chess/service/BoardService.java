package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
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
        System.out.println("전략패턴");
        Board board = new Board(normalInitStrategy.init());
        System.out.println("초기화");
        ChessEntity chessEntity = new ChessEntity(roomId, BoardConverter.convertToString(board));
        System.out.println("저장된 엔티티 정보");
        chessRepository.save(chessEntity);
    }

//    public void play(int roomId, String source, String target) throws SQLException {
//        Board board = chessRepository.load(roomId);
//        board.moveIfPossible(Position.of(source), Position.of(target));
//        chessRepository.save(roomId, board);
//    }
//
//    public boolean isFinished(int roomId) throws SQLException {
//        Board board = chessRepository.load(roomId);
//        return board.isFinished();
//    }
//
//    public boolean isTurnWhite(int roomId) throws SQLException {
//        Board board = chessRepository.load(roomId);
//        return board.getTurn() == Team.WHITE;
//    }

    public Board loadBoard(Long roomId) {
        ChessEntity entity = chessRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다"));
        System.out.println("보드 로딩");
        return BoardConverter.convertToBoard(entity.getBoard(), entity.getIsWhite());
    }
}
