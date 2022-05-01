package chess.service;

import static chess.domain.game.ChessGame.END_STATE;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.board.Board;
import chess.domain.board.coordinate.Coordinate;
import chess.domain.game.ChessGame;
import chess.domain.game.StatusCalculator;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.dto.BoardDto;
import chess.dto.GameCreateDto;
import chess.dto.GameDeleteDto;
import chess.dto.GameDeleteResponseDto;
import chess.dto.GameDto;
import chess.dto.MoveRequestDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private final BoardDao boardDao;
    private final GameDao gameDao;
    private final PasswordEncoder passwordEncoder;

    public ChessService(BoardDao boardDao, GameDao gameDao,
                        PasswordEncoder passwordEncoder) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Board findBoardByGameId(int gameId) {
        Map<Coordinate, Piece> board = new HashMap<>();
        List<BoardDto> boardDtos = boardDao.findByGameId(gameId);
        for (BoardDto boardDto : boardDtos) {
            board.put(Coordinate.of(boardDto.getPosition()), Piece.of(boardDto.getSymbol(), boardDto.getTeam()));
        }
        return new Board(board);
    }

    public int start(GameCreateDto gameCreateDto) {
        int gameId = gameDao.save(new GameDto(gameCreateDto.getRoomName(), gameCreateDto.getPassword(), Team.WHITE.name()));
        saveBoard(Board.create(), gameId);
        return gameId;
    }

    private void saveBoard(Board board, int gameId) {
        List<BoardDto> boardDtos = new ArrayList<>();
        Map<String, Piece> pieces = board.toMap();

        for (String key : pieces.keySet()) {
            Piece piece = pieces.get(key);
            boardDtos.add(new BoardDto(piece.getSymbol(), piece.getTeam(), key));
        }

        boardDao.save(boardDtos, gameId);
    }

    public void move(int gameId, MoveRequestDto moveRequestDto) {
        Board board = findBoardByGameId(gameId);
        String state = gameDao.findStateById(gameId);

        ChessGame chessGame = new ChessGame(board, state);
        String from = moveRequestDto.getFrom();
        String to = moveRequestDto.getTo();
        chessGame.move(Coordinate.of(from), Coordinate.of(to));
        changeState(gameId, state, chessGame);

        updateBoard(board.findPiece(Coordinate.of(from)), from, gameId);
        updateBoard(board.findPiece(Coordinate.of(to)), to, gameId);
    }

    private void updateBoard(Piece piece, String position, int gameId) {
        BoardDto boardDto = new BoardDto(piece.getSymbol(), piece.getTeam(), position);

        boardDao.update(boardDto, gameId);
    }

    private void changeState(int gameId, String state, ChessGame chessGame) {
        if (chessGame.isFinished()) {
            endGame(gameId);
            return;
        }

        changeTurn(state, gameId);
    }

    private void changeTurn(String state, int gameId) {
        if (state.equals(Team.WHITE.name())) {
            gameDao.update(Team.BLACK.name(), gameId);
            return;
        }
        gameDao.update(Team.WHITE.name(), gameId);
    }

    private void endGame(int gameId) {
        gameDao.update(END_STATE, gameId);
    }

    public StatusCalculator createStatus(int gameId) {
        Board board = findBoardByGameId(gameId);
        String state = gameDao.findStateById(gameId);

        ChessGame chessGame = new ChessGame(board, state);
        return chessGame.status();
    }

    public GameDeleteResponseDto deleteGameByGameId(GameDeleteDto gameDeleteDto) {
        if (isSamePassword(gameDeleteDto)) {
            gameDao.deleteById(gameDeleteDto.getId());
            return GameDeleteResponseDto.success();
        }
        return GameDeleteResponseDto.fail();
    }

    private boolean isSamePassword(GameDeleteDto gameDeleteDto) {
        String passwordById = gameDao.findPasswordById(gameDeleteDto.getId());
        return passwordEncoder.matches(gameDeleteDto.getPassword(), passwordById);
    }

    public List<GameDto> findGames() {
        return gameDao.findGames();
    }
}
