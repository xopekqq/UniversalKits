package ru.xopek.universalkits.config;

import ru.xopek.universalkits.api.StringAPI;

public class KitsConfig {
    public static final long KIT_START_TIMEOUT = 1000 * 60 * 60 * 8;
    public static final long KIT_DONATE_TIMEOUT = 1000 * 60 * 60 * 24 * 7;

    public static final String kitsArgumentsException = StringAPI.asColor("&cСинтаксис: /ekits <действие> <дата | $>");
    public static final String kitsActionRequiresPlayer = StringAPI.asColor("&cДействие может выполнить только игрок!");
    public static final String kitNotFound = StringAPI.asColor("&cКит не найден!");
    public static final String databaseActionUserNotFound = StringAPI.asColor("&cИгрок не найден в бд!");
    public static final String kitsPrefix = StringAPI.asColor("&#7FFEF9Киты | &f");
    public static final String kitsInventorySlotsFull = kitsPrefix + StringAPI.asColor("У вас недостаточно свободных слотов в инвентаре!");
    public static final String kitTaken = kitsPrefix + "Набор &#7FFEF9{0} &fуспешно получен!";
    public static final String kitDelayIssue = kitsPrefix + StringAPI.asColor("&fСтоп! Вы уже &cвзяли &fнабор, следущий будет доступен через &c{delay}"); // {delay} <-
    public static final String kitPermission = kitsPrefix + StringAPI.asColor("&fУ вас недостаточно прав!");
    public static final String kitTakenTitle = StringAPI.asColor("&#7FFEF9&kl &6Набор Получен &#7FFEF9&kl");


}
