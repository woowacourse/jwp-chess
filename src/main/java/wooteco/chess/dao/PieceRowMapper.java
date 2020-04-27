package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.factory.PieceConverter;
import wooteco.chess.dto.PieceDto;

public class PieceRowMapper implements RowMapper<List<PieceDto>> {

	@Override
	public List<PieceDto> mapRow(ResultSet resultSet) throws SQLException {
		return mapPiece(resultSet);
	}

	private List<PieceDto> mapPiece(ResultSet resultSet) throws SQLException {
		List<PieceDto> pieceDtos = new ArrayList<>();

		while (resultSet.next()) {
			String position = resultSet.getString("position");
			String name = resultSet.getString("name");
			pieceDtos.add(PieceDto.from(PieceConverter.convert(position, name)));
		}
		return pieceDtos;
	}
}
