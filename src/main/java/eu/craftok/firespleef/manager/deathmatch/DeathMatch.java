package eu.craftok.firespleef.manager.deathmatch;

import eu.craftok.firespleef.FMain;
import eu.craftok.firespleef.manager.cuboid.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bukkit.Material.AIR;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class DeathMatch {

    private int ytodestroy;
    private int ymin;
    private final Location corner1, corner2;
    private final List<Block> blocks;

    public DeathMatch(Location corner1, Location corner2){
        this.corner1 = corner1;
        this.corner2 = corner2;
        if (corner1.getY() <= corner2.getY()) {
            ytodestroy = corner2.getBlockY();
            ymin = corner1.getBlockY();
        }else {
            ytodestroy = corner1.getBlockY();
            ymin = corner2.getBlockY();
        }
        blocks = new ArrayList<>(new Cuboid(corner1, corner2).getBlocks());
    }

    public void destroyY(){
        if (ymin == ytodestroy){
            return;
        }
        List<Block> cacheblock = new ArrayList<>();
        blocks.forEach(b ->{
            if (b.getY() == ytodestroy && b.getType() != AIR){
                cacheblock.add(b);
            }
        });
        if (cacheblock.isEmpty()){
            ytodestroy--;
            destroyY();
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {

                for (int i = 0; i < 4; i++){
                    if (cacheblock.isEmpty()){
                        cancel();
                        ytodestroy--;
                        destroyY();
                        break;
                    }
                    Block block = cacheblock.remove(new Random().nextInt(cacheblock.size()));
                    if (block.getType() == AIR)
                        continue;
                    block.setType(AIR);
                }

            }
        }.runTaskTimer(FMain.getInstance(), 0, 1);
    }


}
