package lyjak.anna.inzynierka.model.modelDTO;

import org.junit.Test;

import lyjak.anna.inzynierka.model.realmObjects.PlannedRoute;
import lyjak.anna.inzynierka.model.utils.CreateModelDataUtil;

import static org.junit.Assert.*;

/**
 * Created by Anna on 13.11.2017.
 */
public class PlannedRouteForReportDTOTest {

    @Test
    public void getInstance() {
        PlannedRoute route = CreateModelDataUtil.createPlannedRoute("title");

        PlannedRouteForReportDTO instance = PlannedRouteForReportDTO.getInstance(route);

        assertEquals(instance.getTitle(), route.getTitle());
        assertEquals(instance.getDuration(), route.getDuration());
        assertEquals(instance.getDistance(), route.getDistance());
        assertEquals(instance.getDate(), route.getDate());
    }
}