package me.jmgr2007.Reloader;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Pl implements Listener {
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().equalsIgnoreCase("/pl") || event.getMessage().equalsIgnoreCase("/plugins")) {
			if(Bukkit.getPluginManager().getPlugin("Reloader") != null && Bukkit.getServer().getPluginManager().getPlugin("Reloader").getConfig().getBoolean("pl", true) && event.getPlayer().hasPermission("reloader.list")) {
				Utils.list(event.getPlayer());
				event.setCancelled(true);
			}
			if(Bukkit.getPluginManager().getPlugin("Reloader") != null && Bukkit.getServer().getPluginManager().getPlugin("Reloader").getConfig().getBoolean("default-pl", false)) {
				event.getPlayer().sendMessage("§cYou can not use this command on this server");
				event.setCancelled(true);
			}
		}
	}
}
