package lyjak.anna.inzynierka.model.modelDTO;

import org.junit.Test;

import java.util.Date;

import lyjak.anna.inzynierka.model.realmObjects.Route;

import static org.junit.Assert.*;

/**
 * Created by Anna on 13.12.2017.
 */
public class ActualRouteForReportDTOTest {

    @Test
    public void getInstance() {
        Route route = new Route();
        route.setDate(new Date(System.currentTimeMillis()));
        route.setEndDate(new Date(System.currentTimeMillis()));
        route.setStartDate(new Date(System.currentTimeMillis()));
        ActualRouteForReportDTO actualRouteForReportDTO = ActualRouteForReportDTO.getInstance(route);

        assertEquals(route.getStartDate(), actualRouteForReportDTO.getStartDate());
        assertEquals(route.getEndDate(), actualRouteForReportDTO.getEndDate());
    }

}