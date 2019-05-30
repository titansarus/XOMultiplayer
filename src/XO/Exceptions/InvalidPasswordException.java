package XO.Exceptions;

import static XO.Constants.*;

public class InvalidPasswordException extends MyException {
    public InvalidPasswordException() {
        super(INVALID_PASSWORD);
    }
}
