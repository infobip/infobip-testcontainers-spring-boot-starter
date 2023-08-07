package com.infobip.testcontainers;

import org.junit.jupiter.api.BeforeAll;

import java.io.*;
import java.nio.file.*;

public class ReusableTestBase extends TestBase {

    @BeforeAll
    static void setup() {
        var home = System.getProperty("user.home");
        var filename = ".testcontainers.properties";
        var file = new File(home, filename);
        var tempFile = new File(home, filename + ".tmp");
        if (file.exists()) {
            file.renameTo(tempFile);
        }

        Path newFile;
        try {
            newFile = Files.writeString(Paths.get(home, filename), "testcontainers.reuse.enable=true");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Files.delete(newFile);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            tempFile.renameTo(file);
        }));
    }
}
