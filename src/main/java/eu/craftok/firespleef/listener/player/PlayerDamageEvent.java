package eu.craftok.firespleef.listener.player;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.FGame;
import eu.craftok.firespleef.manager.player.FPlayerManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class PlayerDamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }
        if (FMain.getInstance().getGame().getState().equals(FGame.STATE.GAME)){
            event.setDamage(0);
            if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                if (FPlayerManager.getFPlayerByPlayer((Player) event.getEntity()).isPlaying())
                FPlayerManager.getFPlayerByPlayer((Player) event.getEntity()).death();
                else
                event.getEntity().teleport(FMain.getInstance().getGame().getSpawn());
            }
        }else{
            if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                event.getEntity().teleport(FMain.getInstance().getGame().getSpawn());
            }
            event.setCancelled(true);
        }

    }
}
