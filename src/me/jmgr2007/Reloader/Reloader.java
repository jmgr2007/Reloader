package me.jmgr2007.Reloader;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

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
        Logger log = this.getServer().getLogger();
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
        this.getCommand("reloader").setExecutor(CE);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(pl, this);
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
        Graph load = metrics.createGraph("Plugins loaded/unloaded");
        
        load.addPlotter(new Metrics.Plotter("Plugins Loaded") {
            
            @Override
            public int getValue() {
                return Vars.loaded;
            }
            
            @Override
            public void reset() {
            	Vars.loaded = 0;
            }
        });
        
        load.addPlotter(new Metrics.Plotter("Plugins Unloaded") {
            
            @Override
            public int getValue() {
                return Vars.unloaded;
            }
            
            @Override
            public void reset() {
            	Vars.unloaded = 0;
                
            }
        });
        
        Graph enable = metrics.createGraph("Plugins enabled/disabled");
        
        enable.addPlotter(new Metrics.Plotter("Plugins Enabled") {
            
            @Override
            public int getValue() {
                return Vars.enabled;
            }
            
            @Override
            public void reset() {
            	Vars.enabled = 0;
            }
        });
        
        enable.addPlotter(new Metrics.Plotter("Plugins Disabled") {
            
            @Override
            public int getValue() {
                return Vars.disabled;
            }
            
            @Override
            public void reset() {
            	Vars.disabled = 0;
            }
        });
        
        Graph reload = metrics.createGraph("Plugins Reloaded");
        
        reload.addPlotter(new Metrics.Plotter("Plugins Reloaded") {
            
            @Override
            public int getValue() {
                return Vars.reloaded;
            }

            @Override
            public void reset() {
            	Vars.reloaded = 0;
            }
        });
        
        metrics.start();
    }
}
