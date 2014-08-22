package me.jmgr2007.Reloader;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class Pl implements Listener {
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().equalsIgnoreCase("/pl") || event.getMessage().equalsIgnoreCase("/plugins")) {
			if(Bukkit.getPluginManager().getPlugin("Reloader") != null && Bukkit.getServer().getPluginManager().getPlugin("Reloader").getConfig().getBoolean("pl", true) && event.getPlayer().hasPermission("reloader.list")) 
				event.setMessage("/reloader list");
			if(Bukkit.getPluginManager().getPlugin("Reloader") != null && Bukkit.getServer().getPluginManager().getPlugin("Reloader").getConfig().getBoolean("default-pl", false)) {
				event.getPlayer().sendMessage("§cYou can not use this command on this server");
				event.setCancelled(true);
			}
		} else if (event.getMessage().equalsIgnoreCase("/pl -v") || event.getMessage().equalsIgnoreCase("/plugins -v")) {
			if(Bukkit.getPluginManager().getPlugin("Reloader") != null && Bukkit.getServer().getPluginManager().getPlugin("Reloader").getConfig().getBoolean("pl", true) && event.getPlayer().hasPermission("reloader.list")) 
				event.setMessage("/reloader list -v");
			if(Bukkit.getPluginManager().getPlugin("Reloader") != null && Bukkit.getServer().getPluginManager().getPlugin("Reloader").getConfig().getBoolean("default-pl", false)) {
				event.getPlayer().sendMessage("§cYou can not use this command on this server");
				event.setCancelled(true);
			}
		}
		    if (!event.isCancelled() && event.getMessage().toLowerCase().startsWith("/reload ")) 
		        event.setMessage("/reloader reload " + event.getMessage().substring(8));
	}
	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		if(event.getCommand().equalsIgnoreCase("/pl") || event.getCommand().equalsIgnoreCase("/plugins"))
			if(Bukkit.getPluginManager().getPlugin("Reloader").getConfig().getBoolean("pl", true))
				event.setCommand("/reloader list");
		    if (event.getCommand().toLowerCase().startsWith("/reload ")) 
		        event.setCommand("/reloader reload " + event.getCommand().substring(8));
	}
}
