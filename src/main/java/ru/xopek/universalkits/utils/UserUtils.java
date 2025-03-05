package ru.xopek.universalkits.utils;

import ru.xopek.universalkits.config.KitsConfig;
import ru.xopek.universalkits.database.UserEntity;

public class UserUtils {

    public static boolean isTimeouted(UserEntity userEntity, boolean isDonateKitType) {
        long now = System.currentTimeMillis();

        long possibleDifference = isDonateKitType ? KitsConfig.KIT_DONATE_TIMEOUT : KitsConfig.KIT_START_TIMEOUT;

        return (now - (isDonateKitType ? userEntity.getKitDonateTs() : userEntity.getKitStartTs())) < possibleDifference;
    }
    public static long getExpiryTime (UserEntity userEntity, boolean isDonateKitType) {
        long possibleDifference = isDonateKitType ? KitsConfig.KIT_DONATE_TIMEOUT : KitsConfig.KIT_START_TIMEOUT;

        return possibleDifference + (isDonateKitType ? userEntity.getKitDonateTs() : userEntity.getKitStartTs());
    }
    public static long getExpirtyDifference (UserEntity userEntity, boolean isDonateKitType) {
        return System.currentTimeMillis() - (isDonateKitType ? userEntity.getKitDonateTs() : userEntity.getKitStartTs());
    }
    public static long getLonpoolDifference (UserEntity userEntity, boolean isDonateKitType) {
        return isDonateKitType ? KitsConfig.KIT_DONATE_TIMEOUT : KitsConfig.KIT_START_TIMEOUT;
    }
    public static long getUserExpiresAt (UserEntity userEntity, boolean isDonateKitType) {
        long now = System.currentTimeMillis();
        long possibleDifference = isDonateKitType ? KitsConfig.KIT_DONATE_TIMEOUT : KitsConfig.KIT_START_TIMEOUT;

        return possibleDifference - (now - (isDonateKitType ? userEntity.getKitDonateTs() : userEntity.getKitStartTs()));
    }
    public static String appendDiff(long diff_, long end) {
        long diff = diff_ - end;


        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        StringBuilder sb = new StringBuilder();
        if(Math.abs(diffDays) > 0) sb.append(Math.abs(diffDays) + "д ");
        if(Math.abs(diffHours) > 0) sb.append(Math.abs(diffHours) + "ч ");
        if(Math.abs(diffMinutes) > 0) sb.append(Math.abs(diffMinutes) + "м ");
        sb.append(Math.abs(diffSeconds) + "сек ");

        return sb.toString();
    }
}
