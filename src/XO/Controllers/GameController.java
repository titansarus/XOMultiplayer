package XO.Controllers;

import XO.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

import static XO.Constants.*;

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
    Timeline timeline;

    public void handleBack() {
        if (Container.scenes.size() > 0) {
            Container.scenes.removeLast();
            Container.stage.setScene(Container.scenes.getLast());
            Container.stage.show();
        }


    }

    public void timeLineGen() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            try {
                updateGameBoard();
                checkIfGameEnded();
                checkIfGamePuased();
            } catch (IOException e) {

            }
        }), new KeyFrame(Duration.millis(1378)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void checkIfGameEnded() throws IOException {
        String sendMessage = WIN_CHECK;
        Client.dos.writeUTF(sendMessage);
        String receivedMessage = Client.dis.readUTF();
        if (!receivedMessage.equals(NO_WINNER)) {
            if (receivedMessage.equals(YOU_WIN)) {
                Container.notificationShower(CONGRATS_YOU_WIN + "Winner: " + loginedUser_lbl.getText(), CONGRATS_YOU_WIN);
            } else if (receivedMessage.equals(YOU_LOSE)) {
                Container.notificationShower(SORRY_YOU_LOSE + "Loser: " + loginedUser_lbl.getText(), SORRY_YOU_LOSE);
            } else if (receivedMessage.equals(DRAW)) {
                Container.notificationShower(YOU_DRAW, YOU_DRAW);
            }
            timeLineStopper();
            handleBack();
        }
    }

    public void handleBtnPause() throws IOException {
        String sendMessage = PAUSE;
        Client.dos.writeUTF(sendMessage);
    }

    public void checkIfGamePuased() throws IOException {
        String sendMessage = CHECK_PAUSED;
        Client.dos.writeUTF(sendMessage);
        String receivedMessage = Client.dis.readUTF();
        if (receivedMessage.equals(YES_PAUSED)) {
            Container.notificationShower(GAME_PAUSED_NOTIFICATION, GAME_PAUSED_NOTIFICATION);
            timeLineStopper();
            handleBack();
        }
    }

    public void timeLineStopper() {
        timeline.stop();
    }

    public int row;

    public int column;

    public void handleBtnUndo() throws IOException {
        if (turn_lbl.getText().equals(loginedUser_lbl.getText())) {
            Container.ExceptionGenerator(NOT_YOUR_TURN_PROMPT);
            return;
        } else if (getFilledRectanglesCount() < 2) {
            Container.ExceptionGenerator(INVALID_UNDO_PROMPT);
        } else {
            String message = UNDO;
            Client.dos.writeUTF(message);
            String result = "";
            result = Client.dis.readUTF();
            Container.ExceptionGenerator(result);
        }
    }

    public int getFilledRectanglesCount() {

        int count = 0;
        for (int i = 0; i < blocks.size(); i++) {
            if (!blocks.get(i).getFill().equals(Color.BLACK)) {
                count++;
            }
        }

        return count;

    }


    @FXML
    public void initialize() {


    }

    public void insert(int i, int j) throws IOException {//TODO CHECK FOR WIN

        if (!turn_lbl.getText().equals(loginedUser_lbl.getText())) {
            Container.ExceptionGenerator(NOT_YOUR_TURN_PROMPT);
        } else {
            String out = INSERT + " " + i + " " + j + " " + loginedUser_lbl.getText();
            Client.dos.writeUTF(out);
            String result = Client.dis.readUTF();
            if (result.equals(DONE)) {
                updateGameBoard();
            } else {
                Container.ExceptionGenerator(result);
            }
        }

    }

    public void updateLoginedUser() throws IOException {
        Client.dos.writeUTF(LOGINED_USER);

        String user = Client.dis.readUTF();

        loginedUser_lbl.setText(user);
    }

    public void updateGameBoard() throws IOException {

        Client.dos.writeUTF(GIVE_COMPLETE_GAME_INFO);

        String out = Client.dis.readUTF();
        if (out.equals(NO_GAME)) {
            return;
        }

        String[] parts = out.split(" , ");

        String[] info = parts[0].split(" ");

        String[] board = parts[1].split(" ");
//
//        out = game.getRow() + " " +game.getColumn();
//        out = out+ " " + game.getPlayer1().getUsername() + " " + game.getPlayer2().getUsername();
//        out = out + " " + game.getTurnAccount().getUsername() + " , ";

        int row = Integer.parseInt(info[0]);
        int column = Integer.parseInt(info[1]);
        String player1 = info[2];
        String player2 = info[3];
        String turn = info[4];

        player1_lbl.setText(player1);
        player2_lbl.setText(player2);
        turn_lbl.setText(turn);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.printf("%s ", board[i * column + j]);
            }
            System.out.println();
            ;
        }
        blockPainter(board);


    }

    void blockPainter(String[] board) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                blocks.get(i * column + j).setFill(colorSelector(board[i * column + j]));

            }
        }
    }

    Color colorSelector(String string) {
        if (string.equals("1")) {
            return Color.BLUEVIOLET;
        } else if (string.equals("2")) {
            return Color.valueOf("#CC2233");
        }
        return Color.BLACK;
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
                label.setTextFill(Color.WHITE);
                label.relocate(beginX + j * stepOfMove, beginY + i * stepOfMove);
                label.setText(String.valueOf(i * row + j));

                rectangle.setFill(Color.BLACK);
                rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        int i = findNumberOfRcetangle(rectangle);
                        if (i != -1) {
                            try {
                                insert(i / column, i % column);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

//                label.relocate(rectangle.getLayoutX() + rectangleDim / 2.5, rectangle.getLayoutY() + rectangleDim / 2.5);
//                label.setText("");
                blockTexts.add(label);
                blocks.add(rectangle);
            }
        }

    }

    public int findNumberOfRcetangle(Rectangle rectangle) {
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == rectangle) {
                return i;
            }
        }
        return -1;
    }

}
