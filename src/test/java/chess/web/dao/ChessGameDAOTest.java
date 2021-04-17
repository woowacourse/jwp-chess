//package chess.web.dao;
//
//import static chess.domain.player.type.TeamColor.BLACK;
//import static chess.domain.player.type.TeamColor.WHITE;
//import static chess.utils.TestFixture.TEST_TITLE;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import chess.web.dao.entity.ChessGameEntity;
//import chess.web.dao.entity.GameStatusEntity;
//import chess.web.dao.entity.PiecePositionEntity;
//import chess.web.dao.game.ChessGameRepository;
//import chess.web.dao.player.PlayerRepository;
//import chess.web.dao.playerpieceposition.PlayerPiecePositionRepository;
//import chess.domain.board.setting.BoardDefaultSetting;
//import chess.web.domain.game.ChessGame;
//import chess.utils.DBCleaner;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.stream.Collectors;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class ChessGameDAOTest {
//    public static final int NUMBER_OF_PIECES_OF_ONE_PLAYER = 16;
//
//    @Autowired
//    private ChessGameRepository chessGameRepository;
//    @Autowired
//    private PlayerRepository playerRepository;
//    @Autowired
//    private PlayerPiecePositionRepository playerPiecePositionRepository;
//    @Autowired
//    private ChessGame chessGame;
//    @Autowired
//    private DBCleaner dbCleaner;
//
//    @AfterEach
//    void tearDown() throws SQLException {
//        dbCleaner.removeAll();
//    }
//
//    @DisplayName("체스 게임 저장 및 조회")
//    @Test
//    void saveAndFind() throws SQLException {
//        ChessGameEntity chessGame1 = new ChessGameEntity(TEST_TITLE);
//        chessGameRepository.save(chessGame1);
//        ChessGameEntity foundChessGame = chessGameRepository.findById(chessGame1.getId());
//
//        assertThat(foundChessGame.getTitle()).isEqualTo(chessGame1.getTitle());
//
//        String testTitle2 = TEST_TITLE + "2";
//
//        ChessGameEntity chessGame2 = new ChessGameEntity(testTitle2);
//        chessGameRepository.save(chessGame2);
//
//        List<ChessGameEntity> foundAllChessGames = chessGameRepository.findAll();
//        List<Long> foundAllChessGameIds = foundAllChessGames.stream()
//            .map(ChessGameEntity::getId)
//            .collect(Collectors.toList());
//
//        assertThat(foundAllChessGameIds).containsExactlyInAnyOrder(chessGame1.getId(), chessGame2.getId());
//    }
//
//    @DisplayName("체스 게임 이름, 현재 턴 팀 색깔 가져오기")
//    @Test
//    void findStatusByGameId() throws SQLException {
//        ChessGameEntity chessGame = new ChessGameEntity(TEST_TITLE);
//        ChessGameEntity savedChessGame = chessGameRepository.save(chessGame);
//
//        GameStatusEntity gameStatus = chessGameRepository.findStatusByGameId(savedChessGame.getId());
//
//        assertThat(gameStatus.getCurrentTurnTeamColor()).isSameAs(WHITE);
//        assertThat(gameStatus.getTitle()).isEqualTo(TEST_TITLE);
//    }
//
//    @DisplayName("현재 턴 팀 색깔 업데이트")
//    @Test
//    void updateCurrentTurnTeamColor() throws SQLException {
//        ChessGameEntity chessGame = new ChessGameEntity(TEST_TITLE);
//        chessGameRepository.save(chessGame);
//        ChessGameEntity foundChessGame = chessGameRepository.findById(chessGame.getId());
//
//        assertThat(foundChessGame.getCurrentTurnTeamColor()).isSameAs(WHITE);
//
//        chessGame.setCurrentTurnTeamColor(BLACK);
//        chessGameRepository.updateCurrentTurnTeamColor(chessGame);
//
//        ChessGameEntity foundChessGameAfterChange = chessGameRepository.findById(chessGame.getId());
//
//        assertThat(foundChessGameAfterChange.getCurrentTurnTeamColor()).isEqualTo(BLACK);
//    }
//
//    @DisplayName("1개 삭제")
//    @Test
//    void remove() throws SQLException {
//        ChessGameEntity chessGame1 = new ChessGameEntity(TEST_TITLE);
//        chessGameRepository.save(chessGame1);
//
//        String testTitle2 = TEST_TITLE + "2";
//        ChessGameEntity chessGame2 = new ChessGameEntity(testTitle2);
//        chessGameRepository.save(chessGame2);
//
//        chessGameRepository.remove(chessGame1.getId());
//
//        List<ChessGameEntity> foundAllChessGames = chessGameRepository.findAll();
//
//        assertThat(foundAllChessGames).hasSize(1);
//        assertThat(foundAllChessGames.get(0).getId()).isEqualTo(chessGame2.getId());
//    }
//
//    @DisplayName("전체 삭제")
//    @Test
//    void removeAll() throws SQLException {
//        ChessGameEntity chessGame1 = new ChessGameEntity(TEST_TITLE);
//        chessGameRepository.save(chessGame1);
//
//        String testTitle2 = TEST_TITLE + "2";
//        ChessGameEntity chessGame2 = new ChessGameEntity(testTitle2);
//        chessGameRepository.save(chessGame2);
//
//        chessGameRepository.removeAll();
//
//        List<ChessGameEntity> foundAllChessGames = chessGameRepository.findAll();
//
//        assertThat(foundAllChessGames).hasSize(0);
//    }
//
//    @DisplayName("종료를 요청하면, 현재 진행중인 ChessGame에 관련된 모든 정보 삭제")
//    @Test
//    void endGame() throws SQLException {
//        chessGame.createNew(new BoardDefaultSetting(), "game1");
//        chessGame.createNew(new BoardDefaultSetting(), "game2");
//
//        List<ChessGameEntity> allChessGames = chessGameRepository.findAll();
//        assertThat(allChessGames).hasSizeGreaterThan(0);
//
//        for (ChessGameEntity chessGameEntity : allChessGames) {
//            Long whitePlayerId = playerRepository.findIdByGameIdAndTeamColor(chessGameEntity.getId(), WHITE);
//            Long blackPlayerId = playerRepository.findIdByGameIdAndTeamColor(chessGameEntity.getId(), BLACK);
//
//            assertThat(whitePlayerId).isNotNull();
//            assertThat(blackPlayerId).isNotNull();
//
//            assertPiecesSizeOfPlayers(whitePlayerId);
//            assertPiecesSizeOfPlayers(blackPlayerId);
//        }
//
//        chessGame.remove(allChessGames.get(0).getId());
//
//        assertThat(chessGameRepository.findAll()).hasSize(1);
//    }
//
//    private void assertPiecesSizeOfPlayers(Long playerId) throws SQLException {
//        List<PiecePositionEntity> piecesPositions = playerPiecePositionRepository.findAllByPlayerId(playerId);
//        assertThat(piecesPositions).hasSize(NUMBER_OF_PIECES_OF_ONE_PLAYER);
//    }
//}