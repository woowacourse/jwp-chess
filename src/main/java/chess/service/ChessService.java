package chess.service;

import chess.domain.board.ChessBoardFactory;
import chess.domain.command.CommandFactory;
import chess.domain.piece.Piece;
import chess.domain.piece.PiecesFactory;
import chess.domain.player.Round;
import chess.domain.position.Position;
import chess.domain.state.StateFactory;
import chess.repository.ChessRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChessService {
    private final ChessRepository chessRepository;

    public ChessService(final ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public Long addRoom(final String roomName) {
        Round round = new Round(StateFactory.initialization(PiecesFactory.whitePieces()),
                StateFactory.initialization(PiecesFactory.blackPieces()),
                CommandFactory.initialCommand("start"));
        Long roomId = chessRepository.addRoom(roomName);
        initialize(round, roomId);
        return roomId;
    }

    private void initialize(final Round round, final Long roomId) {
        Map<String, String> filteredBoard = new LinkedHashMap<>();
        Map<Position, Piece> board = round.getBoard();
        for (Map.Entry<Position, Piece> boardEntry : board.entrySet()) {
            filterBoard(filteredBoard, boardEntry);
        }
        chessRepository.initializePieceStatus(filteredBoard, roomId);
    }

    private void filterBoard(final Map<String, String> filteredBoard,
                             final Map.Entry<Position, Piece> boardEntry) {
        if (boardEntry.getValue() != null) {
            filteredBoard.put(boardEntry.getKey().toString(), boardEntry.getValue().getPiece());
        }
    }

    public Map<Position, Piece> board(final Long roomId) {
        Round round = round(roomId);
        Map<String, String> pieces = pieces(roomId);
        return round.getBoard(ChessBoardFactory.loadBoard(pieces));
    }

    public Map<Long, String> rooms() {
        return chessRepository.showAllRooms();
    }

    public boolean move(final String source, final String target, final Long roomId) {
        Queue<String> commands =
                new ArrayDeque<>(Arrays.asList("move", source, target));
        Round round = round(roomId);
        round.execute(commands);
        movePiece(source, target);
        return true;
    }

    public double whiteScore(final Long roomId) {
        Round round = round(roomId);
        return round.getWhitePlayer().calculateScore();
    }

    public double blackScore(final Long roomId) {
        Round round = round(roomId);
        return round.getBlackPlayer().calculateScore();
    }

    private Round round(final Long roomId) {
        Map<String, String> pieces = pieces(roomId);
        String currentTurn = currentTurn(roomId);
        return Round.of(ChessBoardFactory.loadBoard(pieces), currentTurn);
    }

    private Map<String, String> pieces(final Long roomId) {
        return chessRepository.showAllPieces(roomId);
    }

    public String currentTurn(final Long roomId) {
        return chessRepository.showCurrentTurn(roomId);
    }

    public void movePiece(final String source, final String target) {
        chessRepository.removePiece(target);
        chessRepository.movePiece(source, target);
    }

    public void changeTurn(final String nextTurn, final Long roomId) {
        chessRepository.changeTurn(nextTurn, roomId);
    }
}
