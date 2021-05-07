package chess.service;

import chess.dao.dto.game.GameDto;
import chess.dao.dto.history.HistoryDto;
import chess.dao.dto.piece.PieceDto;
import chess.dao.dto.score.ScoreDto;
import chess.dao.dto.state.StateDto;
import chess.domain.board.Board;
import chess.domain.board.position.Path;
import chess.domain.board.position.Position;
import chess.domain.entity.Game;
import chess.domain.entity.History;
import chess.domain.entity.Score;
import chess.domain.entity.State;
import chess.domain.manager.ChessManager;
import chess.domain.movecommand.MoveCommand;
import chess.domain.board.piece.EmptyPiece;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.repository.*;
import chess.exception.GameJoinException;
import chess.exception.TurnOwnerException;
import chess.service.dto.game.GameJoinInfoDto;
import chess.service.dto.game.GameSaveInfoDto;
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

    private static final String EMPTY = "";

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
    public Long saveGame(final GameSaveInfoDto gameSaveInfoDto) {
        ChessManager chessManager = new ChessManager();
        Game game = new Game(gameSaveInfoDto.getRoomName(), gameSaveInfoDto.getWhiteUsername(), gameSaveInfoDto.getWhitePassword(), EMPTY, EMPTY);
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

    @Transactional
    public Long joinGame(final GameJoinInfoDto gameJoinInfoDto) {
        Game game = gameRepository.findById(gameJoinInfoDto.getId());
        if (game.getWhiteUsername().equals(gameJoinInfoDto.getUsername())) {
            joinWhiteuser(game, gameJoinInfoDto);
            return game.getId();
        }
        joinBlackuser(game, gameJoinInfoDto);
        return game.getId();
    }

    private void joinWhiteuser(Game game, GameJoinInfoDto gameJoinRequestDto) {
        checkPassword(gameJoinRequestDto.getPassword(), game.getWhitePassword());
    }

    private void joinBlackuser(Game game, GameJoinInfoDto gameJoinInfoDto) {
        if (game.getBlackUsername().isEmpty()) {
            game.setBlackUsername(gameJoinInfoDto.getUsername());
            game.setBlackPassword(gameJoinInfoDto.getPassword());
            gameRepository.update(game);
            return;
        }
        if (game.getBlackUsername().equals(gameJoinInfoDto.getUsername())) {
            checkPassword(gameJoinInfoDto.getPassword(), game.getBlackPassword());
            return;
        }
        throw new GameJoinException("방에 접속할 수 없습니다.");
    }

    private void checkPassword(String password, String dbPassword) {
        if (password.equals(dbPassword)) {
            return;
        }
        throw new GameJoinException("비밀번호가 일치하지 않습니다.");
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

    private void updatePieceByMove(final MoveCommand moveCommand, final Piece sourcePiece, final Long gameId) {
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

    private Board initBoard(final List<chess.domain.entity.Piece> pieces) {
        Map<Position, Piece> piecesOnBoard = new HashMap<>();
        for (chess.domain.entity.Piece piece : pieces) {
            piecesOnBoard.put(Position.of(piece.getPosition()), PieceConverter.parsePiece(piece.getSymbol()));
        }
        return new Board(piecesOnBoard);
    }

    public void checkTurn(final Long gameId, final String password) {
        State state = stateRepository.findByGameId(gameId);
        Game game = gameRepository.findById(gameId);
        String turnOwner = state.getTurnOwner();
        if (turnOwner.equals("WHITE")) {
            checkTurnByPassword(game.getWhitePassword(), password);
            return;
        }
        checkTurnByPassword(game.getBlackPassword(), password);
    }

    private void checkTurnByPassword(final String password, final String otherPassword) {
        if (password.equals(otherPassword)) {
            return;
        }
        throw new TurnOwnerException("지금은 상대편의 턴입니다.");
    }
}
