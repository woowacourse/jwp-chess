package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.judge.Judge;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.repository.PieceInfo;
import wooteco.chess.repository.PieceInfoRepository;
import wooteco.chess.repository.RoomInfo;
import wooteco.chess.repository.RoomInfoRepository;
import wooteco.chess.utils.ModelParser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    @Autowired
    private PieceInfoRepository pieceInfoRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;

    public Map<String, Object> loadRooms() {
        List<RoomInfo> roominfos = roomInfoRepository.findAll();

        Map<String, Object> model = new HashMap<>();
        model.put("rooms", roominfos);
        return model;
    }

    public Map<String, Object> newGame(String roomName) {
        deleteGameData(roomName);

        Board board = BoardFactory.create();
        RoomInfo roomInfo = writeCurrentTurn(board.makeGameInfo(roomName));
        writeWholeBoard(board, roomInfo);

        return makeResultModel(roomName, board);
    }

    private Map<String, Object> makeResultModel(String roomName, Board board) {
        Map<String, Object> model = ModelParser.parseBoard(board);
        addScoreToModel(model, roomName);
        model.put("roomName", roomName);
        return model;
    }

    private Map<String, Object> makeResultModel(String roomName, Board board, List<Position> movablePositions) {
        Map<String, Object> model = ModelParser.parseBoard(board, movablePositions);
        addScoreToModel(model, roomName);
        model.put("roomName", roomName);
        return model;
    }

    private void deleteGameData(String roomName) {
        String roomNameHash = makeHash(roomName);
        Iterable<PieceInfo> pieceInfos = pieceInfoRepository.findByRoomNameHash(roomNameHash);
        RoomInfo roomInfo = roomInfoRepository.findByRoomNameOnlyOne(roomName);

        for (PieceInfo pieceInfo : pieceInfos) {
            pieceInfoRepository.deleteById(pieceInfo.getId());
        }
        if (roomInfo != null) {
            roomInfoRepository.deleteById(roomInfo.getId());
        }
    }

    private String makeHash(String roomName) {
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(roomName.getBytes());
            byte[] byteData = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }

    private RoomInfo writeCurrentTurn(RoomInfo roomInfo) {
        RoomInfo foundRoomInfo = roomInfoRepository.findByRoomNameOnlyOne(roomInfo.getRoomName());
        if (foundRoomInfo != null) {
            roomInfoRepository.deleteById(foundRoomInfo.getId());
        }
        return roomInfoRepository.save(roomInfo);
    }

    private void writeWholeBoard(final Board board, final RoomInfo roomInfo) {
        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);

            pieceInfoRepository.save(new PieceInfo(piece.toString(), position.toString(), makeHash(roomInfo.getRoomName())));
        }
    }

    public Map<String, Object> move(String roomName, String start, String end) {
        return makeResultModel(roomName, tryMove(roomName, start, end));
    }

    private Board tryMove(String roomName, String start, String end) {
        Board board = readBoard(roomName);
        try {
            checkGameOver(roomName);
            board.move(Position.of(start), Position.of(end));
            writeCurrentTurn(board.makeGameInfo(roomName));
            updateBoard(makeHash(roomName), start, end);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return board;
    }

    private void updateBoard(String roomNameHash, String start, String end) {
        PieceInfo startPiece = pieceInfoRepository.findByRoomNameHashAndPositionOnlyOne(roomNameHash, start);
        PieceInfo endPiece = pieceInfoRepository.findByRoomNameHashAndPositionOnlyOne(roomNameHash, end);

        PieceInfo newStartPiece = new PieceInfo(startPiece.getId(), "blank", startPiece.getPosition(), roomNameHash);
        PieceInfo newEndPiece = new PieceInfo(endPiece.getId(), startPiece.getPiece(), endPiece.getPosition(), roomNameHash);

        pieceInfoRepository.save(newStartPiece);
        pieceInfoRepository.save(newEndPiece);
    }

    public Board readBoard(String roomName) {
        List<PieceInfo> pieceInfos = pieceInfoRepository.findByRoomNameHash(makeHash(roomName));
        RoomInfo roomInfo = roomInfoRepository.findByRoomNameOnlyOne(roomName);

        Team currentTurn = Team.of(roomInfo.getCurrentTurn());
        return new Board(parsePieceInformation(pieceInfos), currentTurn);
    }

    public void addScoreToModel(Map<String, Object> model, String roomName) {
        model.put("player1_info", "WHITE: " + calculateScore(roomName, Team.WHITE));
        model.put("player2_info", "BLACK: " + calculateScore(roomName, Team.BLACK));
    }

    private Map<Position, Piece> parsePieceInformation(List<PieceInfo> persistGameInfo) {
        return persistGameInfo.stream()
                .collect(Collectors.toMap(pieceInfo -> Position.of(pieceInfo.getPosition()), pieceInfo -> Piece.of(pieceInfo.getPiece())));
    }

    private double calculateScore(String roomName, final Team team) {
        return Judge.getScoreByTeam(readBoard(roomName), team);
    }

    public Map<String, Object> loadMovable(String roomName, String startName) {
        Position start = Position.of(startName);
        List<Position> movablePositions = findMovablePlaces(roomName, start);
        Map<String, Object> model = makeResultModel(roomName, readBoard(roomName), movablePositions);

        if (movablePositions.size() != 0) {
            model.put("start", startName);
        }
        return model;
    }

    private List<Position> findMovablePlaces(String roomName, final Position start) {
        List<Position> movablePlaces = new ArrayList<>();
        try {
            checkGameOver(roomName);
            movablePlaces = readBoard(roomName).findMovablePositions(start);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return movablePlaces;
    }

    private void checkGameOver(String roomName) {
        if (Judge.findWinner(readBoard(roomName)).isPresent()) {
            throw new IllegalArgumentException("게임이 종료됐습니다.");
        }
    }

    public Map<String, Object> loadGame(String roomName) {
        Map<String, Object> model = ModelParser.parseBoard(readBoard(roomName));
        addScoreToModel(model, roomName);
        model.put("roomName", roomName);
        return model;
    }
}
