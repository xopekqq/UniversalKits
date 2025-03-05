package ru.xopek.universalkits.kits;

import ru.xopek.universalkits.UniversalKits;
import ru.xopek.universalkits.api.StringAPI;
import ru.xopek.universalkits.utils.InventoryUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class KitsSerializer {
    public static void serialize(ConfigurationSection config) {
        ConfigurationSection sec = config.getConfigurationSection("kits");

        // Получаем ключи без сортировки
        List<String> orderedKeys = new ArrayList<>(sec.getKeys(false));

        for (String key : orderedKeys) {
            ConfigurationSection kitData = sec.getConfigurationSection(key);

            String name = kitData.getString("name");
            KitType kitType = KitType.valueOf(kitData.getString("type").toUpperCase());
            String permission = kitData.getString("permission");

            ConfigurationSection items = kitData.getConfigurationSection("items");

            ArrayList<ItemStack> itemsList = new ArrayList<>();

            if (items == null) {
                getLogger().info("Cannot invoke, items is empty for kit=" + key + " with type " + kitType);
                continue; // Пропускаем этот набор, если items пуст
            }

            for (String itemKey : items.getKeys(false)) {
                ConfigurationSection item = items.getConfigurationSection(itemKey);

                Material material = Material.valueOf(item.getString("material").toUpperCase());
                int amount = 1;

                if (item.contains("amount")) {
                    amount = item.getInt("amount");
                }

                String title = null;
                if (item.contains("display_name")) {
                    title = StringAPI.asColor(item.getString("display_name"));
                }

                ItemStack is = new ItemStack(material, amount);
                ItemMeta im = is.getItemMeta();

                if (title != null) {
                    im.setDisplayName(title);
                }

                is.setItemMeta(im);

                if (item.contains("color")) {
                    LeatherArmorMeta lch = (LeatherArmorMeta) im.clone();

                    String[] possibleColorList = item.getString("color").split(",");

                    lch.setColor(
                            Color.fromRGB(
                                    Integer.parseInt(possibleColorList[0].replace(" ", "")),
                                    Integer.parseInt(possibleColorList[1].replace(" ", "")),
                                    Integer.parseInt(possibleColorList[2].replace(" ", ""))
                            )
                    );

                    is.setItemMeta(lch);
                }

                if (item.contains("enchantments")) {
                    for (String enchantKey : item.getStringList("enchantments")) {
                        if (enchantKey.length() <= 1) {
                            throw new IllegalArgumentException("Enchant Data Must be NotNull! for kit=" + key);
                        }

                        String enchantName = enchantKey.split(";")[0];
                        int enchantLevel = Integer.parseInt(enchantKey.split(";")[1].replaceAll(" ", ""));

                        Enchantment enchantment = decodeEnchant(enchantName.toLowerCase());

                        if (enchantment == null) {
                            throw new IllegalArgumentException("Enchantment with name " + enchantName + " not found!");
                        }

                        is.addUnsafeEnchantment(enchantment, enchantLevel);
                    }
                }

                itemsList.add(is);
            }

            Kit kit = new Kit(name, key, permission, kitType, itemsList);
            UniversalKits.getInst().getKits().put(key, kit);
            InventoryUtils.persistKit(kit);

            getLogger().info(StringAPI.asColor("&aSuccessfully loaded &f" + key + " &akit!"));
        }
    }
    public static boolean isCustomEnchant(String in) {
        switch (in) {
            case "бур", "бульдозер", "bur":
            case "магнит", "magnit":
            case "дровосек":
            case "пингер":
            case "автоплавка":
            case "лаваход":
            case "яд":
            case "мегабур", "megabur":
            case "мертвец":
            case "иссушение":
            case "опытный":
                return true;
        }
        return false;
    }
    public static Enchantment decodeEnchant (String in) {
        Enchantment templeOf = Enchantment.getByName(in.toUpperCase());

        if(templeOf != null)
            return templeOf;

        switch(in) {
            case "сила":
                return Enchantment.ARROW_DAMAGE;
            case "горящая стрела":
                return Enchantment.ARROW_FIRE;
            case "бесконечность":
                return Enchantment.ARROW_INFINITE;
            case "откидывание":
                return Enchantment.ARROW_KNOCKBACK;
            case "острота":
                return Enchantment.DAMAGE_ALL;
            case "бич членистоногих":
                return Enchantment.DAMAGE_ARTHROPODS;
            case "небесная кара":
                return Enchantment.DAMAGE_UNDEAD;
            case "разящий клинок":
                return Enchantment.SWEEPING_EDGE;
            case "отдача":
                return Enchantment.KNOCKBACK;
            case "добыча":
                return Enchantment.LOOT_BONUS_MOBS;
            case "удача":
                return Enchantment.LOOT_BONUS_BLOCKS;
            case "эффективность":
                return Enchantment.DIG_SPEED;
            case "прочность":
                return Enchantment.DURABILITY;
            case "защита":
                return Enchantment.PROTECTION_ENVIRONMENTAL;
            case "защита от огня":
                return Enchantment.PROTECTION_FIRE;
            case "защита от взрывов":
                return Enchantment.PROTECTION_EXPLOSIONS;
            case "защита от снарядов":
                return Enchantment.PROTECTION_PROJECTILE;
            case "починка":
                return Enchantment.MENDING;
            case "подводная ходьба":
                return Enchantment.DEPTH_STRIDER;
            case "заговор огня":
                return Enchantment.FIRE_ASPECT;
            case "шипы":
                return Enchantment.THORNS;
            case "невесомость":
                return Enchantment.PROTECTION_FALL;
            case "подводник":
                return Enchantment.WATER_WORKER;
            case "быстрая перезарядка":
                return Enchantment.QUICK_CHARGE;
            case "тройной выстрел":
                return Enchantment.MULTISHOT;
            case "скорость души":
                return Enchantment.SOUL_SPEED;
        }
        return Enchantment.ARROW_DAMAGE;
    }
}
