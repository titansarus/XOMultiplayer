package XO.Exceptions;

import static XO.Constants.*;

public class AccountExistException extends MyException {
    public AccountExistException() {
        super(ACCOUNT_EXIST);
    }
}
