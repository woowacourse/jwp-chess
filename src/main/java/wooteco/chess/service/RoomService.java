package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.strategy.NormalInitStrategy;
import wooteco.chess.repository.ChessEntity;
import wooteco.chess.repository.ChessRepository;
import wooteco.chess.utils.BoardConverter;

import java.util.List;

@Service
public class RoomService {
    private final ChessRepository chessRepository;

    public RoomService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public List<Long> loadRoomNumbers() {
        return chessRepository.findIds();
    }

    public Iterable<ChessEntity> loadRoomInformation() {
        return chessRepository.findAll();
    }

    public Long create(String title) {
        NormalInitStrategy strategy = new NormalInitStrategy();
        Board board = new Board(strategy.init());

        ChessEntity entity = chessRepository.save(
                new ChessEntity(title, BoardConverter.convertToString(board), board.isTurnWhite()));
        return entity.getRoomId();
    }
}
