package XO.Controllers;

import XO.Model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class GameController {

    private static final int beginX = 200;
    private static final int beginY = 200;
    private static final int endX = 650;


    private static final int padding = 10;


    ArrayList<Rectangle> blocks = new ArrayList<>();
    ArrayList<Label> blockTexts = new ArrayList<>();

    public int row;
    public int column;


    @FXML
    public void initialize()
    {


    }
    void blockMaker() {
        int n = (row>column)?row:column;
        int availabeSpace = (endX - beginX - (n - 1) * padding);
        int rectangleDim = availabeSpace / n;
        int stepOfMove = rectangleDim + padding;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Rectangle rectangle = new Rectangle(rectangleDim, rectangleDim);
                rectangle.relocate(beginX + j * stepOfMove, beginY + i * stepOfMove);
                Label label = new Label();
                label.setFont(Font.font(15));

                rectangle.setFill(Color.BLACK);

                label.relocate(rectangle.getLayoutX() + rectangleDim / 2.5, rectangle.getLayoutY() + rectangleDim / 2.5);
                label.setText("");
                blockTexts.add(label);
                blocks.add(rectangle);
            }
        }

    }
}
