package XO.Exceptions;
import static XO.Constants.*;
public class NotYourTurnException extends MyException {
    public NotYourTurnException() {
        super(NOT_YOUR_TURN);
    }
}
