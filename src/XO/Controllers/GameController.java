package XO.Controllers;

import XO.Client;
import XO.Model.Game;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

import static XO.Constants.GIVE_COMPLETE_GAME_INFO;
import static XO.Constants.LOGINED_USER;

public class GameController {

    @FXML
    public Label player1_lbl;

    @FXML
    public Label loginedUser_lbl;

    @FXML
    public Label player2_lbl;

    @FXML
    public Label turn_lbl;

    private static final int beginX = 200;
    private static final int beginY = 200;
    private static final int endX = 650;


    private static final int padding = 10;


    ArrayList<Rectangle> blocks = new ArrayList<>();
    ArrayList<Label> blockTexts = new ArrayList<>();

    public int row;
    public int column;


    @FXML
    public void initialize() {


    }

    public void updateLoginedUser() throws IOException {
        Client.dos.writeUTF(LOGINED_USER);

        String user = Client.dis.readUTF();

        loginedUser_lbl.setText(user);
    }

    public void updateGameBoard() throws IOException{

        Client.dos.writeUTF(GIVE_COMPLETE_GAME_INFO);

        String out = Client.dis.readUTF();

        String[] parts = out.split(" , ");

        String[] info = parts[0].split(" ");

        String[] board = parts[1].split(" ");
//
//        out = game.getRow() + " " +game.getColumn();
//        out = out+ " " + game.getPlayer1().getUsername() + " " + game.getPlayer2().getUsername();
//        out = out + " " + game.getTurnAccount().getUsername() + " , ";

        int row = Integer.parseInt(info[0]);
        int column = Integer.parseInt(info[1]);
        String player1 =  info[2];
        String player2 =  info[3];
        String turn = info[4];

        player1_lbl.setText(player1);
        player2_lbl.setText(player2);
        turn_lbl.setText(turn);

        for (int i =0;i<row;i++)
        {
            for (int j =0;j<column;j++)
            {
                System.out.printf("%s " , board[i*row+j]);
            }
            System.out.println();;
        }

    }



    void blockMaker() {
        int n = (row > column) ? row : column;
        int availabeSpace = (endX - beginX - (n - 1) * padding);
        int rectangleDim = availabeSpace / n;
        int stepOfMove = rectangleDim + padding;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                Rectangle rectangle = new Rectangle(rectangleDim, rectangleDim);
                rectangle.relocate(beginX + j * stepOfMove, beginY + i * stepOfMove);
                Label label = new Label();
                label.setFont(Font.font(15));

                rectangle.setFill(Color.BLACK);
//                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        Client.dos.writeUTF();
//
//                    }
//                });

                label.relocate(rectangle.getLayoutX() + rectangleDim / 2.5, rectangle.getLayoutY() + rectangleDim / 2.5);
                label.setText("");
                blockTexts.add(label);
                blocks.add(rectangle);
            }
        }

    }

}
