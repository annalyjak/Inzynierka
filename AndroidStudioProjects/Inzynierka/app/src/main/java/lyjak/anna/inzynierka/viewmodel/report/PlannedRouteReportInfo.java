package lyjak.anna.inzynierka.viewmodel.report;

import java.util.ArrayList;
import java.util.List;

import lyjak.anna.inzynierka.service.model.utils.DistanceAndDurationUtil;
import lyjak.anna.inzynierka.viewmodel.report.modelDTO.PlannedRouteForReportDTO;
import lyjak.anna.inzynierka.viewmodel.report.modelDTO.PointOfRouteForReportDTO;

/**
 * Created by Anna on 03.11.2017.
 */

public class PlannedRouteReportInfo {

    private PlannedRouteForReportDTO basicRouteInfo;
    private List<Point> infos;

    public PlannedRouteReportInfo(PlannedRouteForReportDTO route) {
        basicRouteInfo = route;
    }

    public void generateInfoForReport() {
        infos = new ArrayList<>();
        List<PointOfRouteForReportDTO> points = basicRouteInfo.getPoints();
        for(int i = 0; i < points.size() - 1 ; i++) {
            PointOfRouteForReportDTO first = points.get(i);
            PointOfRouteForReportDTO second = points.get(i + 1);
            infos.add(generatePoint(first, second));
        }
    }

    private Point generatePoint(PointOfRouteForReportDTO first, PointOfRouteForReportDTO second) {
        Point result = new Point();
        result.setNumer(first.getId());
        result.setName(first.getName() + " (" + first.getPoint().getLatitude() + ", "
                + first.getPoint().getLongitude() + ")");
        result.setStartPoint(first.getName());
        result.setEndPoint(second.getName());
        result.setDuration(first.getDuration());
        result.setDistance(first.getDistance());
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

    public String getAllFormatedDuration() {
        int hours = (basicRouteInfo.getDuration() / 60) / 60;
        int minuts = ((basicRouteInfo.getDuration() - (hours*60*60)) / 60);
        return (hours!=0? hours + " h " : "") + minuts + " min";
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

        public String getDurationHandMin() {
            int hours = (duration / 60) / 60;
            int minuts = ((duration - (hours*60*60)) / 60);
            return (hours!=0? hours + " h " : "") + minuts + " min";
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
