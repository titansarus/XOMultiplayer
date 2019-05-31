package XO.Model;

public class Move {
    private int row;
    private int column;
    private int turn;

    Move(int row, int column, int turn) {
        this.row = row;
        this.column = column;
        this.turn = turn;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    public int getTurn() {
        return turn;
    }
}
