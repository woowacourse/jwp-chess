package chess.service;

import chess.model.dto.GameResultDto;
import chess.model.dto.UserNameDto;
import chess.model.dto.UserNamesDto;
import chess.model.repository.ChessResultDao;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    private static final ChessResultDao CHESS_RESULT_DAO = ChessResultDao.getInstance();

    public UserNamesDto getUsers() {
        return new UserNamesDto(CHESS_RESULT_DAO.findUserNames());
    }

    public GameResultDto getResult(UserNameDto userNameDto) {
        return CHESS_RESULT_DAO.findWinOrDraw(userNameDto.getUserName())
            .orElseThrow(IllegalArgumentException::new);
    }
}
