package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static helper.Helper.*;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public class ClientManager {
    public static void main(String[] args) throws IOException, InterruptedException {
        String folder = getFolder(args);
        int concurrentUploads = getConcurrentUploads(args);
        System.out.println(format("Uploading files from %s with %d files concurrently.", folder, concurrentUploads));

        List<File> files = asList(requireNonNull(new File(folder).listFiles()));

        for (int i = 0; i <= ((files.size() / concurrentUploads) - 1) * concurrentUploads; i += concurrentUploads) {
            concurrentUpload(files.subList(i, i + concurrentUploads));
        }

        if (files.size() % concurrentUploads != 0) {
            concurrentUpload(files.subList((files.size() / concurrentUploads) * concurrentUploads, files.size()));
        }
    }

    private static void concurrentUpload(List<File> filesGroup) throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(filesGroup.size());

        for (File file : filesGroup) {
            new ClientFileManager(new Socket(IP, PORT), file, latch).start();
        }

        latch.await();
    }

    private static int getConcurrentUploads(String[] args) {
        return args.length == 2 ? parseInt(args[1]) : 1;
    }

    private static String getFolder(String[] args) {
        return args.length == 0 ? CLIENT_FOLDER : args[0];
    }
}
