package chess.service;

import chess.dao.dto.game.GameDto;
import chess.dao.dto.history.HistoryDto;
import chess.dao.dto.piece.PieceDto;
import chess.dao.dto.score.ScoreDto;
import chess.dao.dto.state.StateDto;
import chess.domain.board.Board;
import chess.domain.board.position.Path;
import chess.domain.board.position.Position;
import chess.domain.entity.History;
import chess.domain.entity.Score;
import chess.domain.entity.State;
import chess.domain.entity.Game;
import chess.domain.manager.ChessManager;
import chess.domain.movecommand.MoveCommand;
import chess.domain.piece.EmptyPiece;
import chess.domain.piece.Owner;
import chess.domain.piece.Piece;
import chess.domain.repository.*;
import chess.service.dto.game.GameInfoDto;
import chess.service.dto.move.MoveDto;
import chess.service.dto.path.PathDto;
import chess.util.PieceConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final GameRepository gameRepository;
    private final HistoryRepository historyRepository;
    private final PieceRepository pieceRepository;
    private final ScoreRepository scoreRepository;
    private final StateRepository stateRepository;
    private final ModelMapper modelMapper;

    public ChessService(GameRepository gameRepository, HistoryRepository historyRepository, PieceRepository pieceRepository,
                        ScoreRepository scoreRepository, StateRepository stateRepository, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.historyRepository = historyRepository;
        this.pieceRepository = pieceRepository;
        this.scoreRepository = scoreRepository;
        this.stateRepository = stateRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Long saveGame(final GameInfoDto gameInfoDto) {
        ChessManager chessManager = new ChessManager();
        Game game = new Game(gameInfoDto.getRoomName(), gameInfoDto.getWhiteUsername(), gameInfoDto.getBlackUsername());
        Long gameId = gameRepository.save(game);
        State state = State.of(gameId, chessManager);
        stateRepository.save(state);
        scoreRepository.save(Score.of(chessManager.gameStatus(), gameId));
        List<chess.domain.entity.Piece> pieces = chessManager.boardToMap().entrySet().stream()
                .map(entry -> new chess.domain.entity.Piece(gameId, entry.getValue().getSymbol(), entry.getKey().parseString()))
                .collect(Collectors.toList());
        pieceRepository.savePieces(gameId, pieces);
        return gameId;
    }

    public List<PieceDto> findPiecesById(final Long gameId) {
        List<chess.domain.entity.Piece> pieces = pieceRepository.findPiecesByGameId(gameId);
        return pieces.stream()
                .map(piece -> modelMapper.map(piece, PieceDto.class))
                .collect(Collectors.toList());
    }

    public GameDto findGameByGameId(final Long gameId) {
        Game game = gameRepository.findById(gameId);
        return modelMapper.map(game, GameDto.class);
    }

    public List<GameDto> findGamesByPlayingIsTrue() {
        List<Game> games = gameRepository.findByPlayingIsTrue();
        return games.stream()
                .map(game -> modelMapper.map(game, GameDto.class))
                .collect(Collectors.toList());
    }

    public ScoreDto findScoreByGameId(final Long gameId) {
        Score score = scoreRepository.findByGameId(gameId);
        return modelMapper.map(score, ScoreDto.class);
    }

    public StateDto findStateByGameId(final Long gameId) {
        State state = stateRepository.findByGameId(gameId);
        return modelMapper.map(state, StateDto.class);
    }

    public List<HistoryDto> findHistoriesByGameId(final Long gameId) {
        List<History> histories = historyRepository.findByGameId(gameId);
        return histories.stream()
                .map(history -> modelMapper.map(history, HistoryDto.class))
                .collect(Collectors.toList());
    }

    public PathDto movablePath(final String source, final Long gameId) {
        ChessManager chessManager = loadChessManager(gameId);
        System.out.println("asdfgsa service= " + source + " = ******");
        Path path = chessManager.movablePath(Position.of(source));
        return PathDto.from(path);
    }

    @Transactional
    public HistoryDto move(final MoveDto moveDto, final Long gameId) {
        ChessManager chessManager = loadChessManager(gameId);
        MoveCommand moveCommand = new MoveCommand(moveDto.getSource(), moveDto.getTarget());
        History history = History.of(gameId, moveCommand, chessManager);
        Piece sourcePiece = chessManager.pickPiece(Position.of(moveCommand.source()));
        chessManager.move(Position.of(moveCommand.source()), Position.of(moveCommand.target()));
        scoreRepository.update(Score.of(chessManager.gameStatus(), gameId));
        stateRepository.update(State.of(gameId, chessManager));
        this.updatePieceByMove(moveCommand, sourcePiece, gameId);
        historyRepository.save(history);
        return modelMapper.map(history, HistoryDto.class);
    }

    private void updatePieceByMove(MoveCommand moveCommand, Piece sourcePiece, Long gameId) {
        chess.domain.entity.Piece targetPiece =
                new chess.domain.entity.Piece(gameId, sourcePiece.getSymbol(), moveCommand.target());
        pieceRepository.update(targetPiece);

        EmptyPiece emptyPiece = EmptyPiece.getInstance();
        chess.domain.entity.Piece sourceEmptyPiece =
                new chess.domain.entity.Piece(gameId, emptyPiece.getSymbol(), moveCommand.source());
        pieceRepository.update(sourceEmptyPiece);
    }

    private ChessManager loadChessManager(final Long gameId) {
        List<chess.domain.entity.Piece> pieces = pieceRepository.findPiecesByGameId(gameId);
        State state = stateRepository.findByGameId(gameId);
        return new ChessManager(
                this.initBoard(pieces),
                Owner.valueOf(state.getTurnOwner()),
                state.getTurnNumber(),
                state.isPlaying());
    }

    private Board initBoard(List<chess.domain.entity.Piece> pieces) {
        Map<Position, Piece> piecesOnBoard = new HashMap<>();
        for (chess.domain.entity.Piece piece : pieces) {
            piecesOnBoard.put(Position.of(piece.getPosition()), PieceConverter.parsePiece(piece.getSymbol()));
        }
        return new Board(piecesOnBoard);
    }
}
