package me.jmgr2007.Reloader;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private static void util(String name, CommandSender sender) { new Utils(name, sender);}
    private static void utilz(String name) { new Utils(name);}
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	plugins = Bukkit.getServer().getPluginManager().getPlugins();
        if (sender.hasPermission("reloader.reload")) {
            if (cmd.getName().equalsIgnoreCase("reloader")) {
                if (args.length == 0) {
                	return Utils.help(sender);
                } else if(args[0].equalsIgnoreCase("config")) {
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
                	} else {
                		args[1] = plugin.getName();
                        for(Plugin pl : plugins) {
                        	if(pl.getName().toLowerCase().startsWith(Utils.join(args).toLowerCase()))
                        		pl.reloadConfig();
                        }
                   	}
                	Utils.msg(sender, ChatColor.GREEN +"Config reloaded");
                } else if (args[0].equalsIgnoreCase("reload")) {
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
		                        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
		                        for(int i = 0; i < plugins.length; i++) {
		                            if(plugins[i].getName().equalsIgnoreCase(Utils.join(args))) {
										Utils.unload(plugins[i].getName());
		                                Utils.load(plugins[i].getName());
		                                Utils.msg(sender, ChatColor.GREEN + "Plugin reloaded");
		                                Vars.reloaded.increment();
		                            }
		                        }
	                    	} else {
	                    		Utils.msg(sender, ChatColor.RED + "This plugin is exempt");
	                    	}
	                    }
                	} else {
                		Utils.msg(sender, ChatColor.RED + "You must state what you wish to reload");
                	}
                } else if (args[0].equalsIgnoreCase("disable")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                        	Utils.msg(sender, ChatColor.RED + "Disabling all non-exempt plugins");
                            for(Plugin pl : plugins) {
                            	if(!Utils.exempt(pl.getName()) && !pl.getName().toLowerCase().startsWith(plugin.getName().toLowerCase())) {
                            		utilz(pl.getName());
                            		Utils.disable(pl.getName(), sender);
                            		Vars.disabled.increment();
                            	}
                            }
                        } else {
                        	util(Utils.join(args), sender);
                        	Utils.disable(Utils.join(args), sender);
                    		Vars.disabled.increment();
                        }
                    } else {
                    	Utils.msg(sender, ChatColor.RED + "You must state what you wish to disable");
                    }
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                            for(Plugin pl : plugins) {
                                Utils.enable(pl.getName());
                            }
                            Utils.msg(sender, ChatColor.GREEN + "Enabling all plugins");
                        } else {
                        	Utils.enable(Utils.join(args), sender);
                        	Vars.enabled.increment();
                        }
                    } else {
                    	Utils.msg(sender, ChatColor.RED + "You must state what you with to enable");
                    }
                } else if (args[0].equalsIgnoreCase("load")) {
                	if (args.length > 1) {
                        Utils.load(Utils.join(args), sender);
                        Vars.loaded.increment();
                	} else 
                		Utils.msg(sender, ChatColor.RED + "You must state what you would like to load");
                } else if (args[0].equalsIgnoreCase("unload")) {
                	if (args.length > 1) {
	                    utilz(Utils.join(args));
	                	Utils.unload(Utils.join(args), sender);
	                	Vars.unloaded.increment();
                	} else 
                		Utils.msg(sender, ChatColor.RED + "You must state what you would like to unload");
                } else if (args[0].equalsIgnoreCase("check")) {
                	if (args.length > 1) {
                		Utils.check(Utils.join(args), sender);
                	} else 
                		Utils.msg(sender, ChatColor.RED + "You must state what plugin you would like to check");
                } else if (args[0].equalsIgnoreCase("info")) {
                	Utils.info(Utils.join(args), sender);
                } else if (args[0].equalsIgnoreCase("use")) {
	            	Utils.use(Utils.join(args), sender);
	            } else if (args[0].equalsIgnoreCase("perm")) {
	                if(args.length == 3) {
	                	return Utils.perm(args[1], sender, args[2]);
	                } else if(args.length == 2) {
	                	return Utils.perm(sender, args[1]);
	                } else
	                	Utils.msg(sender, ChatColor.RED + "You must state what permission you would like to check");
	            } else if (args[0].equalsIgnoreCase("list")) {
	            	if (args.length == 2) {
	            	    if (args[1].equalsIgnoreCase("-v")) {
	            		    return Utils.vList(sender);
	            	    }
	        		}
	            	return Utils.list(sender);
	            } else {
	            		Utils.msg(sender, ChatColor.RED + "Invalid Args");
	                    return true;
	            }
            }
        } else if (sender.hasPermission("reloader.list")) {
        	if (args[0].equalsIgnoreCase("help")) {
        		return Utils.aHelp(sender);
        	} else if (args[0].equalsIgnoreCase("list")) {
        		if (args.length == 2) {
            	    if (args[1].equalsIgnoreCase("-v")) {
            		    return Utils.vList(sender);
            	    }
        		}
            	return Utils.list(sender);
        	} else {
        		Utils.msg(sender, ChatColor.RED + "Invalid Args");
                return true;
        	}
        } else {
        	Utils.msg(sender, ChatColor.RED + "Invalid permissions");
            return true;
        }
        return true;
    }
}
