package XO.Model;

import java.util.ArrayList;

public class Account {

    private String username;
    private String password;

    private int wins =0;
    private int loses =0;
    private int draws = 0;

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

    public static boolean accountExist(String username, ArrayList<Account> accounts) {
        return findAccount(username, accounts) != null;
    }

    public static void deleteAccount(String username, ArrayList<Account> accounts) {
        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                if (account != null && account.getUsername().equals(username)) {
                    accounts.remove(account);
                    return;
                }
            }
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    void incrementWins()
    {
        setWins(getWins()+1);
    }

    void incrementLoses()
    {
        setLoses(getLoses()+1);
    }

    void incrementDraws()
    {
        setDraws(getDraws()+1);
    }

    public int getDraws() {
        return draws;
    }

    private void setDraws(int draws) {
        this.draws = draws;
    }

    public int getWins() {
        return wins;
    }

    private void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    private void setLoses(int loses) {
        this.loses = loses;
    }
}
