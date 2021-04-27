package chess.common;

import chess.controller.dto.RoomRequestDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@Profile("http")
public class RoomValidator implements Validator {
    private static final String EMPTY_MESSAGE = "패스워드를 입력해주세요.";

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RoomRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoomRequestDto room = (RoomRequestDto) target;
        if (room.isLocked()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "empty", EMPTY_MESSAGE);
        }
    }

}
