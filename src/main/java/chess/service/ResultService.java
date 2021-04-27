package chess.service;

import chess.dao.PlayerDAO;
import chess.dao.ResultDAO;
import chess.domain.entity.Player;
import chess.dto.result.RankingDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public final class ResultService {
    private final ResultDAO resultDAO;
    private final PlayerDAO playerDAO;

    public ResultService(final ResultDAO resultDAO, final PlayerDAO playerDAO) {
        this.resultDAO = resultDAO;
        this.playerDAO = playerDAO;
    }

    public List<RankingDTO> allUserResult() {
        List<RankingDTO> results = new ArrayList<>();
        List<Player> players = playerDAO.findAll();
        for (Player player : players) {
            int userId = player.getId();
            int winCount = resultDAO.winCountByUserId(userId);
            int loseCount = resultDAO.loseCountByUserId(userId);
            results.add(new RankingDTO(playerDAO.findNicknameById(userId), winCount, loseCount));
        }
        Collections.sort(results);
        return results;
    }

    public void saveGameResult(final String roomId, final int winnerId, final int loserId) {
        resultDAO.saveGameResult(roomId, winnerId, loserId);
    }
}
