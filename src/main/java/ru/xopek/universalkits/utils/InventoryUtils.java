package ru.xopek.universalkits.utils;

import ru.xopek.universalkits.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.xopek.universalkits.api.StringAPI;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    private static List<Kit> kits = new ArrayList<>();

    public static void persistKit(Kit kit) {
        kits.add(kit);
    }

    public static NamespacedKey arrowNavKey = NamespacedKey.minecraft("arrownav");
    public static NamespacedKey buyKey = NamespacedKey.minecraft("buybutton");

    public static int getEmptySlotsCount(PlayerInventory inventory) {
        int count = 0;
        for(int i = 0; i < inventory.getSize(); i ++) {
            if((i >= 36 && i <= 40)) continue;
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)
                count++;
        }

        return count;
    }

    public static Inventory openKitMenu(int kitIndex) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Набор: " + StringAPI.asColor(kits.get(kitIndex).getKitName()));

        ItemStack glass = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta itemMeta = glass.getItemMeta();
        itemMeta.setDisplayName("");
        glass.setItemMeta(itemMeta);

        for (int i = 36; i < 45; i++) {
            inventory.setItem(i, glass);
        }

        Kit kit = kits.get(kitIndex);
        List<ItemStack> items = kit.getItemList();
        for (int i = 0; i < items.size() && i < 36; i++) {
            inventory.setItem(i, items.get(i));
        }

        ItemStack previousButton = createNavButton(
                Material.ARROW,
                "&e[«]&6 Назад",
                kitIndex - 1 < 0 ? kits.size() - 1 : kitIndex - 1,
                InventoryUtils.arrowNavKey
        );
        inventory.setItem(45, previousButton);

        ItemStack nextButton = createNavButton(
                Material.ARROW,
                "&e[»]&6 Вперед",
                (kitIndex + 1) % kits.size(),
                InventoryUtils.arrowNavKey
        );
        inventory.setItem(53, nextButton);

        ItemStack buyButton = createBuyButton(
                Material.LIME_DYE,
                "&a[Получить набор]",
                kit.getKitKey(),
                buyKey
        );
        inventory.setItem(49, buyButton);

        return inventory;
    }

    public static ItemStack createNavButton(Material material, String displayName, int index, NamespacedKey key) {
        ItemStack button = new ItemStack(material);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(StringAPI.asColor(displayName));

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.INTEGER, index);

        button.setItemMeta(meta);
        return button;
    }

    public static ItemStack createBuyButton(Material material, String displayName, String index, NamespacedKey key) {
        ItemStack button = new ItemStack(material);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(StringAPI.asColor(displayName));

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.STRING, index);

        button.setItemMeta(meta);
        return button;
    }
}
