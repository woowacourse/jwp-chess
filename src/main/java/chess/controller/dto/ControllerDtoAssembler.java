package chess.controller.dto;

import java.util.stream.Collectors;

import chess.controller.dto.response.ColorResponse;
import chess.controller.dto.response.GameResponse;
import chess.controller.dto.response.GameStatusResponse;
import chess.controller.dto.response.PieceResponse;
import chess.controller.dto.response.PlayerResponse;
import chess.controller.dto.response.PlayerScoresResponse;
import chess.controller.dto.response.PlayersResponse;
import chess.controller.dto.response.PositionResponse;
import chess.service.dto.response.ColorResponseDto;
import chess.service.dto.response.GameResponseDto;
import chess.service.dto.response.GameStatusResponseDto;
import chess.service.dto.response.PieceResponseDto;
import chess.service.dto.response.PlayerResponseDto;
import chess.service.dto.response.PlayerScoresResponseDto;
import chess.service.dto.response.PlayersResponseDto;
import chess.service.dto.response.PositionResponseDto;

public class ControllerDtoAssembler {

    private ControllerDtoAssembler() {
    }

    public static GameResponse gameResponse(final GameResponseDto gameResponseDto) {
        return new GameResponse(gameResponseDto.getGameId(),
                playersResponse(gameResponseDto.getPlayersResponseDto()),
                gameResponseDto.getFinished(), gameResponseDto.getPromotable(),
                colorResponse(gameResponseDto.getCurrentTurnColor()));
    }

    public static GameStatusResponse gameStatusResponse(final GameStatusResponseDto gameStatusResponseDto) {
        return new GameStatusResponse(gameStatusResponseDto.getId(),
                gameStatusResponseDto.getTitle(), gameStatusResponseDto.getFinished());
    }

    private static PlayersResponse playersResponse(final PlayersResponseDto playersResponseDto) {
        return new PlayersResponse(playersResponseDto.getPlayerResponseDtos()
                .entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> colorResponse(entry.getKey()),
                        entry -> playerResponse(entry.getValue())
                ))
        );
    }

    private static PlayerResponse playerResponse(final PlayerResponseDto playerResponseDto) {
        return new PlayerResponse(playerResponseDto.getPieceUnits()
                .entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> positionResponse(entry.getKey()),
                        entry -> pieceResponse(entry.getValue())
                )));
    }

    private static PositionResponse positionResponse(final PositionResponseDto positionResponseDto) {
        return new PositionResponse(positionResponseDto.getPosition());
    }

    private static PieceResponse pieceResponse(final PieceResponseDto pieceResponseDto) {
        return new PieceResponse(pieceResponseDto.getPiece());
    }

    private static ColorResponse colorResponse(final ColorResponseDto colorResponseDto) {
        return new ColorResponse(colorResponseDto.getColor());
    }

    public static PlayerScoresResponse playerScoresResponse(final PlayerScoresResponseDto playerScoresResponseDto) {
        return new PlayerScoresResponse(playerScoresResponseDto.getPlayerScores());
    }
}
