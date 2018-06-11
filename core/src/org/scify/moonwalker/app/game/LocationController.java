package org.scify.moonwalker.app.game;

import java.util.LinkedList;
import java.util.List;

public class LocationController {

    public static final String NO_MISSION = "(Καμία)";

    protected List<Location> locations;
    protected Location initialLocation;
    protected Location afterInitialLocation;
    protected static boolean selectFirstMiddleOfNowhere = false;
    protected static String CONVERSATIONS_PATH = "conversations/locations/";
    protected static String CONVERSATION_MAIN_FILE_NAME = "main_conversation.json";
    protected static String CONVERSATION_SUCCESS_FILE_NAME = "success_conversation.json";
    protected static String LOCATION_IMG_PATH = "img/locations/";

    protected static LocationController instance;

    public static LocationController getInstance() {
        if (instance == null) {
            instance = new LocationController();
        }
        return instance;
    }

    private LocationController() {
        locations = new LinkedList<>();
        Location greece = createLocation("Αθήνα", "athens", 1600, 1080 - 950, "Πάρε το\nΗλιακό Πάνελ\nX5000");
        Location uk = createLocation("Λονδίνο",  "london", 900, 1080 - 420, "Πάρε το\nΗλιακό Πάνελ\nX2000");
        Location france = createLocation("Παρίσι", "paris", 965, 1080 - 540, "Πάρε την\nΜπαταρία");
        Location spain = createLocation("Μαδρίτη", "madrid", 725, 1080 - 870, "Πάρε τους\nπλαϊνούς\nκινητήρες");
        Location italy = createLocation("Μιλάνο", "milan", 1130, 1080 - 680, "Πάρε το\nΗλιακό Πάνελ\nX3000");
        Location germany = createLocation("Βερολίνο", "berlin", 1220, 1080 - 400, "Πάρε τον\nκεντρικό\nκινητήρα");
        Location norway = createLocation("Όσλο", "oslo", 1170, 1080 - 90, "Πάρε το\nΗλιακό Πάνελ\nX4000");
        // todo add moon as last location
        greece.addOtherLocationAndDistance(uk, 2500);
        greece.addOtherLocationAndDistance(france, 1800);
        greece.addOtherLocationAndDistance(spain, 2200);
        greece.addOtherLocationAndDistance(italy, 700);
        greece.addOtherLocationAndDistance(germany, 1600);
        greece.addOtherLocationAndDistance(norway, 2700);

        uk.addOtherLocationAndDistance(greece, 2500);
        uk.addOtherLocationAndDistance(france, 400);
        uk.addOtherLocationAndDistance(spain, 1200);
        uk.addOtherLocationAndDistance(italy, 1400);
        uk.addOtherLocationAndDistance(germany, 900);
        uk.addOtherLocationAndDistance(norway, 1100);

        france.addOtherLocationAndDistance(greece, 1800);
        france.addOtherLocationAndDistance(uk, 400);
        france.addOtherLocationAndDistance(spain, 1000);
        france.addOtherLocationAndDistance(italy, 1000);
        france.addOtherLocationAndDistance(germany, 800);
        france.addOtherLocationAndDistance(norway, 1300);

        spain.addOtherLocationAndDistance(greece, 2200);
        spain.addOtherLocationAndDistance(uk, 1200);
        spain.addOtherLocationAndDistance(france, 1000);
        spain.addOtherLocationAndDistance(italy, 1300);
        spain.addOtherLocationAndDistance(germany, 1800);
        spain.addOtherLocationAndDistance(norway, 2400);

        italy.addOtherLocationAndDistance(greece, 700);
        italy.addOtherLocationAndDistance(uk, 1400);
        italy.addOtherLocationAndDistance(france, 1000);
        italy.addOtherLocationAndDistance(spain, 1300);
        italy.addOtherLocationAndDistance(germany, 1100);
        italy.addOtherLocationAndDistance(norway, 2000);

        germany.addOtherLocationAndDistance(greece, 1600);
        germany.addOtherLocationAndDistance(uk, 900);
        germany.addOtherLocationAndDistance(france, 800);

        germany.addOtherLocationAndDistance(spain, 1800);
        germany.addOtherLocationAndDistance(italy, 1100);
        germany.addOtherLocationAndDistance(norway, 800);

        norway.addOtherLocationAndDistance(greece, 2700);
        norway.addOtherLocationAndDistance(uk, 1100);
        norway.addOtherLocationAndDistance(france, 1300);
        norway.addOtherLocationAndDistance(spain, 2400);
        norway.addOtherLocationAndDistance(italy, 2000);
        norway.addOtherLocationAndDistance(germany, 800);

        locations.add(germany);
        locations.add(spain);
        locations.add(uk);
        locations.add(italy);
        locations.add(norway);
        locations.add(greece);
        locations.add(france);

        initialLocation = greece;
        afterInitialLocation = germany;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public static boolean isSelectFirstMiddleOfNowhere() {
        return selectFirstMiddleOfNowhere;
    }

    public Location getNowhereLocation(String name, int posX, int posY) {
        Location ret;
        if (selectFirstMiddleOfNowhere) {
            ret = createLocation(name, "nowhere", posX, posY, NO_MISSION);
        } else {
            ret = createLocation(name, "nowhere2", posX, posY, NO_MISSION);
        }
        return  ret;
    }

    public void resetSelectFirstMiddleOfNowhere() {
        selectFirstMiddleOfNowhere = false;
    }

    public void toggleSelectFirstMiddleOfNowhere() {
        selectFirstMiddleOfNowhere = !selectFirstMiddleOfNowhere;
    }

    protected Location createLocation(String name, String resDirName, int posX, int posY, String mission) {
        return new Location(name, LOCATION_IMG_PATH + resDirName + "/", posX, posY, mission,
                CONVERSATIONS_PATH + resDirName + "/" + CONVERSATION_MAIN_FILE_NAME,
                CONVERSATIONS_PATH + resDirName + "/" + CONVERSATION_SUCCESS_FILE_NAME,
                CONVERSATIONS_PATH + "conversation_after_quiz_failure.json",
                CONVERSATIONS_PATH + resDirName + "/" + "arrival_conversation.json"
        );
    }

    public Location getInitialLocation() {
        return initialLocation;
    }

    public Location getLocationAfter(Location location) {
        int index = locations.indexOf(location) + 1;
        if (index >= locations.size())
            return null;
        else
            return locations.get(index);
    }

    public boolean isLastLocation(Location currentLocation) {
        return locations.indexOf(currentLocation) == locations.size() - 1;
    }

    public Location getAfterInitialLocation() {
        return afterInitialLocation;
    }

    public static void setSelectFirstMiddleOfNowhere(boolean selectFirstMiddleOfNowhere) {
        LocationController.selectFirstMiddleOfNowhere = selectFirstMiddleOfNowhere;
    }
}
