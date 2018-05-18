package org.scify.moonwalker.app.game;

import java.util.*;

public class MoonPhasesController {

    protected List<MoonPhase> moonPhases;

    protected static final String FIRST_QUARTER_MOON_PHASE_IMG_PATH = "img/moon_phases/first_quarter.png";
    protected static final String FULL_MOON_PHASE_IMG_PATH = "img/moon_phases/full_moon.png";
    protected static final String NEW_MOON_PHASE_IMG_PATH = "img/moon_phases/new_moon.png";
    protected static final String THIRD_QUARTER_PHASE_IMG_PATH = "img/moon_phases/third_quarter.png";
    protected static final String WANING_CRESCENT_PHASE_IMG_PATH = "img/moon_phases/waning_crescent.png";
    protected static final String WANING_GIBBOUS_PHASE_IMG_PATH = "img/moon_phases/waning_gibbous.png";
    protected static final String WAXING_CRESCENT_PHASE_IMG_PATH = "img/moon_phases/waxing_crescent.png";
    protected static final String WAXING_GIBBOUS_PHASE_IMG_PATH = "img/moon_phases/waxing_gibbous.png";

    public MoonPhasesController() {
        moonPhases = new LinkedList<>();
        moonPhases.add(new MoonPhase(FULL_MOON_PHASE_IMG_PATH, 100, 3));
        moonPhases.add(new MoonPhase(WANING_GIBBOUS_PHASE_IMG_PATH, 80, 5));
        moonPhases.add(new MoonPhase(THIRD_QUARTER_PHASE_IMG_PATH, 50,3));
        moonPhases.add(new MoonPhase(WANING_CRESCENT_PHASE_IMG_PATH, 20, 5));
        moonPhases.add(new MoonPhase(NEW_MOON_PHASE_IMG_PATH, 10, 1));
        moonPhases.add(new MoonPhase(WAXING_CRESCENT_PHASE_IMG_PATH, 20, 5));
        moonPhases.add(new MoonPhase(FIRST_QUARTER_MOON_PHASE_IMG_PATH, 50, 3));
        moonPhases.add(new MoonPhase(WAXING_GIBBOUS_PHASE_IMG_PATH, 80, 5));
    }

    public MoonPhase getMoonPhaseForDay(int day) {
        int dayMod = (day + 2) % 30;
        int daysPassed = 0;
        for(MoonPhase moonPhase : moonPhases) {
            daysPassed += moonPhase.getLastingDays();
            if(daysPassed >= dayMod) {
                return moonPhase;
            }
        }
        return null;
    }

    public int getUnitsOfMoonPhase(MoonPhase moonPhase, int inventorySize) {
        int ret = moonPhase.energyUnits;
        if (inventorySize < 3)
            return ret;
        else if (inventorySize == 3)
            return (int)(ret * 1.5);
        else if (inventorySize == 4)
            return ret * 2;
        else if (inventorySize == 5)
            return (int)(ret * 2.5);
        else
            return ret * 3;
    }

}
