package me.jmgr2007.Reloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.UnknownDependencyException;

public class Utils {
	private static PluginManager pm = Bukkit.getServer().getPluginManager();
	private static boolean canceled;
	public static String canceledPl = "";
	protected static FileConfiguration locale = null;
	protected static File localeFile = null;
	private static Plugin plugin = pm.getPlugin("Reloader");
	
	public Utils(String name, CommandSender sender) {
		if(exempt(name))
			msg(sender, string("exempt", canceledPl));
		canceled = exempt(name);
	}

	public Utils(String name) {
		canceled = exempt(name);
	}

    public static void load(final String pluginName) {
        for(Plugin pl : pm.getPlugins()) {
        	if(pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
        		plugin.getLogger().info(string("loaded", pl.getName()));
        		return;
        	}
        }
        
        String name = "";
        String path = plugin.getDataFolder().getParent();
        File folder = new File(path);
        ArrayList<File> files = new ArrayList<File>();
        File[] listOfFiles = folder.listFiles();
        for (File compare : listOfFiles) {
            if (compare.isFile()) {
            	try {
					name = ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare).getName();
				} catch (InvalidDescriptionException e) {
					plugin.getLogger().info(string("ErrorA", compare.getName()));
				}
            	if(name.toLowerCase().startsWith(pluginName.toLowerCase())) {
            		files.add(compare);
            		try {
						pm.loadPlugin(compare);
					} catch (UnknownDependencyException e) {
						plugin.getLogger().info(string("ErrorC", compare.getName()));
						return;
					} catch (InvalidPluginException e) {
						plugin.getLogger().info(string("ErrorB", compare.getName()));
						return;
					} catch (InvalidDescriptionException e) {
						plugin.getLogger().info(string("ErrorA", compare.getName()));
						return;
					}
            	}
            }
        }
        
