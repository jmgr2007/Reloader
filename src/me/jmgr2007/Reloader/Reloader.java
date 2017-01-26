package me.jmgr2007.Reloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

import me.jmgr2007.Reloader.Metrics.Plotter;
import me.jmgr2007.Reloader.Metrics.Graph;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Reloader extends JavaPlugin {
    private CommandExecutor CE = new ReloaderListener(this);
    private final Pl pl = new Pl(); 
    
    public void onEnable() {
        initialConfigCheck();
        Logger log = this.getLogger();
        log.info(Utils.string("reloader"));
        this.getCommand("reloader").setExecutor(CE);
        log.info(Utils.string("plugin"));
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(pl, this);
        if(this.getConfig().getBoolean("check"))
        	versionCheck();
        try {
            startMetrics();
        } catch (IOException e1) {
            log.severe(Utils.string("metricsError"));
        }
        if(this.getConfig().getBoolean("timer.autoreload")) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                	Utils.scheduler();
                }
            }, (((this.getConfig().getLong("timer.hours")*60*60) + (this.getConfig().getLong("timer.minutes")*60) + this.getConfig().getLong("timer.seconds")) * 20), (((this.getConfig().getLong("timer.hours")*60*60) + (this.getConfig().getLong("timer.minutes")*60) + this.getConfig().getLong("timer.seconds")) * 20));
        }
    }

    public void onDisable() {

    }
    
    private void versionCheck() {
		int v = Integer.parseInt(this.getDescription().getVersion().replaceAll("[.]", ""));
		URL url = null;
		try {
			url = new URL("https://api.curseforge.com/servermods/files?projectIds=40397");
		} catch (MalformedURLException e) {
			return;
		}
		Scanner s = null;
		try {
			s = new Scanner(url.openStream());
		} catch (IOException e) {
			return;
		}
		if (url != null && s != null) {
			String j = ""; 
			if (s.hasNextLine()) {
				j = s.nextLine();
			}
			s.close();
			if (!j.equals("")) {
				String[] t = j.replace("[", "").replace("]", "").replaceAll("\\}\\,\\{", "};{").replaceAll("\\{","").replaceAll("\\}", "").split(";");
				t = t[t.length-1].split(",");
				j = t[t.length-3].substring(7).replaceAll("\"", "").replaceAll("[A-Za-z. ]", "");
				if (!j.equals("")) {
					int c = Integer.parseInt(j);
					if (v < c) {
						this.getLogger().info(Utils.string("update").replaceAll("%URL%", t[t.length-8].substring(14).replaceAll("\"", "").replaceAll("\\\\", "")));
					}
				}
			}
		}
		return;
    }
    
    private void initialConfigCheck(){
        if(Utils.localeFile == null)
        	Utils.localeFile = new File (getDataFolder(),"locale.yml");
        if(!(Utils.localeFile.exists())){
        	Utils.localize();
        	this.getLogger().info(Utils.string("defaultLocale"));
        } else
        	Utils.locale = YamlConfiguration.loadConfiguration(Utils.localeFile);
        getConfig().options().copyDefaults(true);
        if(!(new File(this.getDataFolder(),"config.yml").exists())){
            this.getLogger().info(Utils.string("defaultConfig"));
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
