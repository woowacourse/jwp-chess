package chess.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.domain.chess.Chess;
import chess.domain.position.MovePosition;
import chess.repository.ChessRepository;
import chess.repository.MoveRepository;

@Service
public class MoveService {
    private final MoveRepository moveRepository;
    private final ChessRepository chessRepository;

    public MoveService(MoveRepository moveRepository, ChessRepository chessRepository) {
        this.moveRepository = moveRepository;
        this.chessRepository = chessRepository;
    }

    @Transactional
    public Chess move(long chessId, MovePosition movePosition) {
        Chess chess = chessRepository.findChessById(chessId).move(movePosition);
        moveRepository.move(chessId, movePosition);
        moveRepository.updateChess(chessId, chess.status(), chess.color());
        return chess;
    }
}
