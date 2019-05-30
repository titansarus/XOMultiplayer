package XO.Exceptions;

import static XO.Constants.*;

public class NoAccountExistException extends MyException {
    public NoAccountExistException() {
        super(ACCOUNT_NOT_EXIST);
    }
}
