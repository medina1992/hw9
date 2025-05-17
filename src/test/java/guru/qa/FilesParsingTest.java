package guru.qa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
//import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.model.Glossary;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;


import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;


public class FilesParsingTest {

    private ClassLoader cl = FilesParsingTest.class.getClassLoader();
    private static final Gson gson = new Gson();


    @Test
    void pdfFileParsingTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloaded = $("[href*='junit-user-guide-5.12.2.pdf']").download();

        try (PDDocument document = PDDocument.load(downloaded)) {
            String pdfText = new PDFTextStripper().getText(document);
            // Проверяем, что в тексте PDF есть имена авторов (пример)
            assertTrue(pdfText.contains("Stefan Bechtold"));
            assertTrue(pdfText.contains("Sam Brannen"));
            // Другие проверки можно добавить по тексту из pdfText
        }
    }

    @Test
    void xlsFileParsingTest() throws Exception {
        open("https://excelvba.ru/programmes/Teachers");
        File downloaded = $("[href*='https://ExcelVBA.ru/sites/default/files/teachers.xls']").download();

        try (InputStream stream = new FileInputStream(downloaded)) {
            Workbook workbook = WorkbookFactory.create(stream);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(3);
            Cell cell = row.getCell(2);
            String actualValue = cell.getStringCellValue();

            assertTrue(actualValue.replaceAll("\\s+", " ")
                    .contains("Суммарное количество часов планируемое на штатную по всем разделам плана должно составлять примерно 1500 час в год"));

            workbook.close();
        }
    }

    @Test
    void csvFileParsingTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("employees.csv");
             CSVReader csvReader = new CSVReader(new InputStreamReader(is))) {
            List<String[]> data = csvReader.readAll();
            Assertions.assertEquals(2, data.size());
            Assertions.assertArrayEquals(
                    new String[]{"198", "Donald", "Connell", "DOCONNEL", "650", "21-JUN-07", "SH_CLERK"},
                    data.get(0)
            );
            Assertions.assertArrayEquals(
                    new String[]{"199", "Douglas", "Grant", "DGRANT", "650", "13-JAN-08", "SH_CLERK"},
                    data.get(1)
            );

        }
    }

    @Test
    void zipFileParsingTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(
                cl.getResourceAsStream("zipforTest.zip")
        )) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }


        }


    }

    // dlya json
    @Test
    void jsonFileParsingTest() throws Exception {
        try (Reader reader = new InputStreamReader(
                cl.getResourceAsStream("glossary.json")
        )) {
            JsonObject actual = gson.fromJson(reader, JsonObject.class);
            Assertions.assertEquals("example glossary", actual.get("title").getAsString());
            Assertions.assertEquals(4654654, actual.get("ID").getAsInt());

            JsonObject inner = actual.get("glossary").getAsJsonObject();//vlojenniy obyeckt
            Assertions.assertEquals("SGML", inner.get("SortAs").getAsString());
            Assertions.assertEquals("Standard Generalized Markup Language", inner.get("GlossTerm").getAsString());

        }

    }

    @Test
    void jsonFileParsingImprovedTest() throws Exception {
        try (Reader reader = new InputStreamReader(
                cl.getResourceAsStream("glossary.json")
        )) {
            Glossary actual = gson.fromJson(reader, Glossary.class);
            Assertions.assertEquals("example glossary", actual.getTitle());
            Assertions.assertEquals(4654654, actual.getID());
            Assertions.assertEquals("SGML", actual.getGlossary().getSortAs());
            Assertions.assertEquals("Standard Generalized Markup Language", actual.getGlossary().getGlossTerm());

        }

    }


    @Test
    void zipFileParsingTest2() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("zipTestHW9.zip");
        assertNotNull(is, "ZIP file not found in resources!");

        try (ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                System.out.println("Found: " + name);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
                byte[] fileBytes = baos.toByteArray();

                if (name.endsWith(".csv")) {
                    String content = new String(fileBytes, StandardCharsets.UTF_8);
                    if (content.contains("Rachel")) {

                    }
                } else if (name.endsWith(".pdf")) {
                    try (PDDocument document = PDDocument.load(new ByteArrayInputStream(fileBytes))) {
                        String pdfText = new PDFTextStripper().getText(document);
                        if (pdfText.contains("This is a simple PDF file. Fun fun fun.")) {

                        }
                    }
                } else if (name.equals("sampleXLS1.xlsx")) {
                    try (InputStream stream = new ByteArrayInputStream(fileBytes)) {
                        Workbook workbook = WorkbookFactory.create(stream);


                        outer:
                        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                            Sheet sheet = workbook.getSheetAt(i);
                            for (Row row : sheet) {
                                for (Cell cell : row) {
                                    System.out.println("Cell: " + cell.toString());

                                    if (cell.getCellType() == CellType.STRING &&
                                            cell.getStringCellValue().contains("Input")) {
                                        break outer;
                                    }
                                }
                            }
                        }
                        workbook.close();
                    }
                }
            }
        }


    }
}




