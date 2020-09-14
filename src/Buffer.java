import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Buffer {

    File file;
    private int score;

    Buffer() {
        file = new File("src\\scores.txt");
        if (file.exists()) {
            parse();
        }
    }

    public void parse() {
        try (Scanner scanner = new Scanner(file)) {
            score = Integer.parseInt(scanner.nextLine());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    void write(int score) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(score);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public int getScore() {
        return score;
    }
}
