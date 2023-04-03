package pro.sky.noskoff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Неверно переданные параметры")
public class InvalidSocksRequestException extends RuntimeException {
    public InvalidSocksRequestException(String message) {
        super(message);
    }
}