        Plugin[] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
        	for(File compare : files) {
        		try {
					if(pl.getName().equalsIgnoreCase(ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare).getName())) {
					    pm.enablePlugin(pl);
					}
				} catch (InvalidDescriptionException e) {
					e.printStackTrace();
				}
        	}
        }
        return;
    }

    public static void load(final String pluginName, CommandSender sender) {
        
        for(Plugin pl : pm.getPlugins()) {
        	if(pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
        		msg(sender, pl.getName());
        		msg(sender, pluginName);
        		msg(sender, string("loaded", pl.getName()));
        		return;
        	}
        }
        String name = "";
        ArrayList<File> files = new ArrayList<File>();
        File[] listOfFiles = new File(ReloaderListener.plugin.getDataFolder().getParent()).listFiles();
        for (File compare : listOfFiles) {
            if (compare.isFile() && compare.getName().toLowerCase().endsWith(".jar")) {
            	try {
					name = ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare).getName();
				} catch (InvalidDescriptionException e) {
					msg(sender, string("ErrorA", compare.getName()));
					return;
				}
            	if(name.toLowerCase().startsWith(pluginName.toLowerCase())) {
            		files.add(compare);
            		try {
						pm.loadPlugin(compare);
					} catch (UnknownDependencyException e) {
						msg(sender, string("ErrorC", compare.getName()));
						return;
					} catch (InvalidPluginException e) {
						msg(sender, string("ErrorB", compare.getName()));
						return;
					} catch (InvalidDescriptionException e) {
						msg(sender, string("ErrorA", compare.getName()));
						return;
					}
            	}
            }
        }
        
        Plugin[] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
        	for(File compare : files) {
        		try {
					if(pl.getName().equalsIgnoreCase(ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare).getName())) {
					    pm.enablePlugin(pl);
				        msg(sender, string("load", pl.getName()));
					}
				} catch (InvalidDescriptionException e) {
					e.printStackTrace();
					msg(sender, "");
					return;
				}
        	}
        }
        return;
    }
      
    public static void fload(final String pluginName, CommandSender sender) {        
        for(Plugin pl : pm.getPlugins())
        	if(pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
            	msg(sender, string("loaded", pl.getName()));
            	return;
        	}
        
        String name = "";
        ArrayList<File> files = new ArrayList<File>();
        File[] listOfFiles = new File(ReloaderListener.plugin.getDataFolder().getParent()).listFiles();
        for (File compare : listOfFiles) {
            if (compare.isFile() && compare.getName().toLowerCase().endsWith(".jar")) {
            	try {
					name = ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare).getName();
				} catch (InvalidDescriptionException e) {
					msg(sender, string("ErrorA", compare.getName()));
					return;
				}
            	if(name.toLowerCase().startsWith(pluginName.toLowerCase())) {
            		files.add(compare);
            		try {
						pm.loadPlugin(compare);
					} catch (UnknownDependencyException e) {
						msg(sender, string("ErrorC", compare.getName()));
						return;
					} catch (InvalidPluginException e) {
						msg(sender, string("ErrorB", compare.getName()));
						return;
					} catch (InvalidDescriptionException e) {
						msg(sender, string("ErrorA", compare.getName()));
						return;
					}
            	}
            }
        }
        
        Plugin[] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
        	for(File compare : files) {
        		try {
					if(pl.getName().equalsIgnoreCase(plugin.getPluginLoader().getPluginDescription(compare).getName())) {
					    pm.enablePlugin(pl);
					}
				} catch (InvalidDescriptionException e) {
					e.printStackTrace();
					msg(sender, "");
					return;
				}
        	}
        }
    }

    @SuppressWarnings("unchecked")
    public static void unload(String pluginName) {
    	
    	if(canceled)
    		return;
    	
    	pluginName = pluginName.toLowerCase().trim();
        SimplePluginManager spm = (SimplePluginManager) pm;
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> lookupNames = null;
        Map<String, Command> knownCommands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadlisteners = true;
        try {
	        if (spm != null) {
	            Field pluginsField = spm.getClass().getDeclaredField("plugins");
	            pluginsField.setAccessible(true);
	            plugins = (List<Plugin>) pluginsField.get(spm);
	
	            Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
	            lookupNamesField.setAccessible(true);
	            lookupNames = (Map<String, Plugin>) lookupNamesField.get(spm);
	
	            try {
	                Field listenersField = spm.getClass().getDeclaredField(
	                        "listeners");
	                listenersField.setAccessible(true);
	                listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField
	                        .get(spm);
	            } catch (Exception e) {
	                reloadlisteners = false;
	            }
	
	            Field commandMapField = spm.getClass().getDeclaredField(
	                    "commandMap");
	            commandMapField.setAccessible(true);
	            commandMap = (SimpleCommandMap) commandMapField.get(spm);
	
	            Field knownCommandsField = commandMap.getClass().getDeclaredField(
	                    "knownCommands");
	            knownCommandsField.setAccessible(true);
	            knownCommands = (Map<String, Command>) knownCommandsField
	                    .get(commandMap);
	        }
    	} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        boolean in = false;

        for (Plugin pl : pm.getPlugins()) {
            if (pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
                pm.disablePlugin(pl);
                if (plugins != null && plugins.contains(pl)) {
                    plugins.remove(pl);
                }

                if (lookupNames != null && lookupNames.containsKey(pl.getName())) {
                    lookupNames.remove(pl.getName());
                }

                if (listeners != null && reloadlisteners) {
                    for (SortedSet<RegisteredListener> set : listeners.values()) {
                        for (Iterator<RegisteredListener> it = set.iterator(); it
                                .hasNext();) {
                            RegisteredListener value = it.next();

                            if (value.getPlugin() == pl) {
                                it.remove();
                            }
                        }
                    }
                }

                if (commandMap != null) {
                    for (Iterator<Map.Entry<String, Command>> it = knownCommands
                            .entrySet().iterator(); it.hasNext();) {
                        Map.Entry<String, Command> entry = it.next();
                        if (entry.getValue() instanceof PluginCommand) {
                            PluginCommand c = (PluginCommand) entry.getValue();
                            if (c.getPlugin() == pl) {
                                c.unregister(commandMap);
                                it.remove();
                            }
                        }
                    }
                }
			    for (Plugin plu : pm.getPlugins()) {
			        if(plu.getDescription().getDepend() != null) {
				        for (String depend : plu.getDescription().getDepend()) {
				        	if(depend.equalsIgnoreCase(pl.getName())) {
				        		Utils.unload(plu.getName());
				        	}
				        }
			        }
			    }
			    in=true;
			    break;
		    }
	    }
        if(!in) {
        	Bukkit.getLogger().info(string("ErrorD", pluginName));
        }
        System.gc();
        return;
    }

    @SuppressWarnings("unchecked")
    public static void unload(String pluginName, CommandSender sender) {
    	
    	if(canceled) {
    		return;
    	}
    	
    	pluginName = pluginName.toLowerCase().trim();
        SimplePluginManager spm = (SimplePluginManager) pm;
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> lookupNames = null;
        Map<String, Command> knownCommands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadlisteners = true;
        try {
	        if (spm != null) {
	            Field pluginsField = spm.getClass().getDeclaredField("plugins");
	            pluginsField.setAccessible(true);
	            plugins = (List<Plugin>) pluginsField.get(spm);
	
	            Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
	            lookupNamesField.setAccessible(true);
	            lookupNames = (Map<String, Plugin>) lookupNamesField.get(spm);
	
	            try {
	                Field listenersField = spm.getClass().getDeclaredField(
	                        "listeners");
	                listenersField.setAccessible(true);
	                listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField
	                        .get(spm);
	            } catch (Exception e) {
	                reloadlisteners = false;
	            }
	
	            Field commandMapField = spm.getClass().getDeclaredField(
	                    "commandMap");
	            commandMapField.setAccessible(true);
	            commandMap = (SimpleCommandMap) commandMapField.get(spm);
	
	            Field knownCommandsField = commandMap.getClass().getDeclaredField(
	                    "knownCommands");
	            knownCommandsField.setAccessible(true);
	            knownCommands = (Map<String, Command>) knownCommandsField
	                    .get(commandMap);
	        }
    	} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        boolean in = false;

        for (Plugin pl : pm.getPlugins()) {
            if (pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
                pm.disablePlugin(pl);
                if (plugins != null && plugins.contains(pl)) {
                    plugins.remove(pl);
                }

                if (lookupNames != null && lookupNames.containsKey(pl.getName())) {
                    lookupNames.remove(pl.getName());
                }

                if (listeners != null && reloadlisteners) {
                    for (SortedSet<RegisteredListener> set : listeners.values()) {
                        for (Iterator<RegisteredListener> it = set.iterator(); it
                                .hasNext();) {
                            RegisteredListener value = it.next();

                            if (value.getPlugin() == pl) {
                                it.remove();
                            }
                        }
                    }
                }

                if (commandMap != null) {
                    for (Iterator<Map.Entry<String, Command>> it = knownCommands
                            .entrySet().iterator(); it.hasNext();) {
                        Map.Entry<String, Command> entry = it.next();
                        if (entry.getValue() instanceof PluginCommand) {
                            PluginCommand c = (PluginCommand) entry.getValue();
                            if (c.getPlugin() == pl) {
                                c.unregister(commandMap);
                                it.remove();
                            }
                        }
                    }
                }
			    for (Plugin plu : pm.getPlugins()) {
			        if(plu.getDescription().getDepend() != null) {
				        for (String depend : plu.getDescription().getDepend()) {
				        	if(depend.equalsIgnoreCase(pl.getName())) {
				        		Utils.unload(plu.getName());
				        	}
				        }
			        }
			    }
			    in = true;
	            msg(sender, string("unload", pl.getName()));
			    break;
		    }
	    }
        if(!in) {
        	msg(sender, string("ErrorD", pluginName));
        }
        if(in) {
        }
        System.gc();
        return;
    }

    public static void disable(String plugin) {
    	if(canceled)
    		return;
    	Plugin [] plugins = pm.getPlugins();
    	for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                pm.disablePlugin(pl);
            }
        }
        return;
    }

    public static void disable(String plugin, CommandSender sender) {
    	if(canceled) {
    		return;
    	}
    	boolean h = false;
    	Plugin [] plugins = pm.getPlugins();
    	for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                pm.disablePlugin(pl);
                h = true;
            	msg(sender, string("disable", pl.getName()));
            }
        }
    	if(!h) {
    		msg(sender, string("disableError", plugin));
    		return;
    	}
        return;
    }

    public static void hReload() {
    	Bukkit.reload();
    }

    public static void enable(String plugin) {
        Plugin[] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                pm.enablePlugin(pl);
            }
        }
        return;
    }
    
    public static void enable(String plugin, CommandSender sender) {
        Plugin[] plugins = pm.getPlugins();
        boolean h = false;
        for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                pm.enablePlugin(pl);
                h = true;
                msg(sender, string("enable",pl.getName()));
                return;
            }
        }
        if(!h) {
        	msg(sender, string("enableError", plugin));
        	return;
        }
    }

    @SuppressWarnings("rawtypes")
	public static void use(String plugin, CommandSender sender) {
        Plugin plug = null;
        if (plugin.trim() == "")
        	plugin = Utils.plugin.getName();
        Plugin [] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                plug = pl;
            }
        }
        if(plug == null) {
        	msg(sender, string("notFound", plugin));
        	return;
        }
        ArrayList<String> out = new ArrayList<String>();
        ArrayList<String> parsedCommands = new ArrayList<String>();
        Map commands = plug.getDescription().getCommands();

        if (commands != null) {
            Iterator commandsIt = commands.entrySet().iterator();
            while (commandsIt.hasNext()) {
                Map.Entry pluginEntry = (Map.Entry) commandsIt.next();
                if (pluginEntry != null) {
                    parsedCommands.add((String) pluginEntry.getKey());
                }
            }
        }

        if (!parsedCommands.isEmpty()) {

            StringBuilder commandsOut = new StringBuilder();
            msg(sender, ChatColor.RED + "Commands: ");    
            for (int i = 0; i < parsedCommands.size(); i++) {

                String pluginCommand = parsedCommands.get(i);

                if (commandsOut.length() + pluginCommand.length() > 55) {
                    msg(sender, commandsOut.toString());
                    commandsOut = new StringBuilder();
                }
                if (parsedCommands.size() < 10)
                	msg(sender, ChatColor.RED + "* " + ChatColor.GREEN + "/" + pluginCommand);
                else {
                	msg(sender,ChatColor.GREEN + parsedCommands.toString().replace("[","").replace("]", ""));
                	break;
                }
            }
            out.add(commandsOut.toString());
        }

        if(plug.getDescription().getPermissions() != null) {
            List<Permission> perms = plug.getDescription().getPermissions();
            if(perms.size() != 0) {
                msg(sender, ChatColor.RED + "Permissions:");
                if (perms.size() < 10) {
	                for(int i = 0; i < perms.size(); i++) {
	                    msg(sender, ChatColor.RED + "* " + ChatColor.GREEN + perms.get(i).getName());
	                }
                } else {
                	msg(sender, ChatColor.GREEN + perms.toString().replace("[", "").replace("]", ""));
                }
            }
        }
        return;
    }

    public static void info(String plugin, CommandSender sender) {
        Plugin plug = null;
        Plugin [] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                plug = pl;
            }
        }
        if (plugin == "")
        	plug = Utils.plugin;
        if(plug == null) {
        	msg(sender, string("notFound", plugin));
        	return;
        }
        
        if(plugin != null) {
            msg(sender, ChatColor.RED + "Plugin info: " + ChatColor.GREEN + plug.getName());
            if(plug.getDescription().getAuthors() != null && !plug.getDescription().getAuthors().isEmpty()) {
                String author = "";
                List<String> authors = plug.getDescription().getAuthors();
                for(int i = 0; i < authors.size(); i++) {
                    if(i == 0)
                        author = authors.get(i);
                    if(i > 1)
                        author = author + ", " + authors.get(i);
                }
                msg(sender, ChatColor.RED + "Author(s): " + ChatColor.GREEN + author);
            }
            if(plug.getDescription().getDescription() != null && !plug.getDescription().getDescription().isEmpty())
                msg(sender, ChatColor.RED + "Description: " + ChatColor.GREEN + plug.getDescription().getDescription());
            if(plug.getDescription().getVersion() != null && !plug.getDescription().getVersion().isEmpty())
                msg(sender, ChatColor.RED + "Version: " + ChatColor.GREEN + plug.getDescription().getVersion());
            if(plug.getDescription().getWebsite() != null && !plug.getDescription().getWebsite().isEmpty()) 
                msg(sender, ChatColor.RED + "Website: " + ChatColor.GREEN + plug.getDescription().getWebsite());
            if(plug.getDescription().getDepend() != null && !plug.getDescription().getDepend().isEmpty()) {
                msg(sender, ChatColor.RED + "Required plugins");
                    List<String> depends = plug.getDescription().getDepend();
                    for(int i = 0; i < depends.size(); i++) {
                            msg(sender, ChatColor.RED + "* " + ChatColor.GREEN + depends.get(i));
                    }
                }
            if(plug.getDescription().getSoftDepend() != null && !plug.getDescription().getSoftDepend().isEmpty()) {
                msg(sender, ChatColor.RED + "Recommended plugins");
                    List<String> depends = plug.getDescription().getSoftDepend();
                    for(int i = 0; i < depends.size(); i++) {
                            msg(sender, ChatColor.RED + "* " + ChatColor.GREEN + depends.get(i));
                    }
                }
            }
        	return;
    }

    public static boolean check(String plugin, CommandSender sender) {
        Plugin plug = null;
        Plugin [] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                plug = pl;
            }
        }
        if(plug != null) {
            if(plug.isEnabled()) {
                msg(sender, string("enabled",plug.getName()));
            } else {
                msg(sender, string("disabled", plug.getName()));
            }
            return true;
        } else {
            msg(sender, string("notFound", plugin));
            return true;
        }
    }

    public static boolean perm(CommandSender sender, String permission) {
    	if(pm.getPermission(permission) == null) {
    		msg(sender, string("permNotFound", permission));
    		return true;
    	}
        if(sender.hasPermission(permission)) {
            msg(sender, string("senderPerm", permission));
        } else {
            msg(sender, string("senderNoPerm", permission));
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
	public static boolean perm(String player, CommandSender sender, String permission) {
    	if(pm.getPermission(permission) == null) {
    		msg(sender, string("permNotFound", permission));
    		return true;
    	}
    	if(Bukkit.getServer().getPlayer(player) != null) {
            Player target = Bukkit.getServer().getPlayer(player);
	        if(target.hasPermission(permission)) {
	            msg(sender, string("userPerm", permission).replaceAll("%USER%", player));
	        } else {
	            msg(sender, string("userNoPerm", permission).replaceAll("%USER%", player));
	        }
    	}
        return true;
    }
    
    public static boolean list(CommandSender sender) {
        Plugin[] plugins = pm.getPlugins();
        ArrayList<String> enabled = new ArrayList<String>();
        ArrayList<String> disabled = new ArrayList<String>();
        for(Plugin pl : plugins) {
            if(pl.isEnabled())
                enabled.add(pl.getName());
            else
                disabled.add(pl.getName());
        }
        Collections.sort(enabled,  String.CASE_INSENSITIVE_ORDER);
        Collections.sort(disabled,  String.CASE_INSENSITIVE_ORDER);
        if(plugins.length != 0)
        	msg(sender, ChatColor.GREEN + "" + plugins.length + " plugins loaded");
        if(!enabled.isEmpty()) {
            msg(sender, ChatColor.GOLD + "Enabled:");
            String enable = "";
            for(int i = 0; i < enabled.size(); i++) {
                enable = enable + ", "  + enabled.get(i);
            }
            enable = enable.replaceFirst(", ", "");
            msg(sender, ChatColor.GREEN + enable);
        }
        if(!disabled.isEmpty()) {
            String disable = "";
            msg(sender, ChatColor.GOLD + "Disabled:");
            for(int i = 0; i < disabled.size(); i++) {
                disable = disable + ", " + disabled.get(i);
            }
            disable = disable.replaceFirst(", ", "");
            msg(sender, ChatColor.RED + disable);
        }
        return true;
    }
    
    public static boolean vList(CommandSender sender) {
        Plugin[] plugins = pm.getPlugins();
        ArrayList<String> enabled = new ArrayList<String>();
        ArrayList<String> disabled = new ArrayList<String>();
        for(Plugin pl : plugins) {
            if(pl.isEnabled())
                enabled.add(pl.getName());
            else
                disabled.add(pl.getName());
        }
        Collections.sort(enabled,  String.CASE_INSENSITIVE_ORDER);
        Collections.sort(disabled,  String.CASE_INSENSITIVE_ORDER);
        if(plugins.length != 0)
        	msg(sender, ChatColor.GREEN + "" + plugins.length + " plugins loaded");
        if(!enabled.isEmpty()) {
            msg(sender, ChatColor.GOLD + "Enabled:");
            String enable = "";
            for(int i = 0; i < enabled.size(); i++) {
                enable = enable + ChatColor.GREEN + ", "  + enabled.get(i) + ChatColor.GRAY + " " + pm.getPlugin(enabled.get(i)).getDescription().getVersion();
            }
            enable = enable.replaceFirst(", ", "");
            msg(sender, ChatColor.GREEN + enable);
        }
        if(!disabled.isEmpty()) {
            String disable = "";
            msg(sender, ChatColor.GOLD + "Disabled:");
            for(int i = 0; i < disabled.size(); i++) {
                disable = disable + ", " + disabled.get(i) + ChatColor.GRAY +" " + pm.getPlugin(disabled.get(i)).getDescription().getVersion();
            }
            disable = disable.replaceFirst(", ", "");
            msg(sender, ChatColor.RED + disable);
        }
        return true;
    }
    
    public static void msg(CommandSender sender, String msg) {
    	if (sender instanceof Player) {
    		sender.sendMessage(msg);
    	} else {
    		Logger log = Bukkit.getServer().getLogger();
    		log.info(msg);
    	}
    }
    
    public static boolean help(CommandSender sender) {
    	msg(sender, string("helpHead"));
    	if(permCheck(sender, "reloader.reload"))
    		msg(sender, string("helpReload"));
    	if(permCheck(sender, "reloader.disable"))
    		msg(sender, string("helpDisable"));
    	if(permCheck(sender, "reloader.enable"))
    		msg(sender, string("helpEnable"));
    	if(permCheck(sender, "reloader.load"))
    		msg(sender, string("helpLoad"));
    	if(permCheck(sender, "reloader.unload"))
    		msg(sender, string("helpUnload"));
    	if(permCheck(sender, "reloader.check"))
    		msg(sender, string("helpCheck"));
    	if(permCheck(sender, "reloader.info"))
    		msg(sender, string("helpInfo"));
    	if(permCheck(sender, "reloader.use"))
    		msg(sender, string("helpUse"));
    	if(permCheck(sender, "reloader.perm"))
    		msg(sender, string("helpPerm"));
    	if(permCheck(sender, "reloader.list"))
    		msg(sender, string("helpList"));
    	if(permCheck(sender, "reloader.list"))
    		msg(sender, string("helpListV"));
    	if(permCheck(sender, "reloader.config"))
    		msg(sender, string("helpConfig"));
        return true;
    }
    
    public static boolean exempt(String name) {
    	for(String ex : plugin.getConfig().getStringList("exempt")) {
    		if(ex.toLowerCase().startsWith(name.toLowerCase())){
    			canceledPl = ex;
    			return true;
    		}
        }
    	return false;
    }
    
    public static String join(String [] args) {
    	String l = "";
    	for(int i = 1; i < args.length; i++) {
    		l = l + " " + args[i];
    	}
    	l = l.toLowerCase().trim();
		return l;
    }
    public static void scheduler() {
        if (!plugin.getConfig().getString("timer.message").isEmpty())
        	Bukkit.getServer().broadcastMessage(plugin.getConfig().getString("timer.message").replaceAll("&(?=[0-9a-fA-FkKmMoOlLnNrR])", "\u00a7"));
    	Plugin[] plugins = pm.getPlugins();
    	if(plugin.getConfig().getBoolean("timer.all")) {
	        for(Plugin pl : plugins) {
	            if(!pl.getName().toLowerCase().startsWith(plugin.getName().toLowerCase()) && !(Utils.exempt(pl.getName()))) {
	                Utils.unload(pl.getName());
	                Utils.load(pl.getName());
	            }
	        }
    	} else {
    		for (String li : plugin.getConfig().getStringList("timer.list")) {
    			for (Plugin pl : pm.getPlugins()) {
    				if (pl.getName().toLowerCase().startsWith(li.toLowerCase()) && !exempt(pl.getName())) {
    	                Utils.unload(pl.getName());
    	                Utils.load(pl.getName());
    				}
    			}
    		}
    		
    		
    	}
    }
    public static void localize() {
    	plugin.saveResource("locale.yml", true);
    	locale = YamlConfiguration.loadConfiguration(localeFile);
    	try {
			locale.load(localeFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
    	plugin.getServer().broadcastMessage(locale.getString("test"));
    	
    }
    public static String string(String s) {
    	if(locale.getString(s) != null) {
    		return locale.getString(s).replaceAll("&(?=[0-9a-fA-FkKmMoOlLnNrR])", "\u00a7");
    	} else {
    		return "";
    	}
    }
    public static String string(String s, String replac) {
    	if(locale.getString(s) != null) {
    		return locale.getString(s).replaceAll("&(?=[0-9a-fA-FkKmMoOlLnNrR])", "\u00a7").replaceAll("%NAME%", replac);
    	} else {
    		return "";
    	}
    }
    public static boolean permCheck(CommandSender sender, String perm) {
    	return sender.hasPermission(perm);
    }
}
