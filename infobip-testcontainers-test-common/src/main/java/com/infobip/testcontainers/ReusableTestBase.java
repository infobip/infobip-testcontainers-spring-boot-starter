package com.infobip.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(classes = Main.class)
public class ReusableTestBase {

    private static final String HOME = System.getProperty("user.home");
    private static final String FILENAME = ".testcontainers.properties";
    private static final File FILE = new File(HOME, FILENAME);
    private static final File TEMP_FILE = new File(HOME, FILENAME + ".tmp");

    @BeforeAll
    static void createTestcontainersPropertiesFile() {
        if (FILE.exists()) {
            FILE.renameTo(TEMP_FILE);
        }

        TestcontainersConfiguration.getInstance().updateUserConfig("testcontainers.reuse.enable", "true");
    }

    @AfterAll
    static void cleanupTestcontainersPropertiesFile() {
        FILE.delete();

        if (TEMP_FILE.exists()) {
            TEMP_FILE.renameTo(FILE);
        }
    }
}
