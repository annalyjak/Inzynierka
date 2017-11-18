package lyjak.anna.inzynierka.view.fragments.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
@Deprecated
public class LocationContent {

    public static List<LocationItem> ITEMS = new ArrayList<LocationItem>();

    public static LocationItem transferToLocationItem(int position, String location) {
        return  new LocationItem(String.valueOf(position), location);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class LocationItem {
        public final String id;
        public final String content;

        public LocationItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
