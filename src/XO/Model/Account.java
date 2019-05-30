package XO.Model;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Account {

    private int highscore = 0;
    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static boolean isAccountPasswordRight(String username, String password, ArrayList<Account> accounts) {
        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (account != null) {
                    if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Account findAccount(String username, ArrayList<Account> accounts) {
        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (account != null && account.getUsername().equals(username)) {
                    return account;
                }
            }
        }
        return null;
    }


    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
