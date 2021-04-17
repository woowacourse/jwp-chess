package chess.web.service;

import chess.domain.game.ChessGame;
import chess.web.controller.dto.request.MoveRequestDTO;
import chess.web.controller.dto.response.ChessGameResponseDto;
import chess.web.controller.dto.response.GameStatusResponseDto;
import chess.web.controller.dto.response.MoveResponseDto;
import chess.repository.ChessGameRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {

    private final ChessGameRepository chessGameRepository;

    public ChessGameService(ChessGameRepository chessGameRepository) {
        this.chessGameRepository = chessGameRepository;
    }

    public List<ChessGameResponseDto> getAllGames() {
        List<ChessGame> allChessGames = chessGameRepository.findAll();
        return allChessGames.stream()
            .map(ChessGameResponseDto::new)
            .collect(Collectors.toList());
    }

    public Long createNewChessGame(String gameTitle) {
        ChessGame chessGame = new ChessGame(gameTitle);
        return chessGameRepository.save(chessGame);
    }

    public GameStatusResponseDto getGameStatus(Long gameId) {
        ChessGame chessGame = chessGameRepository.findById(gameId);
        return new GameStatusResponseDto(chessGame);
    }

    public void deleteGame(Long gameId) {
        chessGameRepository.deleteById(gameId);
    }

    public MoveResponseDto movePiece(MoveRequestDTO moveRequestDTO) {
        ChessGame chessGame = chessGameRepository.findById(moveRequestDTO.getGameId());
        String startPositionInput = moveRequestDTO.getStartPositionInput();
        String destinationInput = moveRequestDTO.getDestinationInput();
        try {
            chessGame.movePiece(startPositionInput, destinationInput);
        } catch (IllegalArgumentException e) {
            return new MoveResponseDto(true, e.getMessage());
        }
        chessGameRepository.update(chessGame);
        return new MoveResponseDto(false);
    }
}
