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
import chess.dto.ChessBoardDto;
import chess.dto.PiecesDto;
import chess.dto.PlayerDto;
import chess.dto.StringChessBoardDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.ChessResponseDto;
import chess.dto.response.MoveResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.dto.response.TurnResponseDto;
import chess.repository.ChessRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void remove() {
        chessRepository.removeAllPieces();
        chessRepository.removeTurn();
    }

    public void resetRound() {
        round = makeRound();
    }

    public Round makeRound() {
        return new Round(StateFactory.initialization(PiecesFactory.whitePieces()),
                StateFactory.initialization(PiecesFactory.blackPieces()),
                CommandFactory.initialCommand("start"));
    }

    public ChessBoardDto chessBoardFromDB() {
        StringChessBoardDto dbChessBoard = dbChessBoard();
        return new ChessBoardDto(round.getBoard(
                ChessBoardFactory.loadBoard(dbChessBoard.getStringChessBoard())));
    }

    private StringChessBoardDto dbChessBoard() {
        Map<String, String> dbChessBoard = new LinkedHashMap<>();
        List<ChessResponseDto> pieces = chessRepository.showAllPieces();
        for (ChessResponseDto piece : pieces) {
            dbChessBoard.put(piece.getPiecePosition(), piece.getPieceName());
        }
        chessRepository.removeAllPieces();
        return new StringChessBoardDto(dbChessBoard);
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
            filterPieces(whitePieces, blackPieces, chessBoardEntry);
        }
        return new PiecesDto(whitePieces, blackPieces);
    }

    private void filterPieces(final List<Piece> whitePieces, final List<Piece> blackPieces,
                              final Map.Entry<Position, Piece> chessBoardEntry) {
        if (chessBoardEntry.getValue().isBlack()) {
            blackPieces.add(chessBoardEntry.getValue());
            return;
        }
        whitePieces.add(chessBoardEntry.getValue());
    }

    public String jsonFormatChessBoard(final StringChessBoardDto stringChessBoard) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(stringChessBoard.getStringChessBoard());
    }

    public String currentTurn() {
        List<TurnResponseDto> turns = chessRepository.showCurrentTurn();
        return turns.stream()
                .map(TurnResponseDto::getCurrentTurn)
                .collect(Collectors.joining());
    }

    public void updateRound(final ChessBoardDto chessBoard, final String currentTurn) {
        changeRound(chessBoard);
        changeRoundState(currentTurn);
    }

    private void changeRound(final ChessBoardDto chessBoard) {
        PiecesDto piecesDto = piecesDto(chessBoard);
        round = new Round(StateFactory.initialization(new Pieces(piecesDto.getWhitePieces())),
                StateFactory.initialization(new Pieces(piecesDto.getBlackPieces())),
                CommandFactory.initialCommand("start"));
    }

    private void changeRoundState(final String currentTurn) {
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

    public ScoreResponseDto scoreResponseDto() {
        PlayerDto playerDto = playerDto();
        double whiteScore = playerDto.getWhitePlayer().calculateScore();
        double blackScore = playerDto.getBlackPlayer().calculateScore();
        changeRoundToEnd();
        return new ScoreResponseDto(whiteScore, blackScore);
    }

    private void changeRoundToEnd() {
        PlayerDto playerDto = playerDto();
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

    public StringChessBoardDto filteredChessBoard(final ChessBoardDto chessBoard) {
        Map<String, String> filteredChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.getChessBoard().entrySet()) {
            filterChessBoard(filteredChessBoard, chessBoardEntry);
        }
        return new StringChessBoardDto(filteredChessBoard);
    }

    private void filterChessBoard(final Map<String, String> filteredChessBoard,
                                  final Map.Entry<Position, Piece> chessBoardEntry) {
        if (chessBoardEntry.getValue() != null) {
            filteredChessBoard.put(chessBoardEntry.getKey().toString(), chessBoardEntry.getValue().getPiece());
        }
    }

    public void initialize(final StringChessBoardDto filteredChessBoard) {
        chessRepository.initializePieceStatus(filteredChessBoard.getStringChessBoard());
        chessRepository.initializeTurn();
    }
}
