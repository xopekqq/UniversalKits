package ru.xopek.universalkits.data;

import ru.xopek.universalkits.UniversalKits;

public class AnalyticsData {
    public static void addTotalFreeKits (int count) {
        UniversalKits.getInst().getConfig().set("analytics.total_free_kits", getTotalFreeKits() + count);
    }
    public static int getTotalFreeKits () {
        return UniversalKits.getInst().getConfig().getInt("analytics.total_free_kits");
    }
    public static void addTotalPaidKits (int count) {
        UniversalKits.getInst().getConfig().set("analytics.total_paid_kits", getTotalPaidKits() + count);
    }
    public static int getTotalPaidKits () {
        return UniversalKits.getInst().getConfig().getInt("analytics.total_paid_kits");
    }
    public static int getTotalKitsCount () {
        return getTotalFreeKits() + getTotalPaidKits();
    }
}
