package chess.service;

import chess.domain.board.ChessBoardFactory;
import chess.domain.command.CommandFactory;
import chess.domain.piece.Piece;
import chess.domain.piece.PiecesFactory;
import chess.domain.player.Player;
import chess.domain.player.Round;
import chess.domain.position.Position;
import chess.domain.state.StateFactory;
import chess.dto.ChessBoardDto;
import chess.dto.PlayerDto;
import chess.dto.StringChessBoardDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.ChessResponseDto;
import chess.dto.response.MoveResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.ScoreResponseDto;
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
        StringChessBoardDto filteredChessBoard = filteredChessBoard(round, roomId);
        chessRepository.initializePieceStatus(filteredChessBoard.getStringChessBoard(), roomId);
    }

    private StringChessBoardDto filteredChessBoard(final Round round, final Long roomId) {
        ChessBoardDto chessBoard = chessBoard(round, roomId);
        Map<String, String> filteredChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> chessBoardEntry : chessBoard.getChessBoard().entrySet()) {
            filterChessBoard(filteredChessBoard, chessBoardEntry);
        }
        return new StringChessBoardDto(filteredChessBoard);
    }

    private ChessBoardDto chessBoard(final Round round, final Long roomId) {
        String currentTurn = chessRepository.showCurrentTurn(roomId);
        return new ChessBoardDto(round.getBoard(), currentTurn);
    }

    private void filterChessBoard(final Map<String, String> filteredChessBoard,
                                  final Map.Entry<Position, Piece> chessBoardEntry) {
        if (chessBoardEntry.getValue() != null) {
            filteredChessBoard.put(chessBoardEntry.getKey().toString(), chessBoardEntry.getValue().getPiece());
        }
    }

    public ChessBoardDto chessBoardFromDB(final Long roomId) {
        StringChessBoardDto dbChessBoard = dbChessBoard(roomId);
        String currentTurn = chessRepository.showCurrentTurn(roomId);
        Round round = loadRoundFromDB(roomId);
        return new ChessBoardDto(round.getBoard(
                ChessBoardFactory.loadBoard(dbChessBoard.getStringChessBoard())), currentTurn);
    }

    private StringChessBoardDto dbChessBoard(final Long roomId) {
        Map<String, String> dbChessBoard = new LinkedHashMap<>();
        List<ChessResponseDto> pieces = chessRepository.showAllPieces(roomId);
        for (ChessResponseDto piece : pieces) {
            dbChessBoard.put(piece.getPiecePosition(), piece.getPieceName());
        }
        return new StringChessBoardDto(dbChessBoard);
    }

    public List<RoomResponseDto> rooms() {
        return chessRepository.showAllRooms();
    }

    public MoveResponseDto move(final MoveRequestDto moveRequestDto) {
        Queue<String> commands =
                new ArrayDeque<>(Arrays.asList("move", moveRequestDto.getSource(), moveRequestDto.getTarget()));
        Round round = loadRoundFromDB(moveRequestDto.getRoomId());
        round.execute(commands);
        movePiece(moveRequestDto);
        return new MoveResponseDto(true);
    }

    private Round loadRoundFromDB(final Long roomId) {
        StringChessBoardDto dbChessBoard = dbChessBoard(roomId);
        String currentTurn = chessRepository.showCurrentTurn(roomId);
        return Round.of(ChessBoardFactory.loadBoard(dbChessBoard.getStringChessBoard()), currentTurn);
    }

    public ScoreResponseDto scoreResponseDto(final Long roomId) {
        PlayerDto playerDto = playerDto(roomId);
        double whiteScore = playerDto.getWhitePlayer().calculateScore();
        double blackScore = playerDto.getBlackPlayer().calculateScore();
        return new ScoreResponseDto(whiteScore, blackScore);
    }

    private PlayerDto playerDto(final Long roomId) {
        Round round = loadRoundFromDB(roomId);
        Player whitePlayer = round.getWhitePlayer();
        Player blackPlayer = round.getBlackPlayer();
        return new PlayerDto(whitePlayer, blackPlayer);
    }

    public void movePiece(final MoveRequestDto moveRequestDto) {
        chessRepository.removePiece(moveRequestDto);
        chessRepository.movePiece(moveRequestDto);
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        chessRepository.changeTurn(turnChangeRequestDto);
    }
}
