package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.judge.Judge;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.repository.GameStatus;
import wooteco.chess.repository.GameStatusRepository;
import wooteco.chess.repository.PieceInfo;
import wooteco.chess.repository.PieceInfoRepository;
import wooteco.chess.webutil.ModelParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ChessService {

    @Autowired
    private PieceInfoRepository pieceInfoRepository;
    @Autowired
    private GameStatusRepository gameStatusRepository;

    public Map<String, Object> loadInitBoard() {
        Map<String, Object> model = ModelParser.parseBlankBoard();
        model.putAll(ModelParser.parseMovablePlaces(new ArrayList<>()));
        return model;
    }

    public Board newGame() {
        truncateAll();
        Board board = BoardFactory.create();
        writeWholeBoard(board);
        writeCurrentTurn(Team.WHITE);
        return board;
    }

    private void truncateAll() {
        pieceInfoRepository.deleteAll();
        gameStatusRepository.deleteAll();
    }

    private void writeWholeBoard(final Board board) {
        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);

            pieceInfoRepository.save(new PieceInfo(piece.toString(), position.toString()));
        }
    }

    private void writeCurrentTurn(final Team turn) {
        gameStatusRepository.save(new GameStatus(turn.toString()));
    }

    public void move(String start, String end) {
        checkGameOver();

        Board board = readBoard();
        try {
            board.move(Position.of(start), Position.of(end));
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        truncateAll();
        writeWholeBoard(board);
        writeCurrentTurn(board.getCurrentTurn());
    }

    public Board readBoard() {
        Iterable<PieceInfo> persistGameInfo = pieceInfoRepository.findAll();
        GameStatus persistGameStatus = gameStatusRepository.findAll()
                .iterator()
                .next();
        Team team = Team.of(persistGameStatus.getCurrentTurn());
        return new Board(parsePieceInformation(persistGameInfo), team);
    }

    public Map<String, Object> readBoardWithScore() {
        Board board = readBoard();

        Map<String, Object> model = ModelParser.parseBoard(board);
        model.put("player1_info", "WHITE: " + calculateScore(Team.WHITE));
        model.put("player2_info", "BLACK: " + calculateScore(Team.BLACK));
        return model;
    }

    private Map<Position, Piece> parsePieceInformation(Iterable<PieceInfo> persistGameInfo) {
        return StreamSupport.stream(persistGameInfo.spliterator(), false)
                .collect(Collectors.toMap(gameInfo -> Position.of(gameInfo.getPosition()), gameInfo -> Piece.of(gameInfo.getPiece())));
    }

    private double calculateScore(final Team team) {
        return Judge.getScoreByTeam(readBoard(), team);
    }

    public Map<String, Object> loadMovable(String startName) {
        Position start = Position.of(startName);
        List<Position> movablePositions = findMovablePlaces(start);
        Map<String, Object> model = ModelParser.parseBoard(readBoard(), movablePositions);
        if (movablePositions.size() != 0) {
            model.put("start", startName);
        }
        return model;
    }

    private List<Position> findMovablePlaces(final Position start) {
        checkGameOver();
        return readBoard().findMovablePositions(start);
    }

    private void checkGameOver() {
        if (Judge.findWinner(readBoard()).isPresent()) {
            throw new IllegalArgumentException();
        }
    }
}
