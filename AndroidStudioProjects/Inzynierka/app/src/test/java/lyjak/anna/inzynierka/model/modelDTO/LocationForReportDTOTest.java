package lyjak.anna.inzynierka.model.modelDTO;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.model.realmObjects.RealmLocation;

import static org.junit.Assert.*;

/**
 * Created by Anna on 13.12.2017.
 */
public class LocationForReportDTOTest {

    static RealmLocation realmLocation;

    @Before
    public void createElements() {
        realmLocation = new RealmLocation(1.0,1.0);
    }

    private RealmLocation createRealmLocation(double lat, double lon) {
        return new RealmLocation(lat, lon);
    }

    @Test
    public void getInstance() {
        LocationForReportDTO loc = LocationForReportDTO.getInstance(realmLocation);
        assertEquals(realmLocation.getLongitude(), loc.getLongitude());
        assertEquals(realmLocation.getLatitude(), loc.getLatitude());
    }

    @Test
    public void getInstanceList() {
        List<RealmLocation> list = new ArrayList<>();
        list.add(createRealmLocation(1.0, 1.0));
        list.add(createRealmLocation(2.0, 1.0));
        list.add(createRealmLocation(3.0, 3.0));
        list.add(createRealmLocation(4.0, 1.0));

        List<LocationForReportDTO> results = LocationForReportDTO.getInstance(list);
        for(int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i).getLatitude(), results.get(i).getLatitude(), 0);
            assertEquals(list.get(i).getLongitude(), results.get(i).getLongitude(), 0);
        }
    }

}