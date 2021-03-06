package com.division.battlegrounds.storage;

import com.division.battlegrounds.core.BattlegroundCore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author Evan
 */
public class StorageManager {

    /**
     * Saves the player inventory as an object file.
     *
     * @param inventory Player's inventory
     * @param playerName Player's name
     */
    public static void saveStorageCrate(PlayerInventory inventory, String playerName) {
        FileOutputStream fos = null;
        try {
            File invLoc = new File(BattlegroundCore.getInstance().getDataFolder(), "storage");
            System.out.println(invLoc.getAbsolutePath());
            if (!invLoc.exists()) {
                invLoc.mkdir();
            }
            File crateLoc = new File(invLoc, playerName + ".bgs");
            SupplyCrate sCrate = new SupplyCrate(inventory);
            fos = new FileOutputStream(crateLoc);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(sCrate);
            oos.flush();
        } catch (Exception ex) {
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Returns the players storage crate from the Battlegrounds holding center.
     *
     * @param playerName Player's name
     * @return SupplyCrate
     */
    public static SupplyCrate loadStorageCrate(String playerName) {
        FileInputStream fis = null;
        try {
            File invLoc = new File(BattlegroundCore.getInstance().getDataFolder(), "storage/" + playerName + ".bgs");
            fis = new FileInputStream(invLoc);
            ObjectInputStream ois = new ObjectInputStream(fis);
            SupplyCrate sCrate = (SupplyCrate) ois.readObject();
            ois.close();
            fis.close();
            invLoc.delete();
            return sCrate;
        } catch (Exception ex) {
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static void loadOfflineStorage(Player player) {
        if (player != null && !player.isDead()) {
            SupplyCrate crate = loadStorageCrate(player.getName());
            if (crate != null) {
                player.getInventory().clear();
                player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
                player.getInventory().setArmorContents(crate.uncrateArmor());
                player.getInventory().setContents(crate.uncrateContents());
                BattlegroundCore.sendMessage(player, "Your inventory has been restored.");
            }
        }
    }

    public static boolean hasStorageCrate(String playerName) {
        return new File(BattlegroundCore.getInstance().getDataFolder(), "storage/" + playerName + ".bgs").exists();
    }
}
