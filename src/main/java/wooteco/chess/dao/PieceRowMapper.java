package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.factory.PieceConverter;
import wooteco.chess.dto.PieceDTO;

public class PieceRowMapper implements RowMapper<List<PieceDTO>> {

	@Override
	public List<PieceDTO> mapRow(ResultSet resultSet) throws SQLException {
		return mapPiece(resultSet);
	}

	private List<PieceDTO> mapPiece(ResultSet resultSet) throws SQLException {
		List<PieceDTO> pieceDTOs = new ArrayList<>();

		while (resultSet.next()) {
			String position = resultSet.getString("position");
			String name = resultSet.getString("name");
			pieceDTOs.add(PieceDTO.from(PieceConverter.convert(position, name)));
		}
		return pieceDTOs;
	}
}
