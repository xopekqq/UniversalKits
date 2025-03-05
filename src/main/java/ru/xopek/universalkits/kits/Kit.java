package ru.xopek.universalkits.kits;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Kit {
    @Getter @Setter
    private String kitName;
    @Getter @Setter
    private String kitKey;
    @Getter @Setter
    private String kitPermission;
    @Getter
    private ArrayList<ItemStack> itemList;
    @Getter
    private KitType kitType;
    public Kit (@NotNull String kitName ,@NotNull String key,@Nullable String permission, @NotNull KitType kitType, @Nullable ArrayList<ItemStack> items) {
        this.kitName = kitName;
        this.kitType = kitType;
        this.kitPermission = permission;
        this.kitKey = key;

        this.itemList = items == null ? new ArrayList<>() : items;
    }
    public void addItem (ItemStack item) {
        this.itemList.add(item);
    }
    public void addItems (List<ItemStack> items) {
        this.itemList.addAll(items);
    }
    public void removeItem (ItemStack item) {
        this.itemList.remove(item);
    }
    public void removeItem (int[] itemIndexes) {
        for(int i = 0; i < itemIndexes.length; i++)
            this.itemList.remove(itemIndexes[i]);
    }
    public void removeItems (List<ItemStack> items) {
        this.itemList.removeAll(items);
    }
    public void flushItems () {
        this.itemList.clear();
    }
}
