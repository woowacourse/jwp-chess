package chess.service;

import chess.dao.PlayerDAO;
import chess.dao.RoomDAO;
import chess.domain.entity.Player;
import chess.domain.entity.Room;
import chess.dto.player.JoinUserDTO;
import chess.dto.player.PlayerDTO;
import chess.dto.player.PlayersDTO;
import chess.exception.DuplicatedNicknameException;
import chess.exception.InvalidPasswordException;
import org.springframework.stereotype.Service;

@Service
public final class PlayerService {
    private final PlayerDAO playerDAO;
    private final RoomDAO roomDAO;

    public PlayerService(final PlayerDAO playerDAO, final RoomDAO roomDAO) {
        this.playerDAO = playerDAO;
        this.roomDAO = roomDAO;
    }

    public PlayersDTO usersParticipatedInGame(final String roomId) {
        Room room = roomDAO.findPlayersByRoomId(roomId);
        String blackUser = playerDAO.findNicknameById(room.getBlackUserId());
        String whiteUser = playerDAO.findNicknameById(room.getWhiteUserId());
        return new PlayersDTO(blackUser, whiteUser);
    }

    public int userIdByNickname(final String nickname) {
        return playerDAO.findUserIdByNickname(nickname);
    }

    public PlayersDTO participatedUsers(String id) {
        PlayersDTO playersDTO = new PlayersDTO();
        playersDTO.setBlackUser(blackUserParticipatedInGame(id));
        playersDTO.setWhiteUser(whiteUserParticipatedInGame(id));
        return playersDTO;
    }

    public String blackUserParticipatedInGame(final String roomId) {
        return playerDAO.findBlackUserByRoomId(roomId);
    }

    public String whiteUserParticipatedInGame(final String roomId) {
        return playerDAO.findWhiteUserByRoomId(roomId);
    }

    public int registerUser(final JoinUserDTO joinUserDTO) {
        try {
            validatesDuplicatedUser(joinUserDTO);
            return playerDAO.save(joinUserDTO);
        } catch (DuplicatedNicknameException e) {
            return login(joinUserDTO);
        }
    }

    private void validatesDuplicatedUser(final JoinUserDTO joinUserDTO) {
        if (playerDAO.findByNickname(joinUserDTO.getNickname()) != null) {
            throw new DuplicatedNicknameException();
        }
    }

    private int login(final JoinUserDTO user) {
        int userId = playerDAO.findIdByNicknameAndPassword(user.getNickname(), user.getPassword());
        if (userId == 0) {
            throw new InvalidPasswordException();
        }
        return userId;
    }

    public void checkPassword(final String id, final String password) {
        if (playerDAO.findPlayerByIdAndPassword(id, password) == null) {
            throw new InvalidPasswordException();
        }
    }

    public PlayerDTO getUser(final String id, final String password) {
        Player player = playerDAO.findByPlayerIdAndPassword(id, password);
        return new PlayerDTO(player);
    }
}
