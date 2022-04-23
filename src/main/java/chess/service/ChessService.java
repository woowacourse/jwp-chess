package chess.service;

import chess.dao.PieceDao;
import chess.dao.TurnDao;
import chess.domain.ChessWebGame;
import chess.domain.Position;
import chess.domain.Result;
import chess.domain.generator.BlackGenerator;
import chess.domain.generator.WhiteGenerator;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.player.Player;
import chess.domain.player.Team;
import chess.dto.MoveDto;
import chess.dto.PieceDto;
import chess.dto.ResultDto;
import chess.dto.ScoreDto;
import chess.dto.TurnDto;
import chess.view.ChessMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final PieceDao pieceDao;
    private final TurnDao turnDao;

    public ChessService(PieceDao pieceDao, TurnDao turnDao) {
        this.pieceDao = pieceDao;
        this.turnDao = turnDao;
    }

    public ChessMap initializeGame() {
        pieceDao.endPieces();
        pieceDao.initializePieces(new Player(new BlackGenerator(), Team.BLACK));
        pieceDao.initializePieces(new Player(new WhiteGenerator(), Team.WHITE));
        turnDao.resetTurn();

        final ChessWebGame chessWebGame = new ChessWebGame();
        return chessWebGame.initializeChessGame();
    }

    public ChessMap load() {
        final ChessWebGame chessWebGame = new ChessWebGame();
        loadPieces(chessWebGame);
        loadTurn(chessWebGame);

        return chessWebGame.createMap();
    }

    private void loadPieces(final ChessWebGame chessWebGame) {
        final List<PieceDto> whitePiecesDto = pieceDao.findPiecesByTeam(Team.WHITE);
        final List<PieceDto> blackPiecesDto = pieceDao.findPiecesByTeam(Team.BLACK);
        final List<Piece> whitePieces = whitePiecesDto.stream()
                .map(this::findPiece)
                .collect(Collectors.toUnmodifiableList());
        final List<Piece> blackPieces = blackPiecesDto.stream()
                .map(this::findPiece)
                .collect(Collectors.toUnmodifiableList());
        chessWebGame.loadPlayers(whitePieces, blackPieces);
    }

    private Piece findPiece(final PieceDto pieceDto) {
        final char name = Character.toLowerCase(pieceDto.getName().charAt(0));
        final Position position = Position.of(pieceDto.getPosition());
        final Map<Character, Piece> pieces = new HashMap<>();
        pieces.put('p', new Pawn(position));
        pieces.put('r', new Rook(position));
        pieces.put('n', new Knight(position));
        pieces.put('b', new Bishop(position));
        pieces.put('q', new Queen(position));
        pieces.put('k', new King(position));
        return pieces.get(name);
    }

    private void loadTurn(final ChessWebGame chessWebGame) {
        final TurnDto turnDto = turnDao.findTurn();
        Team turn = Team.from(turnDto.getTurn());
        chessWebGame.loadTurn(turn);
    }

    public ChessMap move(final MoveDto moveDto) {
        final Position currentPosition = Position.of(moveDto.getCurrentPosition());
        final Position destinationPosition = Position.of(moveDto.getDestinationPosition());
        final TurnDto turnDto = turnDao.findTurn();

        final ChessWebGame chessWebGame = loadGame();
        chessWebGame.move(currentPosition, destinationPosition);
        chessWebGame.changeTurn();
        pieceDao.removePieceByCaptured(moveDto);
        pieceDao.updatePiece(moveDto);
        turnDao.updateTurn(turnDto.getTurn());
        return chessWebGame.createMap();
    }

    private ChessWebGame loadGame() {
        final ChessWebGame chessWebGame = new ChessWebGame();
        loadPieces(chessWebGame);
        loadTurn(chessWebGame);
        return chessWebGame;
    }

    public ScoreDto getStatus() {
        final ChessWebGame chessWebGame = loadGame();
        final Map<String, Double> scores = chessWebGame.getScoreStatus();
        final Double whiteScore = scores.get(Team.WHITE.getName());
        final Double blackScore = scores.get(Team.BLACK.getName());

        final String status = String.format("%s: %.1f\n%s: %.1f",
                Team.WHITE.getName(), whiteScore, Team.BLACK.getName(), blackScore);
        return new ScoreDto(status);
    }

    public ResultDto getResult() {
        final ChessWebGame chessWebGame = loadGame();
        final Result result = chessWebGame.getResult();
        return new ResultDto(result.getResult());
    }
}
