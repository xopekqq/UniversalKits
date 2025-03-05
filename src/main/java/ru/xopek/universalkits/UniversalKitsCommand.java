package ru.xopek.universalkits;

import org.hibernate.Session;
import ru.xopek.universalkits.api.EKitsAPI;
import ru.xopek.universalkits.api.StringAPI;
import ru.xopek.universalkits.config.KitsConfig;
import ru.xopek.universalkits.data.AnalyticsData;
import ru.xopek.universalkits.database.DatabaseMethods;
import ru.xopek.universalkits.database.HibernateUtils;
import ru.xopek.universalkits.database.UserEntity;
import ru.xopek.universalkits.kits.Kit;
import ru.xopek.universalkits.kits.KitType;
import ru.xopek.universalkits.utils.InventoryUtils;
import ru.xopek.universalkits.utils.UserUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getScheduler;

public class UniversalKitsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        getScheduler().runTaskAsynchronously(UniversalKits.getInst(), () -> {

            if(args.length < 1) {
                sender.sendMessage(KitsConfig.kitsArgumentsException);
                return;
            }
            if(!(sender instanceof Player)) {
                sender.sendMessage(KitsConfig.kitsActionRequiresPlayer);
                return;
            }
            Player player = (Player) sender;

            switch(args[0].toLowerCase()) {
                case "get" -> {
                    if(args.length < 2) {
                        sender.sendMessage(KitsConfig.kitsArgumentsException);
                        return;
                    }
                    String targetKit = args[1];

                    try (Session session = HibernateUtils.getSession()) {
                        UserEntity user = DatabaseMethods.getUserEntityByName(session, player.getName());

                        if(user == null)
                            user = new UserEntity(player.getName(),player.getAddress().getHostName(), -1, -1,0,0);

                        session.beginTransaction();

                        /**
                         * Cringe Code Below
                         */
                        Kit kit = EKitsAPI.getKitByName(targetKit);

                        if(kit.getKitPermission() != null && !player.isOp() && !player.hasPermission(kit.getKitPermission())) {
                            player.sendMessage(KitsConfig.kitPermission);
                            return;
                        }

                        if(kit == null) {
                            player.sendMessage(KitsConfig.kitNotFound);
                            return;
                        }

                        int emptyItems = InventoryUtils.getEmptySlotsCount(player.getInventory());

                        if(emptyItems < kit.getItemList().size()) {
                            player.sendMessage(KitsConfig.kitsInventorySlotsFull);
                            return;
                        }

                        boolean isDonateKit = kit.getKitType() == KitType.DONATE;

                        if(UserUtils.isTimeouted(user, isDonateKit)) {
                            player.sendMessage(KitsConfig.kitDelayIssue.
                                    replace("{delay}", UserUtils.appendDiff(UserUtils.getExpirtyDifference(user, isDonateKit), UserUtils.getLonpoolDifference(user, isDonateKit)))
                            );
                            player.playSound(player.getLocation(),Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            return;
                        }

                        if(isDonateKit) {
                            user.setKitDonateTs(System.currentTimeMillis());
                            user.setKitDonateCount(user.getKitDonateCount() + 1);
                            AnalyticsData.addTotalPaidKits(1);

                        }else {
                            user.setKitStartTs(System.currentTimeMillis());
                            user.setKitStartCount(user.getKitStartCount() + 1);

                            AnalyticsData.addTotalFreeKits(1);
                        }

                        session.persist(user);

                        player.sendMessage(StringAPI.asColor(KitsConfig.kitTaken.replace("{0}", kit.getKitName())));
                        player.sendTitle(KitsConfig.kitTakenTitle, "", 10,30,7);
                        player.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_YES, 1, 1);


                        for(ItemStack itemStack : kit.getItemList()) {
                            player.getInventory().addItem(itemStack);
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
                        }


                        session.getTransaction().commit();
                    }

                    break;
                }
                case "recover" , "r" -> {
                    if(args.length < 2) {
                        sender.sendMessage(KitsConfig.kitsArgumentsException);
                        return;
                    }
                    if(!player.isOp()) return;

                    String target = args[1];

                    try (Session session = HibernateUtils.getSession()) {
                        UserEntity user = DatabaseMethods.getUserEntityByName(session, target);

                        if(user == null) {
                            sender.sendMessage(KitsConfig.databaseActionUserNotFound);
                            return;
                        }

                        session.beginTransaction();

                        user.setKitStartCount(0);
                        user.setKitDonateTs(-1);
                        user.setKitDonateCount(0);
                        user.setKitStartTs(-1);
                        session.persist(user);

                        session.getTransaction().commit();

                        player.sendMessage(KitsConfig.kitsPrefix + StringAPI.asColor("Очистка &6" + target + "&f Успешна!"));
                    }
                    break;
                }
                case "garbage", "info", "total" -> {

                    player.sendMessage(StringAPI.asColor(KitsConfig.kitsPrefix + StringAPI.asColor("Статистика сервера")));
                    player.sendMessage(StringAPI.asColor("  &6›&f Всего Взято Китов: &6 " + AnalyticsData.getTotalKitsCount()));
                    player.sendMessage(StringAPI.asColor("  &6›&f Взято Бесплатных Китов: &6 " + AnalyticsData.getTotalFreeKits()));
                    player.sendMessage(StringAPI.asColor("  &6›&f Взято Платных Китов: &6 " + AnalyticsData.getTotalPaidKits()));

                    break;
                }
                case "read", "user", "data" -> {
                    if(args.length < 2) {
                        sender.sendMessage(KitsConfig.kitsArgumentsException);
                        return;
                    }
                    if(!player.isOp()) return;
                    String target = args[1];

                    try (Session session = HibernateUtils.getSession()) {
                        String matchFlag = args.length > 2 ? args[2] : null;

                        if(matchFlag == null) {
                            UserEntity user = DatabaseMethods.getUserEntityByName(session, target);

                            if(user == null) {
                                sender.sendMessage(KitsConfig.databaseActionUserNotFound);
                                return;
                            }

                            player.sendMessage(KitsConfig.kitsPrefix + StringAPI.asColor("Информация о игроке &6" + target));
                            player.sendMessage(StringAPI.asColor("  &6› &fБесплатные Киты: &6" + user.getKitStartCount()));
                            player.sendMessage(StringAPI.asColor("  &6› &fПлатные Киты: &6" + user.getKitDonateCount()));
                            player.sendMessage(StringAPI.asColor("  &6› &fОткат Бесплатного Кита: " + (UserUtils.isTimeouted(user, false) ? "&cДа" : "&aНет")));
                            player.sendMessage(StringAPI.asColor("  &6› &fОткат Платного Кита: " + (UserUtils.isTimeouted(user, true)  ? "&cДа" : "&aНет")));
                            player.sendMessage(StringAPI.asColor("  &6› &fАйпи: &6" + user.getUserIp()));
                        }else {
                            switch(matchFlag.replace("--", "").toLowerCase()) {
                                case "ip" -> {

                                    break;
                                }
                            }
                        }


                    }
                    break;
                }
                case "menu" -> {
                    if(args.length < 2) {
                        sender.sendMessage(KitsConfig.kitsArgumentsException);
                        return;
                    }

                    Bukkit.getScheduler().runTask(UniversalKits.getInst(), () -> {
                        Inventory inventory = InventoryUtils.openKitMenu(Integer.parseInt(args[1]));

                        player.openInventory(inventory);
                    });
                }
                default -> {
                    sender.sendMessage(KitsConfig.kitsArgumentsException);
                    return;
                }
            }
        });
        return false;
    }
}
