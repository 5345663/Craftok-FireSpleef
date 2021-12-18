package eu.craftok.firespleef.listener.player;

import eu.craftok.core.common.user.User;
import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.FGame;
import eu.craftok.firespleef.manager.player.FPlayer;
import eu.craftok.firespleef.manager.player.FPlayerManager;
import eu.craftok.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class PlayerInventoryEvent implements Listener {



    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        User user = FMain.getAPI().getUserManager().getUserByUniqueId(event.getPlayer().getUniqueId());
        if (FMain.getInstance().getGame().getState() == FGame.STATE.GAME) {
            if (!FPlayerManager.getFPlayerByPlayer(event.getPlayer()).isPlaying()) {
                event.setCancelled(true);
                FPlayerManager.getSpectatingFPlayers().forEach(s -> s.getPlayer().sendMessage(user.getDisplayName() + "§f: " + event.getMessage()));
                return;
            }
        }

        event.setFormat(user.getDisplayName() + "§f: %2$s");
    }

    @EventHandler
    public void onMotd(ServerListPingEvent event) {
        if (FMain.getInstance().getGame().getState() != FGame.STATE.LOBBY || FPlayerManager.getPlayingFPlayers().size() == FMain.getInstance().getGame().getMaxplayers()) {
            event.setMotd("INGAME");
        }
    }

    private ArrayList<Player> cooldown = new ArrayList<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (FPlayerManager.getFPlayerByPlayer(player).isPlaying()) {
            Action action = event.getAction();
            if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)
                return;
            if (cooldown.contains(player))
                return;
            ItemStack item = event.getItem();
            if (item != null && item.getType().equals(Material.FIREBALL)) {
                event.setCancelled(true);
                shoot(player);
            }
        }
    }

    private List<Entity> cachemegaexplosion = new ArrayList<>();

    @EventHandler
    public void onBlockDamageEvent(ExplosionPrimeEvent event){
        if (cachemegaexplosion.contains(event.getEntity())){
            event.setRadius(5);
            cachemegaexplosion.remove(event.getEntity());
            return;
        }
        event.setRadius(3);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }
    private Map<Entity, Player> cachetnt = new HashMap<>();
    @EventHandler
    public void onFireBallExplode(EntityExplodeEvent event){
        if (event.getEntity().getType().equals(EntityType.FIREBALL)) {
            event.setCancelled(true);
            event.blockList().clear();
            TNTPrimed tnt = event.getLocation().getWorld().spawn(event.getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(0);
            tnt.setIsIncendiary(false);
            Player shooter = (Player) ((Fireball) event.getEntity()).getShooter();
            cachetnt.put(tnt, shooter);
            if (cacheexplosion.containsKey(shooter)) {
                cachemegaexplosion.add(tnt);
                int ex = cacheexplosion.get(shooter);
                if (ex > 1){
                    cacheexplosion.put(shooter, ex - 1);
                }else {
                    cacheexplosion.remove(shooter);
                }

            }

        }
        if (event.getEntity().getType().equals(EntityType.PRIMED_TNT)){
            event.blockList().forEach(b -> {
                b.setType(Material.AIR);
                if (FMain.getInstance().getGame().getBonuscache().contains(b)){
                    FMain.getInstance().getGame().getBonuscache().remove(b);
                    giveBonus(cachetnt.remove(event.getEntity()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            FMain.getInstance().getGame().spawnBonus();
                        }
                    }.runTaskLater(FMain.getInstance(), 30 * 20);
                }
            });
        }
    }

    private void shoot(Player player) {
        Location eye = player.getEyeLocation();
        Location loc = eye.add(eye.getDirection().multiply(1.2));
        Fireball fireball = (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);
        fireball.setVelocity(loc.getDirection().normalize().multiply(2));
        fireball.setShooter(player);
        fireball.setIsIncendiary(false);
        cooldown.add(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldown.remove(player);
            }
        }.runTaskLater(FMain.getInstance(), 20);
    }
    private Map<Player, Integer> cacheexplosion = new HashMap<>();
    private void giveBonus(Player player){
        int random = new Random(System.currentTimeMillis()).nextInt(4);
        PlayerUtils util = new PlayerUtils(player);
        switch (random){
            case 0:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15 * 20, 0, false, false));
                util.sendSound(Sound.BLAZE_DEATH, 2f);
                util.sendMessage("§c§lCRAFTOK §8§l» §7Vous venez de récupérer le bonus : §aVitesse");
                Bukkit.getServer().broadcastMessage("§c§lCRAFTOK §8§l» §a" + player.getName() + " §7vient de récupérer le bonus");
                break;
            case 1:
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 15 * 20, 1, false, false));
                util.sendSound(Sound.BLAZE_DEATH, 2f);
                util.sendMessage("§c§lCRAFTOK §8§l» §7Vous venez de récupérer le bonus : §aSaut amélioré");
                Bukkit.getServer().broadcastMessage("§c§lCRAFTOK §8§l» §a" + player.getName() + " §7vient de récupérer le bonus");
                break;
            case 2:
                cacheexplosion.put(player, 5);
                util.sendSound(Sound.BLAZE_DEATH, 2f);
                util.sendMessage("§c§lCRAFTOK §8§l» §7Vous venez de récupérer le bonus : §aExplosion x2");
                Bukkit.getServer().broadcastMessage("§c§lCRAFTOK §8§l» §a" + player.getName() + " §7vient de récupérer le bonus");
                break;
            case 3:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 0, false, false));
                util.sendSound(Sound.BLAZE_DEATH, 2f);
                util.sendMessage("§c§lCRAFTOK §8§l» §7Vous venez de récupérer le bonus : §cLenteur");
                Bukkit.getServer().broadcastMessage("§c§lCRAFTOK §8§l» §a" + player.getName() + " §7vient de récupérer le bonus");
                break;
            case 4:
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 0, false, false));
                util.sendSound(Sound.BLAZE_DEATH, 2f);
                util.sendMessage("§c§lCRAFTOK §8§l» §7Vous venez de récupérer le bonus : §cAveuglement");
                Bukkit.getServer().broadcastMessage("§c§lCRAFTOK §8§l» §a" + player.getName() + " §7vient de récupérer le bonus");
                break;
            case 5:
                FPlayer fp = FPlayerManager.getFPlayerByPlayer(player);
                if (fp.getDoublejump() != 5){
                    fp.addDoubleJump();
                    util.sendSound(Sound.BLAZE_DEATH, 2f);
                    util.sendMessage("§c§lCRAFTOK §8§l» §7Vous venez de récupérer le bonus : §a+1 Double Saut");
                }else{
                    giveBonus(player);
                }
                break;
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (FMain.getInstance().getGame().getState().equals(FGame.STATE.GAME)) {
            FPlayer fp = FPlayerManager.getFPlayerByPlayer(player);
            if (fp.isPlaying() && fp.getDoublejump() > 0) {
                if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
                    player.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        FPlayer fp = FPlayerManager.getFPlayerByPlayer(player);
        if (fp.isPlaying() && fp.getPlayer().getGameMode() == GameMode.ADVENTURE){
            fp.removeDoubleJump();
            event.setCancelled(true);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setVelocity(player.getLocation().getDirection().multiply(2.0D).setY(1.00));
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1,1);
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.CLOUD, true, (float)player.getLocation().getX(), (float)(player.getLocation().getY() - 2.0D), (float)player.getLocation().getZ(), 0.1F, 0.1F, 0.1F, 0.1F, 100, new int[] { 3, 10 });
            for (Player ps : Bukkit.getOnlinePlayers())
                (((CraftPlayer)ps).getHandle()).playerConnection.sendPacket(packet);
        }
    }
}
