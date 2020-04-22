package wooteco.chess.view;

import wooteco.chess.dto.ContinueCommandDto;
import wooteco.chess.dto.StartCommandDto;

public interface InputView {

    StartCommandDto askStartCommand();

    ContinueCommandDto askContinueCommand();
}
