package lyjak.anna.inzynierka.viewmodel.report;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import lyjak.anna.inzynierka.service.model.realm.PlannedRoute;
import lyjak.anna.inzynierka.service.model.realm.PointOfRoute;
import lyjak.anna.inzynierka.service.utils.DistanceAndDurationUtil;

/**
 * Created by Anna on 03.11.2017.
 */

public class PlannedRouteReportInfo {

    private PlannedRoute basicRouteInfo;
    private List<Point> infos;

    public PlannedRouteReportInfo(PlannedRoute route) {
        basicRouteInfo = route;
    }

    public void generateInfoForReport() {
        infos = new ArrayList<>();
        RealmList<PointOfRoute> points = basicRouteInfo.getPoints();
        for(int i = 0; i < points.size() - 1 ; i++) {
            PointOfRoute first = points.get(i);
            PointOfRoute second = points.get(i + 1);
            infos.add(generatePoint(first, second));
        }
    }

    private Point generatePoint(PointOfRoute first, PointOfRoute second) {
        Point result = new Point();
        result.setNumer(first.getId());
        result.setName(first.getName() + " (" + first.getPoint().getLatitude() + ", "
                + first.getPoint().getLongitude() + ")");
        result.setStartPoint(first.getName());
        result.setEndPoint(second.getName());
        result.setDistance(DistanceAndDurationUtil.getDistanceInKm(first.getPoint(), second.getPoint()));
//        result.setDuration();
        return result;
    }

    public List<Point> getInfoForReport() {
        if (infos == null) {
            generateInfoForReport();
        }
        return infos;
    }

    public int getAllDistance() {
        return basicRouteInfo.getDistance();
    }

    public int getAllDuration() {
        return basicRouteInfo.getDuration();
    }

    public class Point {
        private int numer;
        private String name;
        private String startPoint;
        private String endPoint;
        private float distance;
        private int duration;

        public Point() {
        }

        public Point(int numer, String name, String startPoint, String endPoint, int distance, int duration) {
            this.numer = numer;
            this.name = name;
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.distance = distance;
            this.duration = duration;
        }

        public int getNumer() {
            return numer;
        }

        public void setNumer(int numer) {
            this.numer = numer;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartPoint() {
            return startPoint;
        }

        public void setStartPoint(String startPoint) {
            this.startPoint = startPoint;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "numer=" + numer +
                    ", name='" + name + '\'' +
                    ", startPoint='" + startPoint + '\'' +
                    ", endPoint='" + endPoint + '\'' +
                    ", distance=" + distance +
                    ", duration=" + duration +
                    '}';
        }
    }
}
