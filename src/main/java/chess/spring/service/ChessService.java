package chess.spring.service;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardGenerator;
import chess.domain.board.Coordinate;
import chess.domain.history.Histories;
import chess.domain.piece.TeamType;
import chess.domain.result.Result;
import chess.domain.result.Scores;
import chess.repository.ChessRepository;
import chess.spring.dao.ChessDAO;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Objects;

@Service
public class ChessService {
    private static ChessBoard CACHED_CHESS_BOARD = null;

    private final ChessDAO chessDAO;

    public ChessService(ChessDAO chessDAO) {
        this.chessDAO = chessDAO;
    }

    public ChessBoard findChessBoard() {
        if (Objects.isNull(CACHED_CHESS_BOARD)) {
            CACHED_CHESS_BOARD = new ChessBoard(ChessBoardGenerator.generateDefaultChessBoard());
            Histories histories = new Histories(chessDAO.findAllHistories());
            histories.restoreChessBoardAsLatest(CACHED_CHESS_BOARD);
        }
        return CACHED_CHESS_BOARD;
    }

    public TeamType findCurrentTeamType() {
        Histories histories = new Histories(chessDAO.findAllHistories());
        return histories.findNextTeamType();
    }

    public void move(String current, String destination, String teamType) {
        ChessBoard chessBoard = findChessBoard();
        chessBoard.move(Coordinate.from(current), Coordinate.from(destination), TeamType.valueOf(teamType));
        chessDAO.insert(current, destination, teamType);
    }

    public Result calculateResult() {
        ChessBoard chessBoard = findChessBoard();
        Scores scores = chessBoard.calculateScores();
        TeamType winnerTeamType = chessBoard.findWinnerTeam();
        return new Result(scores, winnerTeamType);
    }

    public void deleteAllHistories() {
        chessDAO.deleteAll();
    }
}
