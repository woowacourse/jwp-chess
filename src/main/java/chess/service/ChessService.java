package chess.service;

import chess.domain.command.Command;
import chess.domain.command.CommandFactory;
import chess.domain.piece.Piece;
import chess.domain.piece.PiecesFactory;
import chess.domain.player.Round;
import chess.domain.position.Position;
import chess.domain.state.State;
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

    public int makeRoom() {
        State whiteState = StateFactory.initialization(PiecesFactory.whitePieces());
        State blackState = StateFactory.initialization(PiecesFactory.blackPieces());
        Command start = CommandFactory.initialCommand("start");
        Round round = new Round(whiteState, blackState, start);

        return chessRepository.makeRoom(filteredChessBoard(round.getBoard()));
    }

    public Map<String, String> filteredChessBoard(final Map<Position, Piece> chessBoard) {
        Map<String, String> filteredChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.entrySet()) {
            if (chessBoardEntry.getValue() != null) {
                filteredChessBoard.put(chessBoardEntry.getKey().toString(),
                        chessBoardEntry.getValue().getPiece());
            }
        }
        return filteredChessBoard;
    }

    public Round getStoredRound(int roomId) {
        Map<Position, Piece> board = chessRepository.getBoardByRoomId(roomId);
        String currentTurn = chessRepository.getCurrentTurnByRoomId(roomId);
        return new Round(board, currentTurn);
    }

    public void move(String source, String target, int roomId) {
        Queue<String> commands = new ArrayDeque<>(Arrays.asList("move", source, target));
        Round round = getStoredRound(roomId);
        round.execute(commands);
        movePiece(source, target, roomId);
    }

    public void movePiece(String source, String target, int roomId) {
        chessRepository.removePiece(target, roomId);
        chessRepository.movePiece(source, target, roomId);
    }

    public void changeTurn(String nextTurn, String currentTurn, int roomId) {
        chessRepository.changeTurn(nextTurn, currentTurn, roomId);
    }

    public Double getWhiteScore(int roomId) {
        Round round = getStoredRound(roomId);
        return round.getWhitePlayer().calculateScore();
    }

    public Double getBlackScore(int roomId) {
        Round round = getStoredRound(roomId);
        return round.getBlackPlayer().calculateScore();
    }
}
