package chess.repository.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import chess.domain.Color;
import chess.domain.Position;
import chess.domain.piece.Piece;
import chess.domain.player.Player;
import chess.repository.dto.player.PlayerDto;

public class PlayerDtoAssembler {

    private PlayerDtoAssembler() {
    }

    public static Player toPlayer(final Long playerId, final PlayerDto playerDto) {
        final Color color = Color.from(playerDto.getColorName());
        final Map<Position, Piece> pieces = convertStringToPieces(color, playerDto.getPieces());
        return new Player(playerId, color, pieces);
    }

    public static Player toPlayer(final PlayerDto playerDto) {
        return toPlayer(playerDto.getId(), playerDto);
    }

    public static PlayerDto toPlayerDto(final Player player) {
        final Color color = player.getColor();
        final Map<Position, Piece> pieces = player.getPieces();
        return new PlayerDto(player.getId(), color.getName(), convertPiecesToString(pieces));
    }

    private static String convertPiecesToString(final Map<Position, Piece> pieces) {
        final List<String> values = new ArrayList<>();
        for (Entry<Position, Piece> entry : pieces.entrySet()) {
            final Position position = entry.getKey();
            final Piece piece = entry.getValue();
            values.add(concatPositionAndPiece(position, piece));
        }
        return String.join(",", values);
    }

    private static String concatPositionAndPiece(final Position position, final Piece piece) {
        return String.format("%s:%s", convertPositionToString(position), piece.getPieceName());
    }

    private static String convertPositionToString(final Position position) {
        return String.valueOf(position.getColumn()) + position.getRow();
    }

    private static Map<Position, Piece> convertStringToPieces(final Color color, final String pieceUnits) {
        return Arrays.stream(pieceUnits.split(","))
                .map(pieceUnit -> pieceUnit.split(":"))
                .collect(Collectors.toMap(
                        value -> Position.from(value[0]),
                        value -> PieceConvertor.convertToPiece(color, value[1])
                ));
    }
}
