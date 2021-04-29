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
        Map<String, String> filteredChessBoard = new LinkedHashMap<>();
        Map<Position, Piece> chessBoard = round.getBoard();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.entrySet()) {
            filterChessBoard(filteredChessBoard, chessBoardEntry);
        }
        chessRepository.initializePieceStatus(filteredChessBoard, roomId);
    }

    private void filterChessBoard(final Map<String, String> filteredChessBoard,
                                  final Map.Entry<Position, Piece> chessBoardEntry) {
        if (chessBoardEntry.getValue() != null) {
            filteredChessBoard.put(chessBoardEntry.getKey().toString(), chessBoardEntry.getValue().getPiece());
        }
    }

    public Map<Position, Piece> chessBoardFromDB(final Long roomId) {
        Round round = loadRoundFromDB(roomId);
        Map<String, String> dbChessBoard = dbChessBoard(roomId);
        return round.getBoard(ChessBoardFactory.loadBoard(dbChessBoard));
    }

    private Map<String, String> dbChessBoard(final Long roomId) {
        return chessRepository.showAllPieces(roomId);
    }

    public Map<Long, String> rooms() {
        return chessRepository.showAllRooms();
    }

    public boolean move(final String source, final String target, final Long roomId) {
        Queue<String> commands =
                new ArrayDeque<>(Arrays.asList("move", source, target));
        Round round = loadRoundFromDB(roomId);
        round.execute(commands);
        movePiece(source, target);
        return true;
    }

    public double whiteScore(final Long roomId) {
        Round round = loadRoundFromDB(roomId);
        return round.getWhitePlayer().calculateScore();
    }

    public double blackScore(final Long roomId) {
        Round round = loadRoundFromDB(roomId);
        return round.getBlackPlayer().calculateScore();
    }

    private Round loadRoundFromDB(final Long roomId) {
        Map<String, String> dbChessBoard = dbChessBoard(roomId);
        String currentTurn = currentTurn(roomId);
        return Round.of(ChessBoardFactory.loadBoard(dbChessBoard), currentTurn);
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
