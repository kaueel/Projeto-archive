import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import com.company.FileHandler;

public class FileHandlerTest {
    FileHandler fileHandler;

    @BeforeEach
    public void initializeFileHandlerComponent() throws IOException {
        this.fileHandler = new FileHandler();
    }

    @Test
    public void writeTest() {
        Assertions.assertTrue(this.fileHandler.writeFile("teste.txt", "testeToBeCompressed.txt"));
    }
}
