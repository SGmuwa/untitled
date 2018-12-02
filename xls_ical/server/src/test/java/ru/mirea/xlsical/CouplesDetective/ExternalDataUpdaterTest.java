package ru.mirea.xlsical.CouplesDetective;

import org.junit.Test;
import ru.mirea.xlsical.CouplesDetective.xl.ExcelFileInterface;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExternalDataUpdaterTest {

    @Test
    public void run() throws IOException {
        ExternalDataUpdater edu = new ExternalDataUpdater();
        assertNotNull(edu.pathToCache);
        assertTrue(edu.pathToCache.canWrite());
        edu.run();
        assertTrue(edu.isAlive());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail("Cancel test.");
        }
        assertTrue(edu.isAlive());
        assertEquals("Матчин Василий Тимофеевич, старший преподаватель кафедры инструментального и прикладного программного обеспечения.", edu.findTeacher("Матчин В.Т."));
        // 63 файла в бакалавре
        // 22 файла в магистратуре
        // 25 файла в аспирантуре
        // 1 файл в колледже
        // -10 pdf
        // .xls 101 файл.
        ArrayList<ExcelFileInterface> files = edu.openTablesFromExternal();
        // Осторожно! Число со временем тестирования может меняться!
        /* <a ref="view-source:https://www.mirea.ru/education/schedule-main/schedule/">
         * сюда</a> и проверитье количество соответствий с ".xls"*/
        assertEquals(files.size(), 101);
        for (ExcelFileInterface file : files) {
            file.close();
        }
        edu.interrupt();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            fail("Cancel test.");
        }
        assertFalse(edu.isAlive());
    }
}