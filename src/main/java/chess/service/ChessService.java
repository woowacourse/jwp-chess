package chess.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.chess.Chess;
import chess.domain.position.MovePosition;
import chess.repository.ChessRepository;
import chess.repository.MoveRepository;

@Service
public class ChessService {

    private final ChessRepository chessRepository;
    private final MoveRepository moveRepository;

    public ChessService(ChessRepository chessRepository, MoveRepository moveRepository) {
        this.chessRepository = chessRepository;
        this.moveRepository = moveRepository;
    }

    public Chess findChessById(long chessId) {
        return chessRepository.findChessById(chessId);
    }

    @Transactional
    public Chess move(long chessId, MovePosition movePosition) {
        Chess chess = findChessById(chessId).move(movePosition);
        moveRepository.move(chessId, movePosition);
        moveRepository.updateChess(chessId, chess.status(), chess.color());
        return chess;
    }
}
