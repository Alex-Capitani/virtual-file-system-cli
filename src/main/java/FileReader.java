import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;

public class FileReader {

    public void read(Path path) {
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        }
        catch (IOException e) {
            throw new UnsupportedOperationException("Erro ao ler o arquivo.");
        }
    }
}
