package XO.Exceptions;
import static XO.Constants.*;
public class UserAlreadyLoginedException extends MyException {
    public UserAlreadyLoginedException() {
        super(USER_ALREADY_LOGINED);
    }
}
