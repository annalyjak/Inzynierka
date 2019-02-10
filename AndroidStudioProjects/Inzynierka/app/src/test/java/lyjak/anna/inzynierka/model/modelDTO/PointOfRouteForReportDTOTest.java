package lyjak.anna.inzynierka.model.modelDTO;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import lyjak.anna.inzynierka.model.realmObjects.PointOfRoute;
import lyjak.anna.inzynierka.model.realmObjects.RealmLocation;

import static org.junit.Assert.*;

/**
 * Created by Anna on 13.12.2017.
 */
public class PointOfRouteForReportDTOTest {

    PointOfRouteForReportDTO porfr;
    static PointOfRoute pointOfRoute;
    List<PointOfRoute> list;

    @Before
    public void createElements() {
        pointOfRoute = createPointOfRoute(1, 1.0, 1.0);
        pointOfRoute.setDistance(100);
        pointOfRoute.setDuration(100);
    }

    private static PointOfRoute createPointOfRoute(int i, double v, double v1) {
        return new PointOfRoute(i, new RealmLocation(v,v1));
    }

    @Test
    public void getInstance() {
        porfr = PointOfRouteForReportDTO.getInstance(pointOfRoute);
        assertEquals(pointOfRoute.getId(), porfr.getId());
        assertEquals(0, porfr.getDistance());
        assertEquals(0, porfr.getDuration());
        assertEquals(pointOfRoute.getName(), porfr.getName());
        assertEquals(pointOfRoute.getPoint().getLatitude(), porfr.getPoint().getLatitude(), 0);
        assertEquals(pointOfRoute.getPoint().getLongitude(), porfr.getPoint().getLongitude(), 0);
    }

    @Test
    public void getInstanceWithDistance() {
        porfr = PointOfRouteForReportDTO.getInstanceWithDistance(pointOfRoute);
        assertEquals(pointOfRoute.getId(), porfr.getId());
        assertEquals(pointOfRoute.getDistance(), porfr.getDistance());
        assertEquals(pointOfRoute.getDuration(), porfr.getDuration());
        assertEquals(pointOfRoute.getName(), porfr.getName());
        assertEquals(pointOfRoute.getPoint().getLatitude(), porfr.getPoint().getLatitude());
        assertEquals(pointOfRoute.getPoint().getLongitude(), porfr.getPoint().getLongitude());
    }

}