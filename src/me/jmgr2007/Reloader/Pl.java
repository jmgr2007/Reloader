package me.jmgr2007.Reloader;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Pl implements Listener {
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().equalsIgnoreCase("pl") || event.getMessage().equalsIgnoreCase("plugins")) {
			Utils.list(event.getPlayer());
			event.setCancelled(true);
		}
	}
}
