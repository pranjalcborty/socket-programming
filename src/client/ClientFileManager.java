package client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

import static helper.Helper.BUFFER_SIZE;

public class ClientFileManager extends Thread {
    private final Socket socket;
    private final File file;
    private final CountDownLatch latch;

    public ClientFileManager(Socket socket, File file, CountDownLatch latch) {
        this.socket = socket;
        this.file = file;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] buffer = new byte[BUFFER_SIZE];

            CheckedInputStream checkedInputStream = new CheckedInputStream(new FileInputStream(file), new Adler32());

            dataOutputStream.writeUTF(file.getName());

            int read;
            while ((read = checkedInputStream.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, read);
            }

            System.out.println(file.getName() + " : " + checkedInputStream.getChecksum().getValue());

            checkedInputStream.close();
            dataOutputStream.close();

            latch.countDown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
