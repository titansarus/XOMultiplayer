package XO.Exceptions;

import static XO.Constants.*;

public class NoUserLoginedException extends MyException {
    public NoUserLoginedException() {
        super(NO_USER_LOGINED_EXCEPTION);
    }
}
