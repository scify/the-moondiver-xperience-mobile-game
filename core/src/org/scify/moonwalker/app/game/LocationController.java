package org.scify.moonwalker.app.game;

import java.util.LinkedList;
import java.util.List;

public class LocationController {

    protected List<Location> locations;

    public LocationController() {
        locations = new LinkedList<>();
        Location greece = new Location("Ελλάδα", "img/episode_map/athens.png", 1600, 1080 - 950, "GreekItem");
        Location uk = new Location("Ηνωμένο Βασίλειο", "img/episode_map/london.png", 900, 1080 - 420, "UKItem");
        Location france = new Location("Γαλλία", "img/episode_map/paris.png", 965, 1080 - 540, "FrenchItem");
//        Location poland = new Location("Πολωνία", "img/globe.png", 510, 190, "PolishItem");
        Location spain = new Location("Ισπανία", "img/episode_map/madrid.png", 725, 1080 - 870, "SpanishItem");
        Location italy = new Location("Ιταλία", "img/episode_map/milan.png", 1130, 1080 - 680, "ItalianItem");
        Location germany = new Location("Γερμανία", "img/episode_map/berlin.png", 1220, 1080 - 400, "GermanItem");
        Location norway = new Location("Νορβηγία", "img/episode_map/oslo.png", 1170, 1080 - 90, "NordicItem");
        
        greece.addOtherLocationAndDistance(uk, 2500);
        greece.addOtherLocationAndDistance(france, 1800);
//        greece.addOtherLocationAndDistance(poland, 1500);
        greece.addOtherLocationAndDistance(spain, 2200);
        greece.addOtherLocationAndDistance(italy, 700);
        greece.addOtherLocationAndDistance(germany, 1600);
        greece.addOtherLocationAndDistance(norway, 2700);

        uk.addOtherLocationAndDistance(greece, 2500);
        uk.addOtherLocationAndDistance(france, 400);
//        uk.addOtherLocationAndDistance(poland, 1400);
        uk.addOtherLocationAndDistance(spain, 1200);
        uk.addOtherLocationAndDistance(italy, 1400);
        uk.addOtherLocationAndDistance(germany, 900);
        uk.addOtherLocationAndDistance(norway, 1100);

        france.addOtherLocationAndDistance(greece, 1800);
        france.addOtherLocationAndDistance(uk, 400);
//        france.addOtherLocationAndDistance(poland, 1300);
        france.addOtherLocationAndDistance(spain, 1000);
        france.addOtherLocationAndDistance(italy, 1000);
        france.addOtherLocationAndDistance(germany, 800);
        france.addOtherLocationAndDistance(norway, 1300);

//        poland.addOtherLocationAndDistance(greece, 1500);
//        poland.addOtherLocationAndDistance(uk, 1400);
//        poland.addOtherLocationAndDistance(france, 1300);
//        poland.addOtherLocationAndDistance(spain, 2200);
//        poland.addOtherLocationAndDistance(italy, 1300);
//        poland.addOtherLocationAndDistance(germany, 500);
//        poland.addOtherLocationAndDistance(norway, 1000);

        spain.addOtherLocationAndDistance(greece, 2200);
        spain.addOtherLocationAndDistance(uk, 1200);
        spain.addOtherLocationAndDistance(france, 1000);
//        spain.addOtherLocationAndDistance(poland, 2200);
        spain.addOtherLocationAndDistance(italy, 1300);
        spain.addOtherLocationAndDistance(germany, 1800);
        spain.addOtherLocationAndDistance(norway, 2400);

        italy.addOtherLocationAndDistance(greece, 700);
        italy.addOtherLocationAndDistance(uk, 1400);
        italy.addOtherLocationAndDistance(france, 1000);
//        italy.addOtherLocationAndDistance(poland, 1300);
        italy.addOtherLocationAndDistance(spain, 1300);
        italy.addOtherLocationAndDistance(germany, 1100);
        italy.addOtherLocationAndDistance(norway, 2000);

        germany.addOtherLocationAndDistance(greece, 1600);
        germany.addOtherLocationAndDistance(uk, 900);
        germany.addOtherLocationAndDistance(france, 800);
//        germany.addOtherLocationAndDistance(poland, 500);
        germany.addOtherLocationAndDistance(spain, 1800);
        germany.addOtherLocationAndDistance(italy, 1100);
        germany.addOtherLocationAndDistance(norway, 800);

        norway.addOtherLocationAndDistance(greece, 2700);
        norway.addOtherLocationAndDistance(uk, 1100);
        norway.addOtherLocationAndDistance(france, 1300);
//        norway.addOtherLocationAndDistance(poland, 1000);
        norway.addOtherLocationAndDistance(spain, 2400);
        norway.addOtherLocationAndDistance(italy, 2000);
        norway.addOtherLocationAndDistance(germany, 800);

        locations.add(greece);
        locations.add(uk);
        locations.add(france);
//        locations.add(poland);
        locations.add(spain);
        locations.add(italy);
        locations.add(germany);
        locations.add(norway);
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
}
