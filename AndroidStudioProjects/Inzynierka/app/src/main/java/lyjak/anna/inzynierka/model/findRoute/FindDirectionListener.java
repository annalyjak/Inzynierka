package lyjak.anna.inzynierka.model.findRoute;

import java.util.List;

import lyjak.anna.inzynierka.viewmodel.others.RouteBeetweenTwoPointsDTO;

/**
 * The listener for drawing polylines beetween actuall selected route's points
 * Created by Anna on 14.10.2017.
 */
public interface FindDirectionListener {

    /**
     * Method initiates finding directions beetwen two points on the route (clean auxiliary variables)
     */
    void onStartFindDirection();

    /**
     * Method downloads url of polylines beetwen two points on the route
     */
    void onSucceedFindDirection(List<RouteBeetweenTwoPointsDTO> route);

    /**
     * Method is nesecery for selecting if the current position of user is on the route
     */
    void onStoreFindDirection();
}
