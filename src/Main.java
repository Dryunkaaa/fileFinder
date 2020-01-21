import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    private static ExecutorService service = Executors.newFixedThreadPool(15);
    private static List<Callable<Void>> tasks = new ArrayList<>();
    private static String[] FILE_EXTENSIONS = new String[]{
            ".js", ".twig", ".php", ".html"
    };

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String content = "function getAddress";
        String path = "C:\\Users\\Andrey\\Desktop\\localeximlab";
        getAllTasks(new File(path), content);
        List<Future<Void>> futures = service.invokeAll(tasks);
        for (Future<Void> future : futures) {
            future.get();
        }
        System.out.println("End");
        service.shutdownNow();
    }

    public static void getAllTasks(File directory, String content) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getAllTasks(file, content);
                } else if (isExtensionExist(file)) {
                    Callable<Void> callable = () -> {
                        String contentOfFile = readContentFromFile(file);
                        if (contentOfFile.contains(content)) {
                            System.out.println(file.getAbsolutePath());
                        }
                        return null;
                    };
                    tasks.add(callable);
                }
            }
        }
    }

    public static String readContentFromFile(File file) {
        String result = null;
        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8.name())) {
            while (sc.hasNextLine()) {
                result += sc.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }

    public static boolean isExtensionExist(File file) {
        for (String extension : FILE_EXTENSIONS) {
            if (file.getAbsolutePath().endsWith(extension)) return true;
        }
        return false;
    }
}
