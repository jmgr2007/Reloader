package me.jmgr2007.Reloader;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import me.jmgr2007.Reloader.Metrics.Plotter;
import me.jmgr2007.Reloader.Metrics.Graph;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Reloader extends JavaPlugin {
    private CommandExecutor CE = new ReloaderListener(this);
    private final Pl pl = new Pl(); 
    
    public void onEnable() {
    	Vars.disabled = new Val();
    	Vars.enabled = new Val();
    	Vars.loaded = new Val();
    	Vars.reloaded = new Val();
    	Vars.unloaded = new Val();
        Logger log = this.getServer().getLogger();
        log.info("[Reloader] Passing Reloader command to command handler");
        this.getCommand("reloader").setExecutor(CE);
        log.info("[Reloader] Passing Plugin command to command listener");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(pl, this);
        initialConfigCheck();
        try {
            startMetrics();
        } catch (IOException e1) {
            log.severe("[Reloader] Metrics was unable to start");
        }
        if(this.getConfig().getBoolean("timer.autoreload")) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getServer().broadcastMessage("§6[§2Reloader§6] §aRunning automatic reload, Please wait for lag to end.");
                    PluginManager pm = Bukkit.getServer().getPluginManager();
                    Plugin[] plugins = pm.getPlugins();
                    for(int i = 0; i < plugins.length; i++) {
                        if(plugins[i].getName() != "Reloader") {
                            Utils.unload(plugins[i].getName());
                            Utils.load(plugins[i].getName());
                        }
                    }
                }
            }, (((this.getConfig().getLong("timer.hours")*60*60) + (this.getConfig().getLong("timer.minutes")*60) + this.getConfig().getLong("timer.seconds")) * 20), (((this.getConfig().getLong("timer.hours")*60*60) + (this.getConfig().getLong("timer.minutes")*60) + this.getConfig().getLong("timer.seconds")) * 20));
        }
    }

    public void onDisable() {

    }
    
    private void initialConfigCheck(){
        getConfig().options().copyDefaults(true);
        if(!(new File(this.getDataFolder(),"config.yml").exists())){
            this.getLogger().info("Saving default configuration file.");
            this.saveDefaultConfig();
        }
    }

    private void startMetrics() throws IOException {
        Metrics metrics;
        metrics = new Metrics(this);
        Graph load = metrics.createGraph("loaded");
        
        load.addPlotter(new Plotter("Plugins Loaded") {
            
            @Override
            public int getValue() {
                return Vars.loaded.getValue();
            }
            
            @Override
            public void reset() {
            	Vars.loaded.reset();
            }
        });
        
        load.addPlotter(new Metrics.Plotter("Plugins Unloaded") {
            
            @Override
            public int getValue() {
                return Vars.unloaded.getValue();
            }
            
            @Override
            public void reset() {
            	Vars.unloaded.reset();
                
            }
        });
        
        Graph enable = metrics.createGraph("enabled");
        
        enable.addPlotter(new Metrics.Plotter("Plugins Enabled") {
            
            @Override
            public int getValue() {
                return Vars.enabled.getValue();
            }
            
            @Override
            public void reset() {
            	Vars.enabled.reset();
            }
        });
        
        enable.addPlotter(new Metrics.Plotter("Plugins Disabled") {
            
            @Override
            public int getValue() {
                return Vars.disabled.getValue();
            }
            
            @Override
            public void reset() {
            	Vars.disabled.reset();
            }
        });
        
        Graph reload = metrics.createGraph("Reloaded");
        
        reload.addPlotter(new Metrics.Plotter("Plugins Reloaded") {
            
            @Override
            public int getValue() {
                return Vars.reloaded.getValue();
            }

            @Override
            public void reset() {
            	Vars.reloaded.reset();
            }
        });
        
        metrics.start();
    }
}
