package XO.Model;

import java.util.ArrayList;

public class Game {
    private Account player1;
    private Account player2;
    private int row;
    private int column;
    private int[][] grid;
    private long UID;


    public Game(Account player1, Account player2, int row, int column , long UID) {
        this.player1 = player1;
        this.player2 = player2;
        this.row = row;
        this.column = column;
        this.grid = new int[row][column];
        this.UID = UID;
    }

    public static Game findGameByUID(long UID , ArrayList<Game> games)
    {
        for (int i =0;i<games.size();i++)
        {
            if(games.get(i)!=null && games.get(i).getUID()==UID)
            {
                return games.get(i);
            }
        }
        return null;
    }

    public Account getPlayer1() {
        return player1;
    }

    public void setPlayer1(Account player1) {
        this.player1 = player1;
    }

    public Account getPlayer2() {
        return player2;
    }

    public void setPlayer2(Account player2) {
        this.player2 = player2;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public long getUID() {
        return UID;
    }

    public void setUID(long UID) {
        this.UID = UID;
    }
}
