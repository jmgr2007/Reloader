package me.jmgr2007.Reloader;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReloaderListener implements CommandExecutor {
	public static Reloader plugin;
	private Plugin [] plugins = Bukkit.getServer().getPluginManager().getPlugins();
	public ReloaderListener(Reloader instance) {
		ReloaderListener.plugin = instance;
	}
    private static void utilz(String name) { new Utils(name);}
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	plugins = Bukkit.getServer().getPluginManager().getPlugins();
            if (cmd.getName().equalsIgnoreCase("reloader")) {
                if (args.length == 0) {
                	return Utils.help(sender);
                } else if(args[0].equalsIgnoreCase("config") && Utils.permCheck(sender, "reloader.config")) {
                	if(args.length < 2 || plugin.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                		plugin.reloadConfig();
                		plugin.getServer().getScheduler().cancelTasks(plugin);
                		if(plugin.getConfig().getBoolean("timer.autoreload")) {
                            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                	Utils.scheduler();
                                }
                            }, (((plugin.getConfig().getLong("timer.hours")*60*60) + (plugin.getConfig().getLong("timer.minutes")*60) + plugin.getConfig().getLong("timer.seconds")) * 20), (((plugin.getConfig().getLong("timer.hours")*60*60) + (plugin.getConfig().getLong("timer.minutes")*60) + plugin.getConfig().getLong("timer.seconds")) * 20));
                        }
                    	Utils.msg(sender, Utils.string("config", plugin.getName()));
                	} else {
                        for(Plugin pl : plugins) {
                        	if(pl.getName().toLowerCase().startsWith(Utils.join(args).toLowerCase())) {
                        		pl.reloadConfig();
                        		Utils.msg(sender, Utils.string("config", pl.getName()));
                        	}
                        }
                   	}
                } else if (args[0].equalsIgnoreCase("reload") && Utils.permCheck(sender, "reloader.reload")) {
                	if(args.length > 1) { 
	                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
	                    	if(!plugin.getConfig().getString("reload").trim().isEmpty())
	                        	plugin.getServer().broadcastMessage(plugin.getConfig().getString("reload").replaceAll("&(?=[0-9a-fA-FkKmMoOlLnNrR])", "\u00a7"));
	                        for(Plugin pl : plugins) {
	                            if(!pl.getName().toLowerCase().startsWith(plugin.getName().toLowerCase()) && !(Utils.exempt(pl.getName()))) {
	                            	utilz(pl.getName());
	                                Utils.unload(pl.getName());
	                                Utils.load(pl.getName());
	                                Vars.reloaded.increment();
	                            }
	                        }
	                    } else if(args[1].equalsIgnoreCase("harsh")) {
	                    	Utils.hReload();
	                    } else {
	                    	if (!Utils.exempt(Utils.join(args))) {
		                        Plugin[] plugins = this.plugins;
		                        for(int i = 0; i < plugins.length; i++) {
		                            if(plugins[i].getName().equalsIgnoreCase(Utils.join(args))) {
										Utils.unload(plugins[i].getName());
		                                Utils.load(plugins[i].getName());
		                                Utils.msg(sender, Utils.string("reloaded", plugins[i].getName()));
		                                Vars.reloaded.increment();
		                            }
		                        }
	                    	} else {
	                    		Utils.msg(sender, Utils.string("exempt", Utils.canceledPl));
	                    	}
	                    }
                	} else {
                		Utils.msg(sender, Utils.string("args"));
                	}
                } else if (args[0].equalsIgnoreCase("disable") && Utils.permCheck(sender, "reloader.disable")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                        	Utils.msg(sender, Utils.string("disableAll"));
                            for(Plugin pl : plugins) {
                            	if(!Utils.exempt(pl.getName()) && !pl.getName().toLowerCase().startsWith(plugin.getName().toLowerCase())) {
                            		utilz(pl.getName());
                            		Utils.disable(pl.getName(), sender);
                            		Vars.disabled.increment();
                            	}
                            }
                        } else {
                       		if(!Utils.exempt(Utils.join(args))) {
	                        	utilz(Utils.join(args));
	                        	Utils.disable(Utils.join(args), sender);
	                    		Vars.disabled.increment();
                       		}
                        }
                    } else {
                    	Utils.msg(sender, Utils.string("args"));
                    }
                } else if (args[0].equalsIgnoreCase("enable") && Utils.permCheck(sender, "reloader.enable")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                            for(Plugin pl : plugins) {
                                Utils.enable(pl.getName());
                            }
                            Utils.msg(sender, Utils.string("enableAll"));
                        } else {
                        	Utils.enable(Utils.join(args), sender);
                        	Vars.enabled.increment();
                        }
                    } else {
                    	Utils.msg(sender, Utils.string("args"));
                    }
                } else if (args[0].equalsIgnoreCase("load") && Utils.permCheck(sender, "reloader.load")) {
                	if (args.length > 1) {
                        Utils.load(Utils.join(args), sender);
                        Vars.loaded.increment();
                	} else 
                		Utils.msg(sender, Utils.string("args"));
                } else if (args[0].equalsIgnoreCase("unload") && Utils.permCheck(sender, "reloader.unload")) {
                	if (args.length > 1 && !Utils.exempt(Utils.join(args))) {
	                    utilz(Utils.join(args));
	                	Utils.unload(Utils.join(args), sender);
	                	Vars.unloaded.increment();
                	} else 
                		Utils.msg(sender, Utils.string("args"));
                } else if (args[0].equalsIgnoreCase("check") && Utils.permCheck(sender, "reloader.check")) {
                	if (args.length > 1) {
                		Utils.check(Utils.join(args), sender);
                	} else 
                		Utils.msg(sender, Utils.string("args"));
                } else if (args[0].equalsIgnoreCase("info") && Utils.permCheck(sender, "reloader.info")) {
                	Utils.info(Utils.join(args), sender);
                } else if (args[0].equalsIgnoreCase("use") && Utils.permCheck(sender, "reloader.use")) {
	            	Utils.use(Utils.join(args), sender);
	            } else if (args[0].equalsIgnoreCase("perm") && Utils.permCheck(sender, "reloader.perm")) {
	                if(args.length == 3) {
	                	return Utils.perm(args[1], sender, args[2]);
	                } else if(args.length == 2) {
	                	return Utils.perm(sender, args[1]);
	                } else
	                	Utils.msg(sender, Utils.string("args"));
	            } else if (args[0].equalsIgnoreCase("list") && Utils.permCheck(sender, "reloader.list")) {
	            	if (args.length == 2) {
	            	    if (args[1].equalsIgnoreCase("-v")) {
	            		    return Utils.vList(sender);
	            	    }
	        		}
	            	return Utils.list(sender);
	            } else {
	            	if(!args[0].toLowerCase().equalsIgnoreCase("reload") && !args[0].toLowerCase().equalsIgnoreCase("disable") && !args[0].toLowerCase().equalsIgnoreCase("enable") && !args[0].toLowerCase().equalsIgnoreCase("load") && !args[0].toLowerCase().equalsIgnoreCase("unload") && !args[0].toLowerCase().equalsIgnoreCase("check") && !args[0].toLowerCase().equalsIgnoreCase("info") && !args[0].toLowerCase().equalsIgnoreCase("perm") && !args[0].toLowerCase().equalsIgnoreCase("list") && !args[0].toLowerCase().equalsIgnoreCase("config")) {
	            		Utils.msg(sender, Utils.string("args"));
	            		return true;
	            	} else {
	            		Utils.msg(sender, Utils.string("perms"));
	            		return true;
	            	}
	            }
            }
        return true;
    }
}
