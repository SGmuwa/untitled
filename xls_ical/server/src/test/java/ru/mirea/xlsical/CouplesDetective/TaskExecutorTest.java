package ru.mirea.xlsical.CouplesDetective;

import org.junit.Test;
import ru.mirea.xlsical.CouplesDetective.ViewerExcelCouples.DetectiveDate;
import ru.mirea.xlsical.Server.TaskExecutor;
import ru.mirea.xlsical.interpreter.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TaskExecutorTest {

    private static final PercentReady ready1 =
            new PercentReady(GlobalPercentReady.percentReady, 1f/3f);

    @Test
    public void pullPollStep() throws InterruptedException, IOException {
        PercentReady pr = new PercentReady(ready1, 1f/4f);
        TaskExecutor te = new TaskExecutor(new PercentReady(pr, 0.99f));
        te.add(new PackageToServer(null, new PercentReady(pr, 0.01f), null));
        te.step();
        PackageToClient ptc = te.take();

        assertNull(ptc.CalFile);
        assertEquals(0, ptc.Count);
        assertEquals("Ошибка: отстствуют критерии поиска.", ptc.Messages);
    }

    @Test
    public void sendSampleExcel() throws InterruptedException, IOException {
        PercentReady pr = new PercentReady(ready1, 1f/4f);
        ArrayList<File> excels = new ArrayList<>();
        excels.add(new File("tests\\IIT-3k-18_19-osen (2).xlsx"));

        ZonedDateTime now = ZonedDateTime.of(
                LocalDate.of(2018, 9, 1),
                LocalTime.MIN,
                ZoneId.of("Europe/Minsk")
        );

        TaskExecutor a = new TaskExecutor(new CoupleHistorian(new ExternalDataUpdater(
                excels,
                new ArrayList<>()
        ), new DetectiveDate(), true, now, new PercentReady(pr, 0.1f), new File("tests/ArrayListOfCouplesInCalendar_GOOD.dat")));
        a.add(new PackageToServer(null,
                new PercentReady(pr, 0.9f),
                new Seeker(
                        "ИКБО-04-16",
                        LocalDate.of(2018, 9, 1),
                        LocalDate.of(2018, 9, 3),
                        ZoneId.of("Europe/Minsk")
                )
        ));

        a.step();
        PackageToClient b = a.take();
        assertNotNull(b.CalFile);
        System.out.println(b.CalFile);
        assertEquals(3, b.Count);
    }

    @Test
    public void sendSampleExcelAllSem() throws InterruptedException, IOException {
        PercentReady pr = new PercentReady(ready1, 1f/4f);
        ArrayList<File> excels = new ArrayList<File>();
        excels.add(new File("tests\\IIT-3k-18_19-osen (2).xlsx"));

        ZonedDateTime now = ZonedDateTime.of(
                LocalDate.of(2018, 9, 1),
                LocalTime.MIN,
                ZoneId.of("Europe/Minsk")
        );

        CoupleHistorian historian = new CoupleHistorian(new ExternalDataUpdater(
                excels,
                new ArrayList<>()
        ), new DetectiveDate(), true, now, new PercentReady(pr, 0.9f), new File("tests/ArrayListOfCouplesInCalendar_GOOD.dat"));

        TaskExecutor a = new TaskExecutor(historian);
        a.add(new PackageToServer(null, new PercentReady(pr, 0.1f),
                new Seeker(
                        "ИКБО-04-16",
                        LocalDate.of(2018, 9, 1),
                        LocalDate.of(2018, 12, 31),
                        ZoneId.of("Europe/Minsk")
                )
        ));

        a.step();
        PackageToClient b = a.take();
        System.out.println(b.CalFile);
        assertNotNull(b.CalFile);
        assertEquals(232, b.Count);
    }

    @Test
    public void sendExcelAllSem() throws InterruptedException, IOException {
        PercentReady pr = new PercentReady(ready1, 1f/4f);
        ZonedDateTime now = ZonedDateTime.of(
                LocalDate.of(2018, 9, 1),
                LocalTime.MIN,
                ZoneId.of("Europe/Minsk")
        );

        // В этом тесте надо уточнить, чтобы код думал, что сейчас 1 сентября 2018 года,
        // чтобы построил расписание на осенний семестр 2018 года.
        CoupleHistorian historian = new CoupleHistorian(
                new PercentReady(pr, 0.9f),
                new File("tests/ArrayListOfCouplesInCalendar_test.dat"),
                now
        );

        TaskExecutor a = new TaskExecutor(historian);
        a.add(new PackageToServer(null,
                new PercentReady(pr, 0.1f),
                new Seeker(
                        "ИКБО-04-16",
                        LocalDate.of(2018, 9, 1),
                        LocalDate.of(2018, 12, 31),
                        ZoneId.of("Europe/Minsk")
                )
        ));

        a.step();
        PackageToClient b = a.take();
        System.out.println(b.CalFile);
        assertNotNull(b.CalFile);
        assertEquals(232, b.Count);
    }
}