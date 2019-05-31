package XO.Model;

public class Move {
    private int row;
    private int column;

    Move(int row, int column) {
        this.row = row;
        this.column = column;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

}
