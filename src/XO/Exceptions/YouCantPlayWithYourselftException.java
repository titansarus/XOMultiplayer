package XO.Exceptions;
import static XO.Constants.*;
public class YouCantPlayWithYourselftException extends MyException {
    public YouCantPlayWithYourselftException() {
        super(YOU_CANT_PLAY_WITH_YOURSELF);
    }
}
