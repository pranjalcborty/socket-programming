import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GenerateFiles {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            RandomAccessFile file = new RandomAccessFile(new File("client_files/file_" + i), "rw");
            file.setLength(10 * 1024 * 1024);
        }
    }
}
