import processing.core.PApplet;
import processing.event.KeyEvent;

public class Main extends PApplet {

    Model model;

    public Main() {
        model = new Model();
    }

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    public void setup() {
        frameRate(10);
        noStroke();
        rectMode(CENTER);
        textAlign(CENTER, CENTER);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);
        if (keyCode == ENTER) {
            model = model.startGame();
        } else if (key == '0') {
            model.undo();
        } else if (key == '=') {
            model.increaseTargetScore();
            model = model.startGame();
        } else if (key == '-') {
            model.lowerTargetScore();
            model = model.startGame();
        } else if (keyCode == UP || keyCode == DOWN || keyCode == LEFT || keyCode == RIGHT) {
            model.update(keyCode);
        } else if (keyCode == ESC) {
            exit();
        }
    }

    @Override
    public void draw() {
        background(0, 0, 0);

        redraw();
        textInGame();

        if (model.isWin() || !model.movesAvailable()) {
            gameOver();
        }
    }

    @Override
    public void redraw() {
        super.redraw();
        fill(200, 200, 200);
        rect(width / 2, height / 2 + 50, 420, 420, 10);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                switch (model.getField(row, col)) {
                    case 0:
                        fill(0, 0, 0);
                        break;
                    case 2:
                        fill(255, 3, 23);
                        break;
                    case 4:
                        fill(92, 255, 205);
                        break;
                    case 8:
                        fill(184, 217, 237);
                        break;
                    case 16:
                        fill(72, 87, 255);
                        break;
                    case 32:
                        fill(217, 255, 105);
                        break;
                    case 64:
                        fill(216, 71, 255);
                        break;
                    case 128:
                        fill(255, 195, 32);
                        break;
                    case 512:
                        fill(87, 217, 255);
                        break;
                    case 1024:
                        fill(255, 176, 168);
                        break;
                    case 2048:
                        fill(203, 191, 255);
                        break;
                    default:
                        fill(255, 0, 0);
                }

                rect(width / 2 - 150 + 100 * col, height / 2 - 100 + 100 * row, 90, 90, 10);
                if (model.getField(row, col) != 0) {
                    fill(255, 255, 255);
                    textSize(35);
                    text(model.getField(row, col), width / 2 - 150 + 100 * col, height / 2 - 100 + 100 * row, 50);

                }
            }
        }
    }


    private void textInGame() {
        fill(132, 18, 16);
        textSize(72);
        text("Game 2048", width / 2, height / 8);

        textSize(48);
        text("Goal\n" + model.getTargetScore(), width / 6, height / 2);
        //text(model.getScore(), width / 6, height / 3 + 100);

        text("Score\n" + model.getScore(), width / 2 + width / 3, height / 2 + 75);
        text("Best Score\n" + model.getBestScore(), width / 2 + width / 3, height / 2 - 75);

    }

    private void gameOver() {
        fill(132, 18, 16, 250);
        rect(width / 2, height / 2, 500, 250);
        textSize(50);
        fill(0);
        text("You are Winner!", width / 2, height / 2 - 35);
        textSize(20);
        text("Press <Enter> to play again\nPress <Esc> to exit", width / 2, height / 2 + 75);
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}


