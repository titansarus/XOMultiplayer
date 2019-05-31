package XO.Exceptions;

import static XO.Constants.*;

public class InvalidRowColNumberException extends MyException {
    public InvalidRowColNumberException() {
        super(INVALID_ROW_COL_NUMBER);
    }
}
