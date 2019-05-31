package XO.Model;

import java.util.ArrayList;

public class Game {
    private Account player1;
    private Account player2;
    private boolean isPlayer1Undoed = false;
    private boolean isPlayer2Undoed = false;
    private boolean isWinnerDetemined = false;

    ArrayList<Move> moves = new ArrayList<>();
    private int row;
    private int column;
    private int[][] grid;
    private long UID;
    private int turn = 0;

    public static Account drawAccount = new Account("draw" , "draw");


    public Game(Account player1, Account player2, int row, int column, long UID) {
        this.player1 = player1;
        this.player2 = player2;
        this.row = row;
        this.column = column;
        this.grid = new int[row][column];
        this.UID = UID;
    }

    public static ArrayList<Game> findListOfPausedGames(String username, ArrayList<Game> games) {
        ArrayList<Game> result = new ArrayList<>();
        if (games != null && username != null && username.length() > 0) {
            for (int i = 0; i < games.size(); i++) {
                Game game = games.get(i);
                if (game != null && (game.player1.getUsername().equals(username) || game.player2.getUsername().equals(username))) {
                    result.add(game);
                }
            }
        }
        return result;
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

    public boolean isCompletelyFilled() {
        boolean isFilled = true;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
             if (grid[i][j]==0)
             {
                 isFilled = false;
                 return isFilled;
             }
            }
        }
        return isFilled;

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


    public Account findWinner() {
        int winner = -1;
        if (getRow() == 3 || getColumn() == 3) {
            winner = winCheck3();
        } else {
            winner = winCheck4();
        }
        if (winner == 1) {
            if (!isWinnerDetemined) {
                player1.incrementWins();
                player2.incrementLoses();
                isWinnerDetemined=true;
            }
            return player1;
        } else if (winner == 2) {
            if (!isWinnerDetemined) {
                player2.incrementWins();
                player1.incrementLoses();
                isWinnerDetemined=true;
            }
            return player2;
        }
        if (winner == -1 && isCompletelyFilled())
        {
            if (!isWinnerDetemined) {
                player1.incrementDraws();
                player2.incrementDraws();
                isWinnerDetemined=true;
            }
            return drawAccount;
        }
        return null;
    }

    private int winCheck4() {
        int[][] grid = this.getGrid();
        int numberOfRows = this.getRow();
        int numberOfColumns = this.getColumn();
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (i + 3 < numberOfRows) {
                    if (grid[i][j] == 1 && grid[i + 1][j] == 1 && grid[i + 2][j] == 1 && grid[i + 3][j] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i + 1][j] == 2 && grid[i + 2][j] == 2 && grid[i + 3][j] == 2) {
                        return 2;
                    }
                }
                if (j + 3 < numberOfColumns) {
                    if (grid[i][j] == 1 && grid[i][j + 1] == 1 && grid[i][j + 2] == 1 && grid[i][j + 3] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i][j + 1] == 2 && grid[i][j + 2] == 2 && grid[i][j + 3] == 2) {
                        return 2;
                    }
                }
                if (i + 3 < numberOfRows && j + 3 < numberOfColumns) {
                    if (grid[i][j] == 1 && grid[i + 1][j + 1] == 1 && grid[i + 2][j + 2] == 1 && grid[i + 3][j + 3] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i + 1][j + 1] == 2 && grid[i + 2][j + 2] == 2 && grid[i + 3][j + 3] == 2) {
                        return 2;
                    }
                }
                if (i + 3 < numberOfRows && j - 3 >= 0) {
                    if (grid[i][j] == 1 && grid[i + 1][j - 1] == 1 && grid[i + 2][j - 2] == 1 && grid[i + 3][j - 3] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i + 1][j - 1] == 2 && grid[i + 2][j - 2] == 2 && grid[i + 3][j - 3] == 2) {
                        return 2;
                    }
                }
            }
        }

        return -1;
    }

    private int winCheck3() {
        int[][] grid = this.getGrid();
        int numberOfRows = this.getRow();
        int numberOfColumns = this.getColumn();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (i + 2 < numberOfRows) {
                    if (grid[i][j] == 1 && grid[i + 1][j] == 1 && grid[i + 2][j] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i + 1][j] == 2 && grid[i + 2][j] == 2) {
                        return 2;
                    }
                }
                if (j + 2 < numberOfColumns) {
                    if (grid[i][j] == 1 && grid[i][j + 1] == 1 && grid[i][j + 2] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i][j + 1] == 2 && grid[i][j + 2] == 2) {
                        return 2;
                    }
                }
                if (i + 2 < numberOfRows && j + 2 < numberOfColumns) {
                    if (grid[i][j] == 1 && grid[i + 1][j + 1] == 1 && grid[i + 2][j + 2] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i + 1][j + 1] == 2 && grid[i + 2][j + 2] == 2) {
                        return 2;
                    }
                }
                if (i + 2 < numberOfRows && j - 2 >= 0) {
                    if (grid[i][j] == 1 && grid[i + 1][j - 1] == 1 && grid[i + 2][j - 2] == 1) {
                        return 1;
                    } else if (grid[i][j] == 2 && grid[i + 1][j - 1] == 2 && grid[i + 2][j - 2] == 2) {
                        return 2;
                    }
                }
            }
        }

        return -1;
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
