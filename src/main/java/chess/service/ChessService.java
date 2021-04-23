package chess.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.chess.Chess;
import chess.repository.ChessRepository;

@Transactional
@Service
public class ChessService {

    private final ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public Chess findChessById(long chessId) {
        return chessRepository.findChessById(chessId);
    }
}
