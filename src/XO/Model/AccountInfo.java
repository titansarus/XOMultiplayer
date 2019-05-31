package XO.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AccountInfo {
    private String username;
    private Integer wins;
    private Integer loses;
    private Integer draws;
    private Integer rank = 0;


    public AccountInfo(String username, int wins, int loses, int draws) {
        this.username = username;
        this.wins = wins;
        this.loses = loses;
        this.draws = draws;
    }


    public static ArrayList<AccountInfo> accountInfoSorter(ArrayList<AccountInfo> accounts) {
        accounts.sort(((Comparator<AccountInfo>) (o1, o2) -> o2.wins - o1.wins)
                .thenComparing(((Comparator<AccountInfo>) (o1, o2) -> o1.loses - o2.loses)
                        .thenComparing(((Comparator<AccountInfo>) (o1, o2) -> o1.draws - o2.draws)
                                .thenComparing(Comparator.comparing(o -> o.username)))));
        return accounts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLoses() {
        return loses;
    }

    public void setLoses(Integer loses) {
        this.loses = loses;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
