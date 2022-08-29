package com.test;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesTests {

    ClassLoader classLoader = FilesTests.class.getClassLoader();

    @Test
    void zipTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files/Downloads.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("files/Downloads.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            //org.assertj.core.api.Assertions.assertThat(entry.getName()).isEqualTo("example1.csv");
            try (InputStream stream = zf.getInputStream(entry)){
                CSVReader reader = new CSVReader(new InputStreamReader(stream, StandardCharsets.UTF_8));{
                    List<String[]> csv = reader.readAll();
                    assertThat(csv).contains(
                            new String[]{"29.07.13", "West", "Joplin", "18707"},
                            new String[]{"30.07.13", "East", "Phyllis", "9939"}
                    );
                }
            }
            if (is != null) {
                is.close();
                is.close();
            }
        }
    }
}

