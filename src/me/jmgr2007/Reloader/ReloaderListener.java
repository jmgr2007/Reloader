package me.jmgr2007.Reloader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

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
                	PluginManager pm = Bukkit.getServer().getPluginManager();
                	if(args.length > 1) {
                        for(Plugin pl : plugins) {
                        	if(pl.getName().toLowerCase().startsWith(Utils.join(args)))
                        		pl.reloadConfig();
                        }
                	} else {
                		pm.getPlugin("Reloader").reloadConfig();
                	}
                	sender.sendMessage("§aConfig reloaded");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                        plugin.getServer().broadcastMessage("§2[Reloader] §4Reloading ALL plugins");
                        Vars.reloaded.add(plugins.length);
                        for(Plugin pl : plugins) {
                            boolean allow = true;
                            for(String ex : plugin.getConfig().getStringList("exempt")) {
                                String p = ex;
                                if(pl.getName() == p) {
                                    allow = false;
                                }
                            }
                            if(!pl.getName().toLowerCase().startsWith(plugin.getName().toLowerCase()) && allow) {
                                utilz(pl.getName());
                                Utils.unload(pl.getName());
                                Utils.load(pl.getName());
                            }
                        }
                    } else if(args[1].equalsIgnoreCase("harsh")) {
                    	Utils.hReload();
                    } else {
                        Vars.reloaded.increment();
                        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
                        for(int i = 0; i < plugins.length; i++) {
                            if(plugins[i].getName().equalsIgnoreCase(Utils.join(args))) {
                                utilz(plugins[i].getName());
								Utils.unload(plugins[i].getName());
                                Utils.load(plugins[i].getName());
                                Utils.msg(sender, ChatColor.GREEN + "Plugin reloaded");
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("disable")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("*")) {
                        	Utils.msg(sender, ChatColor.RED + "Disabling all non-exempt plugins");
                            for(Plugin pl : plugins) {
                            	utilz(pl.getName());
                            	if(!pl.getName().toLowerCase().startsWith("reloader".toLowerCase()))
                            		Utils.disable(pl.getName(), sender);
                            }
                        } else {
                        	util(Utils.join(args), sender);
                        	Utils.disable(Utils.join(args), sender);
                        }
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
                        }
                    }
                } else if (args[0].equalsIgnoreCase("load")) {
                        Utils.load(Utils.join(args), sender);
                } else if (args[0].equalsIgnoreCase("unload")) {
                    utilz(Utils.join(args));
                	Utils.unload(Utils.join(args), sender);
                } else if (args[0].equalsIgnoreCase("check")) {
                	Utils.check(Utils.join(args), sender);
                } else if (args[0].equalsIgnoreCase("info")) {
                	Utils.info(Utils.join(args), sender);
	            } else if (args[0].equalsIgnoreCase("use")) {
	            	Utils.use(Utils.join(args), sender);
	            } else if (args[0].equalsIgnoreCase("perm")) {
	                if(args.length == 3) {
	                	return Utils.perm(args[1], sender, args[2]);
	                } else if(args.length == 2) {
	                	return Utils.perm(sender, args[1]);
	                }
	            } else if (args[0].equalsIgnoreCase("list")) {
	            	if (args.length == 2) {
	            	    if (args[1].equalsIgnoreCase("-v")) {
	            		    return Utils.vList(sender);
	            	    }
	        		}
	            	return Utils.list(sender);
	            } else {
	            		Utils.msg(sender, ChatColor.RED + "Invalid Args");
	            		Utils.msg(sender, "Disabled: " + Vars.disabled.getValue() + " Enabled: " + Vars.enabled.getValue() + " Loaded: " + Vars.loaded.getValue() + " UnLoaded: " + Vars.unloaded.getValue() + " ReLoaded: " + Vars.reloaded.getValue());
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
