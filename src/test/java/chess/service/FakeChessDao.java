//package chess.service;
//
//import chess.dao.ChessDao;
//import chess.dto.GameRoomDto;
//import chess.dto.PieceAndPositionDto;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class FakeChessDao implements ChessDao {
//
//    private final Map<Integer, Map<Integer, PieceAndPositionDto>> pieceData = new HashMap<>();
//    private int pieceId = 0;
//    private final Map<Integer, GameRoomDto> gameData = new HashMap<>();
//
//    @Override
//    public void updateTurn(String color, int gameId) {
//        gameData.put(gameId, new GameRoomDto(color));
//    }
//
//    @Override
//    public void deletePiece(int gameId) {
//        pieceData.clear();
//    }
//
//    @Override
//    public void savePiece(int gameId, PieceAndPositionDto pieceAndPositionDto) {
//        if (pieceData.get(gameId) != null) {
//            Map<Integer, PieceAndPositionDto> pieceIdPieceAndPositionDtoMap = new HashMap<>(pieceData.get(gameId));
//            pieceIdPieceAndPositionDtoMap.put(pieceId++, pieceAndPositionDto);
//            pieceData.put(gameId, pieceIdPieceAndPositionDtoMap);
//            return;
//        }
//        pieceData.put(gameId, Map.of(pieceId++, pieceAndPositionDto));
//    }
//
//    @Override
//    public List<PieceAndPositionDto> findAllPiece(int gameId) {
//        return new ArrayList<>(pieceData.get(gameId).values());
//    }
//
//    @Override
//    public String findCurrentColor(int gameId) {
//        return gameData.get(gameId);
//    }
//
//    @Override
//    public void deletePiece(int gameId, String to) {
//        var pieceId = pieceData.get(gameId).entrySet().stream()
//                .filter(it -> it.getValue().getPosition().equalsIgnoreCase(to))
//                .map(Map.Entry::getKey)
//                .findFirst();
//        pieceId.ifPresent(integer -> pieceData.get(gameId).remove(integer));
//    }
//
//    @Override
//    public void updatePiece(String from, String to, int gameId) {
//        var pieceId = pieceData.get(gameId).entrySet().stream()
//                .filter(it -> it.getValue().getPosition().equals(from))
//                .map(Map.Entry::getKey)
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//        pieceData.get(gameId).get(pieceId).setPosition(to);
//    }
//
//    @Override
//    public void deleteGame(int gameId) {
//        gameData.remove(gameId);
//    }
//
//    @Override
//    public void initGame(int gameId) {
//        gameData.put(gameId, "WHITE");
//    }
//
//    @Override
//    public Number initGame(String title, String password) {
//        return null;
//    }
//
//    @Override
//    public String findPassword(int gameId) {
//        return null;
//    }
//
//    @Override
//    public List<GameRoomDto> findAllGame() {
//        return null;
//    }
//}
