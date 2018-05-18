package org.scify.moonwalker.app.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LocationController {

    public static final String NO_MISSION = "(Καμία)";

    protected List<Location> locations;
    protected Location greece;
    protected Location germany;
    protected static boolean selectFirstMiddleOfNowhere = true;

    public LocationController() {
        locations = new LinkedList<>();
        String imgPath = "img/locations/";
        String conversationsPath = "conversations/";
        greece = new Location("Αθήνα", imgPath + "athens/", 1600, 1080 - 950, "Πάρε τον\nΗλιακό Συλλέκτη\nX5000", conversationsPath + "episode_athens.json");
        Location uk = new Location("Λονδίνο", imgPath + "london/", 900, 1080 - 420, "Πάρε τον\nΗλιακό Συλλέκτη\nX2000", conversationsPath + "episode_london.json");
        Location france = new Location("Παρίσι", imgPath + "paris/", 965, 1080 - 540, "Πάρε την\n Μπαταρία", conversationsPath + "episode_paris.json");
        Location spain = new Location("Μαδρίτη", imgPath + "madrid/", 725, 1080 - 870, "Πάρε τους\nπλαϊνούς\nκινητήρες", conversationsPath + "episode_madrid.json");
        Location italy = new Location("Μιλάνο", imgPath + "milan/", 1130, 1080 - 680, "Πάρε τον\nΗλιακό Συλλέκτη\nX3000", conversationsPath + "episode_milan.json");
        germany = new Location("Βερολίνο", imgPath + "berlin/", 1220, 1080 - 400, "Πάρε τον\nκεντρικό\nκινητήρα", conversationsPath + "episode_berlin.json");
        Location norway = new Location("Όσλο", imgPath + "oslo/", 1170, 1080 - 90, "Πάρε τον\nΗλιακό Συλλέκτη\nX4000", conversationsPath + "episode_oslo.json");
//        Location poland = new Location("Πολωνία", "img/globe.png", 510, 190, "PolishItem");
        greece.addOtherLocationAndDistance(uk, 2500);
        greece.addOtherLocationAndDistance(france, 1800);
        greece.addOtherLocationAndDistance(spain, 2200);
        greece.addOtherLocationAndDistance(italy, 700);
        greece.addOtherLocationAndDistance(germany, 1600);
        greece.addOtherLocationAndDistance(norway, 2700);
        //        greece.addOtherLocationAndDistance(poland, 1500);

        uk.addOtherLocationAndDistance(greece, 2500);
        uk.addOtherLocationAndDistance(france, 400);
        uk.addOtherLocationAndDistance(spain, 1200);
        uk.addOtherLocationAndDistance(italy, 1400);
        uk.addOtherLocationAndDistance(germany, 900);
        uk.addOtherLocationAndDistance(norway, 1100);
        //        uk.addOtherLocationAndDistance(poland, 1400);

        france.addOtherLocationAndDistance(greece, 1800);
        france.addOtherLocationAndDistance(uk, 400);
        france.addOtherLocationAndDistance(spain, 1000);
        france.addOtherLocationAndDistance(italy, 1000);
        france.addOtherLocationAndDistance(germany, 800);
        france.addOtherLocationAndDistance(norway, 1300);
        //        france.addOtherLocationAndDistance(poland, 1300);

        spain.addOtherLocationAndDistance(greece, 2200);
        spain.addOtherLocationAndDistance(uk, 1200);
        spain.addOtherLocationAndDistance(france, 1000);
        spain.addOtherLocationAndDistance(italy, 1300);
        spain.addOtherLocationAndDistance(germany, 1800);
        spain.addOtherLocationAndDistance(norway, 2400);
//        spain.addOtherLocationAndDistance(poland, 2200);
        italy.addOtherLocationAndDistance(greece, 700);
        italy.addOtherLocationAndDistance(uk, 1400);
        italy.addOtherLocationAndDistance(france, 1000);
        italy.addOtherLocationAndDistance(spain, 1300);
        italy.addOtherLocationAndDistance(germany, 1100);
        italy.addOtherLocationAndDistance(norway, 2000);
        //        italy.addOtherLocationAndDistance(poland, 1300);

        germany.addOtherLocationAndDistance(greece, 1600);
        germany.addOtherLocationAndDistance(uk, 900);
        germany.addOtherLocationAndDistance(france, 800);

        germany.addOtherLocationAndDistance(spain, 1800);
        germany.addOtherLocationAndDistance(italy, 1100);
        germany.addOtherLocationAndDistance(norway, 800);
        //        germany.addOtherLocationAndDistance(poland, 500);

        norway.addOtherLocationAndDistance(greece, 2700);
        norway.addOtherLocationAndDistance(uk, 1100);
        norway.addOtherLocationAndDistance(france, 1300);
        norway.addOtherLocationAndDistance(spain, 2400);
        norway.addOtherLocationAndDistance(italy, 2000);
        norway.addOtherLocationAndDistance(germany, 800);
        //        norway.addOtherLocationAndDistance(poland, 1000);

//        poland.addOtherLocationAndDistance(greece, 1500);
//        poland.addOtherLocationAndDistance(uk, 1400);
//        poland.addOtherLocationAndDistance(france, 1300);
//        poland.addOtherLocationAndDistance(spain, 2200);
//        poland.addOtherLocationAndDistance(italy, 1300);
//        poland.addOtherLocationAndDistance(germany, 500);
//        poland.addOtherLocationAndDistance(norway, 1000);

        locations.add(germany);
        locations.add(spain);
        locations.add(uk);
        locations.add(italy);
        locations.add(norway);
        locations.add(greece);
        locations.add(france);
    }

    public List<Location> getLocations() {
        return locations;
    }

    protected static LocationController instance;

    public static LocationController getInstance() {
        if (instance == null) {
            instance = new LocationController();
        }

        return instance;

    }

    public Location getNowhereLocation(String name, int posX, int posY) {
        if (selectFirstMiddleOfNowhere) {
            selectFirstMiddleOfNowhere = false;
            return new Location(name, "img/locations/nowhere/", posX, posY, NO_MISSION, "");
        } else {
            selectFirstMiddleOfNowhere = true;
            return new Location(name, "img/locations/nowhere2/", posX, posY, NO_MISSION, "");
        }
    }
}
