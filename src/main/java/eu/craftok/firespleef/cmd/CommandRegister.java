package eu.craftok.firespleef.cmd;


import eu.craftok.firespleef.FMain;

/**
 * Project Craftok-FireSpleef Created by Sithey
 */

public class CommandRegister {
    public static void registerCommand() {

        FMain main = FMain.getInstance();
        main.getCommand("start").setExecutor(new StartCommand());
        main.getCommand("stats").setExecutor(new StatsCommand());
    }

}
