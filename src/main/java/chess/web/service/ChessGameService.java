package chess.web.service;

import chess.domain.game.ChessGame;
import chess.repository.ChessGameRepository;
import chess.web.controller.dto.request.CreateGameRequestDto;
import chess.web.controller.dto.request.JoinGameRequestDto;
import chess.web.controller.dto.request.MoveRequestDto;
import chess.web.controller.dto.response.ChessGameResponseDto;
import chess.web.controller.dto.response.CreateGameResponseDto;
import chess.web.controller.dto.response.GameStatusResponseDto;
import chess.web.exception.GameNotExistsException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameRepository chessGameRepository;

    public ChessGameService(ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    public List<ChessGameResponseDto> getNotFulledChessGames() {
        List<ChessGame> notFulledChessGames = chessGameRepository.findAllBlackPlayerPasswordIsNull();
        return notFulledChessGames.stream()
            .map(ChessGameResponseDto::new)
            .collect(Collectors.toList());
    }

    public CreateGameResponseDto createNewChessGame(CreateGameRequestDto createGameRequestDto) {
        ChessGame chessGame = new ChessGame(createGameRequestDto);
        Long createdChessGameId = chessGameRepository.save(chessGame);
        return new CreateGameResponseDto(createdChessGameId, chessGame.getEncryptedWhitePlayerPassword());
    }

    public String joinGame(JoinGameRequestDto joinGameRequestDto) {
        ChessGame chessGame = chessGameRepository.findById(joinGameRequestDto.getGameId())
            .orElseThrow(GameNotExistsException::new);
        chessGame.joinBlackPlayerWithPassword(joinGameRequestDto.getRawBlackPlayerPassword());
        chessGameRepository.update(chessGame);
        return chessGame.getEncryptedBlackPlayerPassword();
    }

    public GameStatusResponseDto getGameStatus(Long gameId) {
        ChessGame chessGame = chessGameRepository.findById(gameId)
            .orElseThrow(GameNotExistsException::new);
        return new GameStatusResponseDto(chessGame);
    }

    public void endGame(Long gameId) {
        chessGameRepository.deleteById(gameId);
    }

    public void movePiece(MoveRequestDto moveRequestDTO, String encryptedPassword) {
        ChessGame chessGame = chessGameRepository.findById(moveRequestDTO.getGameId())
            .orElseThrow(GameNotExistsException::new);
        chessGame.validatePassword(encryptedPassword);
        String startPositionInput = moveRequestDTO.getStartPositionInput();
        String destinationInput = moveRequestDTO.getDestinationInput();
        chessGame.movePiece(startPositionInput, destinationInput);
        chessGameRepository.update(chessGame);
    }
}
