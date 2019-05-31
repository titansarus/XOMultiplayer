package XO.Exceptions;

import static XO.Constants.*;

public class InvalidUndoException extends MyException {
    public InvalidUndoException() {
        super(INVALID_UNDO);
    }
}
