package XO.Model;

import java.util.ArrayList;

public class Game {
    private Account player1;
    private Account player2;
    private boolean isPlayer1Undoed = false;
    private boolean isPlayer2Undoed = false;

    ArrayList<Move> moves = new ArrayList<>();
    private int row;
    private int column;
    private int[][] grid;
    private long UID;
    private int turn = 0;


    public Game(Account player1, Account player2, int row, int column, long UID) {
        this.player1 = player1;
        this.player2 = player2;
        this.row = row;
        this.column = column;
        this.grid = new int[row][column];
        this.UID = UID;
    }

    public void undo() {
        if (isUndoable()) {
            int lastIndex = moves.size() - 1;
            Move move = moves.get(lastIndex);

            grid[move.getRow()][move.getColumn()] = 0;

            moves.remove(lastIndex);
            changeUndoable();
            changeTurn();
        }
    }

    private void changeUndoable() {
        if (turn % 2 == 0) {
            isPlayer2Undoed = true;
        } else if (turn % 2 == 1) {
            isPlayer1Undoed = true;
        }
    }

    public boolean isUndoable() {
        return (turn % 2 == 0 && !isPlayer2Undoed()) || (turn % 2 == 1 && !isPlayer1Undoed());
    }


    public void insert(int i, int j, String username) {
        if (username.equals(player1.getUsername())) {
            grid[i][j] = 1;
        } else if (username.equals(player2.getUsername())) {
            grid[i][j] = 2;
        }
        addMove(i, j, turn);

        changeTurn();
    }


    private void addMove(int i, int j, int turn) {
        moves.add(new Move(i, j, turn));
    }

    private void changeTurn() {
        setTurn((turn + 1) % 2);
    }

    public static Game findGameByUID(long UID, ArrayList<Game> games) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i) != null && games.get(i).getUID() == UID) {
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

    public Account getTurnAccount() {
        if (turn % 2 == 0) {
            return player1;
        }
        return player2;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isPlayer1Undoed() {
        return isPlayer1Undoed;
    }

    public boolean isPlayer2Undoed() {
        return isPlayer2Undoed;
    }
}
