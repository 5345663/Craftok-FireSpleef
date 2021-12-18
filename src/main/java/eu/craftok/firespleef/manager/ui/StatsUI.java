package eu.craftok.firespleef.manager.ui;

import eu.craftok.firespleef.manager.player.stats.FStats;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.inventory.CustomInventory;
import eu.craftok.utils.inventory.item.StaticItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class StatsUI extends CustomInventory {
    private FStats stats;
    public StatsUI(Player p, FStats target) {
        super(p, "§7Statistiques de » §e" + target.getPlayer().getOfflinePlayer().getName(), 1, 1);
        stats = target;
    }

    @Override
    public void setupMenu() {
        addItem(new StaticItem(0, new ItemCreator(Material.DIAMOND_SWORD).setName(" §fPartie jouées §3» §b" + stats.getPlaying()).getItemstack()));
        addItem(new StaticItem(1, new ItemCreator(Material.GOLD_LEGGINGS).setName(" §fMorts §3» §b" + stats.getDeath()).getItemstack()));
        addItem(new StaticItem(3, new ItemCreator(Material.DIAMOND_BOOTS).setName(" §fVictoire §3» §b" + stats.getWins()).getItemstack()));
        addItem(new StaticItem(8, new ItemCreator(Material.SKULL_ITEM).addLore("").addLore(" §fTemps de jeu §3» §b" + stats.getPlayingtime()).setDurability((short) 3).setOwner(stats.getPlayer().getOfflinePlayer().getName()).setName(" §D┃ §7Statistiques de » §e" + stats.getPlayer().getOfflinePlayer().getName()).getItemstack()));
    }
}
