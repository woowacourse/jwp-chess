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
import jdk.jfr.internal.tool.PrettyWriter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChessService {
    private final ChessRepository chessRepository;
    private Round round;

    public ChessService(final ChessRepository chessRepository) {
        this.round = makeRound();
        this.chessRepository = chessRepository;
    }

    public void resetRound() {
        round = makeRound();
    }

    public Round makeRound() {
        return new Round(StateFactory.initialization(PiecesFactory.whitePieces()),
                StateFactory.initialization(PiecesFactory.blackPieces()),
                CommandFactory.initialCommand("start"));
    }

    public StringChessBoardDto dbChessBoard() {
        Map<String, String> dbChessBoard = new LinkedHashMap<>();
        List<ChessRequestDto> pieces = chessRepository.showAllPieces();
        for (ChessRequestDto piece : pieces) {
            dbChessBoard.put(piece.getPiecePosition(), piece.getPieceName());
        }
        chessRepository.removeAllPieces();
        return new StringChessBoardDto(dbChessBoard);
    }

    public ChessBoardDto chessBoard(final StringChessBoardDto dbChessBoard) {
        return new ChessBoardDto(round.getBoard(
                ChessBoardFactory.loadBoard(dbChessBoard.getStringChessBoard())));
    }

    public ChessBoardDto chessBoard() {
        return new ChessBoardDto(round.getBoard());
    }

    public StringChessBoardDto stringChessBoard(final ChessBoardDto chessBoard) {
        Map<String, String> stringChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.getChessBoard().entrySet()) {
            stringChessBoard.put(chessBoardEntry.getKey().toString(), chessBoardEntry.getValue().getPiece());
        }
        chessRepository.initializePieceStatus(stringChessBoard);
        return new StringChessBoardDto(stringChessBoard);
    }

    public PiecesDto piecesDto(final ChessBoardDto chessBoard) {
        List<Piece> whitePieces = new ArrayList<>();
        List<Piece> blackPieces = new ArrayList<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.getChessBoard().entrySet()) {
            if (chessBoardEntry.getValue().isBlack()) {
                blackPieces.add(chessBoardEntry.getValue());
                continue;
            }
            whitePieces.add(chessBoardEntry.getValue());
        }
        return new PiecesDto(whitePieces, blackPieces);
    }

    public void updateRound(final PiecesDto piecesDto) {
        round = new Round(StateFactory.initialization(new Pieces(piecesDto.getWhitePieces())),
                StateFactory.initialization(new Pieces(piecesDto.getBlackPieces())),
                CommandFactory.initialCommand("start"));
    }

    public String currentTurn() {
        List<TurnRequestDto> turns = chessRepository.showCurrentTurn();
        return turns.stream()
                .map(TurnRequestDto::getCurrentTurn)
                .collect(Collectors.joining());
    }

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

    public PlayerDto playerDto() {
        Player whitePlayer = round.getWhitePlayer();
        Player blackPlayer = round.getBlackPlayer();
        return new PlayerDto(whitePlayer, blackPlayer);
    }

    public ScoreDto scoreDto(final PlayerDto playerDto) {
        double whiteScore = playerDto.getWhitePlayer().calculateScore();
        double blackScore = playerDto.getBlackPlayer().calculateScore();
        return new ScoreDto(whiteScore, blackScore);
    }

    public void changeRoundToEnd(final PlayerDto playerDto) {
        if (!(playerDto.getWhitePlayer().getPieces().isKing() &&
                playerDto.getBlackPlayer().getPieces().isKing())) {
            round.changeToEnd();
        }
    }

    public MoveResponseDto move(final MoveRequestDto moveRequestDto) {
        Queue<String> commands =
                new ArrayDeque<>(Arrays.asList("move", moveRequestDto.getSource(), moveRequestDto.getTarget()));
        executeRound(commands);
        movePiece(moveRequestDto);
        return new MoveResponseDto(true);
    }

    public void executeRound(final Queue<String> commands) {
        round.execute(commands);
    }

    public void movePiece(final MoveRequestDto moveRequestDto) {
        chessRepository.removePiece(moveRequestDto);
        chessRepository.movePiece(moveRequestDto);
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        chessRepository.changeTurn(turnChangeRequestDto);
    }

    public void remove() {
        chessRepository.removeAllPieces();
        chessRepository.removeTurn();
    }

    public StringChessBoardDto filteredChessBoard(final ChessBoardDto chessBoard) {
        Map<String, String> filteredChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.getChessBoard().entrySet()) {
            if (chessBoardEntry.getValue() != null) {
                filteredChessBoard.put(chessBoardEntry.getKey().toString(),
                        chessBoardEntry.getValue().getPiece());
            }
        }
        return new StringChessBoardDto(filteredChessBoard);
    }

    public void initialize(final StringChessBoardDto filteredChessBoard) {
        chessRepository.initializePieceStatus(filteredChessBoard.getStringChessBoard());
        chessRepository.initializeTurn();
    }
}
