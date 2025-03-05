package ru.xopek.universalkits;

import ru.xopek.universalkits.api.EKitsAPI;
import ru.xopek.universalkits.kits.Kit;
import ru.xopek.universalkits.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EventProcessor implements Listener {
    @EventHandler
    public void dpsad (PlayerJoinEvent evt) {
        if(!evt.getPlayer().hasPlayedBefore()) {
            Kit kit = EKitsAPI.getKitByName("start");

            for(ItemStack itemStack : kit.getItemList()) {
                evt.getPlayer().getInventory().addItem(itemStack);
                evt.getPlayer().playSound(evt.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
            }

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().startsWith("Набор")) return;
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        PersistentDataContainer data = clickedItem.getItemMeta().getPersistentDataContainer();
        if (data.has(InventoryUtils.arrowNavKey, PersistentDataType.INTEGER)) {
            int newIndex = data.get(InventoryUtils.arrowNavKey, PersistentDataType.INTEGER);
            player.openInventory(InventoryUtils.openKitMenu(newIndex));
        } else if (data.has(InventoryUtils.buyKey, PersistentDataType.STRING)) {
            String buy = data.get(InventoryUtils.buyKey, PersistentDataType.STRING);
            if (buy != null) {
                Bukkit.dispatchCommand(player, "ekits get " + buy);
            }
        }
    }
}
