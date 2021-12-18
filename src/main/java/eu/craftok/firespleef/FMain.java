package eu.craftok.firespleef;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import eu.craftok.core.common.CoreCommon;
import eu.craftok.firespleef.manager.FGame;
import eu.craftok.firespleef.manager.sb.Scoreboard;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class FMain extends JavaPlugin {
    private static FMain instance;
    private static CoreCommon API;
    private FGame game;
    private Scoreboard scoreboard;
    public static CoreCommon getAPI() {
        return API;
    }

    public static FMain getInstance() {
        return instance;
    }

    public FGame getGame() {
        return game;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void onEnable() {
        instance = this;
        API = CoreCommon.getCommon();
        (this.game = new FGame()).setupGame();
        (this.scoreboard = new Scoreboard()).uptadeAllTime();
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Server.WORLD_PARTICLES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Server.WORLD_PARTICLES)
                    return;
                 event.setCancelled(true);
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
