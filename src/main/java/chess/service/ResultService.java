package chess.service;

import chess.dto.repository.GameResultDto;
import chess.dto.repository.UserNamesDto;
import chess.dto.view.GameInformationDto;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Team;
import chess.model.domain.state.MoveState;
import chess.model.repository.ChessGameEntity;
import chess.model.repository.ResultEntity;
import chess.model.repository.ResultRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    private static final String KING_CAPTURED = MoveState.KING_CAPTURED.getMessage();

    private final ResultRepository resultRepository;

    public ResultService(
        ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public UserNamesDto getUsers() {
        List<String> userNames = new ArrayList<>();
        for (ResultEntity resultEntity : resultRepository.findAll()) {
            userNames.add(resultEntity.getUserName());
        }
        return new UserNamesDto(userNames);
    }

    public GameResultDto getResult(String userName) {
        ResultEntity resultEntity = resultRepository.findByUserName(userName)
            .orElseThrow(IllegalArgumentException::new);
        return new GameResultDto(resultEntity);
    }

    public void updateResult(GameInformationDto gameInformationDto) {
        String state = gameInformationDto.getState();
        if (KING_CAPTURED.equals(state)) {
            setGameResult(gameInformationDto.makeTeamScore(), gameInformationDto.makeUserNames());
        }
    }

    public void setGameResult(TeamScore teamScore, Map<Team, String> userNames) {
        for (Team team : Team.values()) {
            ResultEntity resultEntity = resultRepository.findByUserName(userNames.get(team))
                .orElseThrow(IllegalAccessError::new);
            resultEntity.updateWinOrLose(teamScore, team);
            resultRepository.save(resultEntity);
        }
    }

    public void setGameResult(ChessGameEntity chessGameEntity) {
        TeamScore teamScore = chessGameEntity.makeTeamScore();
        Map<Team, String> userNames = chessGameEntity.makeUserNames();
        setGameResult(teamScore, userNames);
    }
}
