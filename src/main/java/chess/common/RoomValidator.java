package chess.common;

import chess.controller.dto.RoomRequestDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class RoomValidator implements Validator {
    private static final String EMPTY_MESSAGE = "패스워드를 입력해주세요.";

    @Override
    public boolean supports(Class<?> clazz) {
        return RoomRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoomRequestDto room = (RoomRequestDto) target;
        if (room.isLocked()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty", EMPTY_MESSAGE);
        }
    }
}
