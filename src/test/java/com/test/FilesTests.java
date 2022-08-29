package com.test;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesTests {

    ClassLoader classLoader = FilesTests.class.getClassLoader();

    @Test
    void zipCsvReader() throws Exception {
        InputStream is = classLoader.getResourceAsStream("Downloads.zip");
        ZipInputStream zip = new ZipInputStream(is);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().contains("example1.csv")) {
                try {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zip, UTF_8));
                    List<String[]> csv = csvReader.readAll();
                    assertThat(csv).contains(
                            new String[]{"Date", "Region", "Name", "Amount"},
                            new String[]{"29.07.13", "West", "Joplin", "18707"},
                            new String[]{"30.07.13", "East", "Phyllis", "9939"}
                    );
                } finally {
                    is.close();
                }
            }
        }
    }

    @Test
    void zipXLSReaderTest() throws Exception {
        InputStream is = classLoader.getResourceAsStream("Downloads.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipFile zfile = new ZipFile(new File("src/test/resources/" + "Downloads.zip"));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().contains("example.xlsx")) {
                try (InputStream stream = zfile.getInputStream(entry)) {
                    XLS xls = new XLS(stream);
                    assertThat(
                            xls.excel.getSheetAt(0)
                                    .getRow(2)
                                    .getCell(1)
                                    .getStringCellValue()
                    ).contains("East");
                }
            }
        }

    }


    @Test
    void zipPdfReader() throws Exception {
        InputStream is = classLoader.getResourceAsStream("Downloads.zip");
        ZipInputStream zip = new ZipInputStream(is);
        ZipEntry entry;
        ZipFile zfile = new ZipFile(new File("src/test/resources/" + "Downloads.zip"));
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().contains("new.pdf")) {
                try (InputStream stream = zfile.getInputStream(entry)) {
                    PDF pdf = new PDF(stream);
                    assertThat(pdf.author).contains("Stefan Bechtold, Sam Brannen, Johannes Link, Matthias Merdes, Marc Philipp, Juliette de Rancourt, Christian Stein");

                }
            }
        }

    }
}
