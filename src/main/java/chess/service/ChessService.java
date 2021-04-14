package chess.service;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardGenerator;
import chess.domain.board.Coordinate;
import chess.domain.history.Histories;
import chess.domain.piece.TeamType;
import chess.domain.result.Result;
import chess.domain.result.Scores;
import chess.repository.ChessRepository;

import java.sql.SQLException;
import java.util.Objects;

public class ChessService {
    private static ChessBoard CACHED_CHESS_BOARD = null;

    private final ChessRepository chessRepository;

    public ChessService(ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public ChessBoard findChessBoard() throws SQLException {
        if (Objects.isNull(CACHED_CHESS_BOARD)) {
            CACHED_CHESS_BOARD = new ChessBoard(ChessBoardGenerator.generateDefaultChessBoard());
            Histories histories = new Histories(chessRepository.findAllHistories());
            histories.restoreChessBoardAsLatest(CACHED_CHESS_BOARD);
        }
        return CACHED_CHESS_BOARD;
    }

    public TeamType findCurrentTeamType() throws SQLException {
        Histories histories = new Histories(chessRepository.findAllHistories());
        return histories.findNextTeamType();
    }

    public void move(String current, String destination, String teamType) throws SQLException {
        ChessBoard chessBoard = findChessBoard();
        chessBoard.move(Coordinate.from(current), Coordinate.from(destination), TeamType.valueOf(teamType));
        chessRepository.insertHistory(current, destination, teamType);
    }

    public Result calculateResult() throws SQLException {
        ChessBoard chessBoard = findChessBoard();
        Scores scores = chessBoard.calculateScores();
        TeamType winnerTeamType = chessBoard.findWinnerTeam();
        return new Result(scores, winnerTeamType);
    }

    public void deleteAllHistories() throws SQLException {
        chessRepository.deleteAllHistories();
        CACHED_CHESS_BOARD = null;
    }
}
