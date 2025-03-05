package ru.xopek.universalkits.api;

import ru.xopek.universalkits.UniversalKits;
import ru.xopek.universalkits.kits.Kit;

/**
 * Most Powerfull API as you cann see
 */
public class EKitsAPI {
    public static Kit getKitByName (String kitName) {
        return UniversalKits.getInst().getKits().get(kitName);
    }
}
