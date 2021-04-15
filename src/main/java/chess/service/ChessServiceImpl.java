package chess.service;

import chess.domain.board.ChessBoardFactory;
import chess.domain.command.CommandFactory;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.piece.PiecesFactory;
import chess.domain.player.Player;
import chess.domain.player.Round;
import chess.domain.position.Position;
import chess.domain.state.State;
import chess.domain.state.StateFactory;
import chess.dto.*;
import chess.dto.request.ChessRequestDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.request.TurnRequestDto;
import chess.dto.response.MoveResponseDto;
import chess.repository.ChessRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChessServiceImpl implements ChessService {
    private Round round;

    private final ChessRepository chessRepository;

    public ChessServiceImpl(final ChessRepository chessRepository) {
        this.round = makeRound();
        this.chessRepository = chessRepository;
    }

    @Override
    public Round makeRound() {
        return new Round(StateFactory.initialization(PiecesFactory.whitePieces()),
                StateFactory.initialization(PiecesFactory.blackPieces()),
                CommandFactory.initialCommand("start"));
    }

    @Override
    public Map<String, String> chessBoardFromDB() throws SQLException {
        Map<String, String> chessBoardFromDB = new LinkedHashMap<>();
        List<ChessRequestDto> pieces = chessRepository.showAllPieces();
        for (ChessRequestDto piece : pieces) {
            chessBoardFromDB.put(piece.getPiecePosition(), piece.getPieceName());
        }
        chessRepository.removeAllPieces();
        return chessBoardFromDB;
    }

    @Override
    public Map<Position, Piece> chessBoard(final Map<String, String> chessBoardFromDB) {
        return round.getBoard(ChessBoardFactory.loadBoard(chessBoardFromDB));
    }

    @Override
    public Map<Position, Piece> chessBoard() {
        return round.getBoard();
    }

    @Override
    public Map<String, String> stringChessBoard(final Map<Position, Piece> chessBoard) throws SQLException {
        Map<String, String> stringChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.entrySet()) {
            stringChessBoard.put(chessBoardEntry.getKey().toString(), chessBoardEntry.getValue().getPiece());
        }
        chessRepository.initializePieceStatus(stringChessBoard);
        return stringChessBoard;
    }

    @Override
    public PiecesDto piecesDto(final Map<Position, Piece> chessBoard) {
        List<Piece> whitePieces = new ArrayList<>();
        List<Piece> blackPieces = new ArrayList<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.entrySet()) {
            if (chessBoardEntry.getValue().isBlack()) {
                blackPieces.add(chessBoardEntry.getValue());
                continue;
            }
            whitePieces.add(chessBoardEntry.getValue());
        }
        return new PiecesDto(whitePieces, blackPieces);
    }

    @Override
    public void updateRound(final PiecesDto piecesDto) {
        round = new Round(StateFactory.initialization(new Pieces(piecesDto.getWhitePieces())),
                StateFactory.initialization(new Pieces(piecesDto.getBlackPieces())),
                CommandFactory.initialCommand("start"));
    }

    @Override
    public String currentTurn() throws SQLException {
        List<TurnRequestDto> turns = chessRepository.showCurrentTurn();
        return turns.stream()
                .map(TurnRequestDto::getCurrentTurn)
                .collect(Collectors.joining());
    }

    @Override
    public void changeRoundState(final String currentTurn) {
        if ("white".equals(currentTurn)) {
            Player white = round.getWhitePlayer();
            Player black = round.getBlackPlayer();
            State nextWhiteTurn = white.getState().toRunningTurn();
            State nextBlackTurn = black.getState().toFinishedTurn();
            white.changeState(nextWhiteTurn);
            black.changeState(nextBlackTurn);
        }
        if ("black".equals(currentTurn)) {
            Player white = round.getWhitePlayer();
            Player black = round.getBlackPlayer();
            State nextWhiteTurn = white.getState().toFinishedTurn();
            State nextBlackTurn = black.getState().toRunningTurn();
            white.changeState(nextWhiteTurn);
            black.changeState(nextBlackTurn);
        }
    }

    @Override
    public PlayerDto playerDto() {
        Player whitePlayer = round.getWhitePlayer();
        Player blackPlayer = round.getBlackPlayer();
        return new PlayerDto(whitePlayer, blackPlayer);
    }

    @Override
    public ScoreDto scoreDto(final PlayerDto playerDto) {
        double whiteScore = playerDto.getWhitePlayer().calculateScore();
        double blackScore = playerDto.getBlackPlayer().calculateScore();
        return new ScoreDto(whiteScore, blackScore);
    }

    @Override
    public void changeRoundToEnd(final PlayerDto playerDto) {
        if (!(playerDto.getWhitePlayer().getPieces().isKing() &&
                playerDto.getBlackPlayer().getPieces().isKing())) {
            round.changeToEnd();
        }
    }

    @Override
    public MoveResponseDto move(final MoveRequestDto moveRequestDto) throws SQLException {
        Queue<String> commands =
                new ArrayDeque<>(Arrays.asList("move", moveRequestDto.getSource(), moveRequestDto.getTarget()));
        try {
            executeRound(commands);
        } catch (RuntimeException runtimeException) {
            return new MoveResponseDto(true, runtimeException.getMessage());
        }
        movePiece(moveRequestDto);
        return new MoveResponseDto(false);
    }

    @Override
    public void executeRound(final Queue<String> commands) {
        round.execute(commands);
    }

    @Override
    public void movePiece(final MoveRequestDto moveRequestDto) throws SQLException {
        chessRepository.removePiece(moveRequestDto);
        chessRepository.movePiece(moveRequestDto);
    }

    @Override
    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) throws SQLException {
        chessRepository.changeTurn(turnChangeRequestDto);
    }

    @Override
    public void remove() throws SQLException {
        chessRepository.removeAllPieces();
        chessRepository.removeTurn();
    }

    @Override
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

    @Override
    public void initialize(final Map<String, String> filteredChessBoard) throws SQLException {
        chessRepository.initializePieceStatus(filteredChessBoard);
        chessRepository.initializeTurn();
    }
}
