package org.scify.moonwalker.app.game;

import java.util.*;

public class MoonPhasesController {

    protected List<MoonPhase> moonPhaseEnergyUnitsMap;

    public MoonPhasesController() {
        moonPhaseEnergyUnitsMap = new LinkedList<>();
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon.png", 100));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon1.png", 200));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon2.png", 300));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon3.png", 400));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon4.png", 500));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon5.png", 600));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon6.png", 700));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon7.png", 600));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon8.png", 500));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon9.png", 400));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon10.png", 300));
        moonPhaseEnergyUnitsMap.add(new MoonPhase("img/moon11.png", 200));
    }

    public MoonPhase getMoonPhaseForDay(int day) {
        return moonPhaseEnergyUnitsMap.get(day % moonPhaseEnergyUnitsMap.size());
    }
}
