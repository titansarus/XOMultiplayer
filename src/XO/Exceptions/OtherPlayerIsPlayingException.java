package XO.Exceptions;
import static XO.Constants.*;

public class OtherPlayerIsPlayingException extends MyException {
    public OtherPlayerIsPlayingException() {
        super(OTHER_PLAYER_PLAYING);
    }
}
