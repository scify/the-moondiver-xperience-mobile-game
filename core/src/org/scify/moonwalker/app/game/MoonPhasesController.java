package org.scify.moonwalker.app.game;

import java.util.*;

public class MoonPhasesController {

    protected List<MoonPhase> moonPhases;

    public MoonPhasesController() {
        moonPhases = new LinkedList<>();
        moonPhases.add(new MoonPhase("img/moon.png", 20, 1));
        moonPhases.add(new MoonPhase("img/moon1.png", 45, 5));
        moonPhases.add(new MoonPhase("img/moon2.png", 70, 3));
        moonPhases.add(new MoonPhase("img/moon3.png", 95, 5));
        moonPhases.add(new MoonPhase("img/moon4.png", 120, 3));
        moonPhases.add(new MoonPhase("img/moon5.png", 95, 5));
        moonPhases.add(new MoonPhase("img/moon6.png", 70,3));
        moonPhases.add(new MoonPhase("img/moon7.png", 45, 5));
    }

    public MoonPhase getMoonPhaseForDay(int day) {
        int dayMod = day % 30;
        int daysPassed = 0;
        for(MoonPhase moonPhase : moonPhases) {
            daysPassed += moonPhase.getLastingDays();
            if(daysPassed > dayMod) {
                return moonPhase;
            }
        }
        return null;
    }

}
