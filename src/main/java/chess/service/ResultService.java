package chess.service;

import chess.dao.ResultDAO;
import chess.dao.UserDAO;
import chess.dto.RankingDTO;
import chess.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public final class ResultService {
    private final ResultDAO resultDAO;
    private final UserDAO userDAO;

    public ResultService(final ResultDAO resultDAO, final UserDAO userDAO) {
        this.resultDAO = resultDAO;
        this.userDAO = userDAO;
    }

    public List<RankingDTO> allUserResult() {
        List<RankingDTO> results = new ArrayList<>();
        List<UserDTO> users = userDAO.findAll();
        for (UserDTO user : users) {
            int userId = user.getId();
            int winCount = resultDAO.winCountByUserId(userId);
            int loseCount = resultDAO.loseCountByUserId(userId);
            results.add(new RankingDTO(userDAO.findNicknameById(userId), winCount, loseCount));
        }
        Collections.sort(results);
        return results;
    }

    public void saveGameResult(final String roomId, final int winnerId, final int loserId) {
        resultDAO.saveGameResult(roomId, winnerId, loserId);
    }
}
