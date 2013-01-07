package me.jmgr2007.Reloader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReloaderListener implements CommandExecutor {
	public Plugin plugin;
	private Plugin [] plugins = Bukkit.getServer().getPluginManager().getPlugins();
	public ReloaderListener(Plugin plugin) {
		this.plugin = plugin;
	}
    private static void util(String name, CommandSender sender) { new Utils(name, sender);}
    private static void utilz(String name) { new Utils(name);}
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("reloader.reload")) {
            if (cmd.getName().equalsIgnoreCase("reloader")) {
                if (args.length == 0) {
                	return Utils.help(sender);
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (args[1].equalsIgnoreCase("all")
                            || args[1].equalsIgnoreCase("*")) {
                        plugin.getServer().broadcastMessage("§2[Reloader] §4Reloading ALL plugins");
                        Vars.addStats("reload", plugins.length);
                        for(int i = 0; i < plugins.length; i++) {
                            boolean allow = true;
                            for(String ex : plugin.getConfig().getStringList("exempt")) {
                                String p = ex;
                                if(plugins[i].getName() == p) {
                                    allow = false;
                                }
                            }
                            if(!plugins[i].getName().toLowerCase().startsWith("Reloader") && allow) {
                                utilz(plugins[i].getName());
                                Utils.unload(plugins[i].getName());
                                return Utils.load(plugins[i].getName());
                            }
                        }
                    } else {
                        Vars.addStats("reload", 1);
                        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
                        for(int i = 0; i < plugins.length; i++) {
                            if(plugins[i].getName().equalsIgnoreCase(args[1])) {
                                utilz(plugins[i].getName());
								Utils.unload(plugins[i].getName());
                                Utils.load(plugins[i].getName(), plugin.getServer().getPlayer(sender.getName()));
                                sender.sendMessage(ChatColor.GREEN + "Plugin reloaded");
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("disable")) {
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("all")
                                || args[1].equalsIgnoreCase("*")) {
                            sender.sendMessage(ChatColor.RED + "Disabling ALL plugins(except for Reloader)");
                            for(Plugin pl : plugins) {
                            	utilz(pl.getName());
                            	Utils.disable(pl.getName(), sender);
                            }
                        } else {
                        	util(args[1], sender);
                        	Utils.disable(args[1], sender);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("all")
                                || args[1].equalsIgnoreCase("*")) {
                            for(Plugin pl : plugins) {
                                Utils.enable(pl.getName(), sender);
                            }
                            sender.sendMessage(ChatColor.GREEN + "Enabling all plugins");
                        } else {
                        	Utils.enable(args[1], sender);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Invalid Args");
                    }
                } else if (args[0].equalsIgnoreCase("load")) {
                    if (args.length == 2) {
                        Utils.load(args[1],
                                plugin.getServer().getPlayer(sender.getName()));
                    } else {
                        sender.sendMessage(ChatColor.RED + "Invalid Args");
                    }
                    sender.sendMessage(ChatColor.GREEN + "Plugin loaded and enabled");
                } else if (args[0].equalsIgnoreCase("unload")) {
                    utilz(args[1]);
                	Utils.unload(args[1]);
                    sender.sendMessage("§2Plugin unloaded and disabled");
                } else if (args[0].equalsIgnoreCase("check")) {
            } else if (args[0].equalsIgnoreCase("info")) {
            	return Utils.info(args[1], sender);
            } else if (args[0].equalsIgnoreCase("use")) {
            	return Utils.use(args[1], sender);
            } else if (args[0].equalsIgnoreCase("perm")) {
                if(args.length == 3) {
                	return Utils.perm(args[1], sender, args[2]);
                } else if(args.length == 2) {
                	return Utils.perm(sender, args[1]);
                }
            } else if (args[0].equalsIgnoreCase("list")) {
            	return Utils.list(sender);
            } else {
                    sender.sendMessage(ChatColor.RED + "Invalid Args");
                    return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid permissions");
            return true;
        }
        return true;
    }
}
