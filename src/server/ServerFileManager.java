package server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;

import static helper.Helper.BUFFER_SIZE;
import static helper.Helper.SERVER_FOLDER;

public class ServerFileManager extends Thread {

    private final Socket socket;

    public ServerFileManager(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] buffer = new byte[BUFFER_SIZE];

            String fileName = dataInputStream.readUTF();

            CheckedOutputStream checkedOutputStream = new CheckedOutputStream(
                    new FileOutputStream(new File(SERVER_FOLDER + fileName)), new Adler32());

            int read;
            while ((read = dataInputStream.read(buffer)) > 0) {
                checkedOutputStream.write(buffer, 0, read);
            }

            System.out.println(fileName + " : " + checkedOutputStream.getChecksum().getValue());

            checkedOutputStream.close();
            dataInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
