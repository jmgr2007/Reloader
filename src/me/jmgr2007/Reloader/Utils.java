package me.jmgr2007.Reloader;

import java.io.File;
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
	private static Plugin plugin = pm.getPlugin("Reloader");
	
	public Utils(String name, CommandSender sender) {
		if(exempt(name))
			msg(sender, ChatColor.RED + "This plugin is exempt");
		canceled = exempt(name);
	}

	public Utils(String name) {
		canceled = exempt(name);
	}

    public static void load(final String pluginName) {
        
        
        boolean there = false;
        
        for(Plugin pl : pm.getPlugins())
        	if(pl.getName().toLowerCase().startsWith(pluginName))
        		there = true;
        
        if(there) {
        	System.out.print("Plugin already loaded");
        	return;
        } else {
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
						System.out.print(compare.getName() + "didn't match");
					}
	            	if(name.toLowerCase().startsWith(pluginName.toLowerCase())) {
	            		files.add(compare);
	            		try {
							pm.loadPlugin(compare);
						} catch (UnknownDependencyException e) {
							System.out.print(compare.getName() + "is missing a dependant plugin");
							return;
						} catch (InvalidPluginException e) {
							System.out.print(compare.getName() + "is not a plugin");
							return;
						} catch (InvalidDescriptionException e) {
							System.out.print(compare.getName() + "has an incorrect description");
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
						    Vars.loaded.increment();
						    Vars.enabled.increment();
						}
					} catch (InvalidDescriptionException e) {
						e.printStackTrace();
					}
	        	}
	        }
        }
        return;
    }

    public static void load(final String pluginName, CommandSender sender) {
        
        boolean there = false;
        
        for(Plugin pl : pm.getPlugins())
        	if(pl.getName().toLowerCase().startsWith(pluginName))
        		there = true;
        
        if(there) {
        	msg(sender, ChatColor.RED + "Plugin already loaded");
        	return;
        } else {
	        String name = "";
	        ArrayList<File> files = new ArrayList<File>();
	        File[] listOfFiles = new File(ReloaderListener.plugin.getDataFolder().getParent()).listFiles();
	        for (File compare : listOfFiles) {
	            if (compare.isFile() && compare.getName().toLowerCase().endsWith(".jar")) {
	            	try {
						name = ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare).getName();
					} catch (InvalidDescriptionException e) {
						msg(sender, ChatColor.RED + compare.getName() + " has an incorect description");
						return;
					}
	            	if(name.toLowerCase().startsWith(pluginName.toLowerCase())) {
	            		files.add(compare);
	            		try {
							pm.loadPlugin(compare);
						} catch (UnknownDependencyException e) {
							msg(sender, ChatColor.RED + compare.getName() + " is missing a dependant plugin");
							return;
						} catch (InvalidPluginException e) {
							msg(sender, ChatColor.RED + compare.getName() + " is not a plugin");
							return;
						} catch (InvalidDescriptionException e) {
							msg(sender, ChatColor.RED + compare.getName() + " has an incorrect description");
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
						    Vars.loaded.increment();
						    Vars.enabled.increment();
						}
					} catch (InvalidDescriptionException e) {
						e.printStackTrace();
						msg(sender, "");
						return;
					}
	        	}
	        }
        }
        msg(sender, ChatColor.GREEN + "Plugin loaded and enabled");
        return;
    }
      
    public static void fload(final String pluginName, CommandSender sender) {
        
        boolean there = false;
        
        for(Plugin pl : pm.getPlugins())
        	if(pl.getName().toLowerCase().startsWith(pluginName))
        		there = true;
        
        if(there) {
        	msg(sender, ChatColor.RED + "Plugin already loaded");
        	return;
        } else {
	        String name = "";
	        ArrayList<File> files = new ArrayList<File>();
	        File[] listOfFiles = new File(ReloaderListener.plugin.getDataFolder().getParent()).listFiles();
		    for (File compare : listOfFiles) {
	            if (compare.isFile() && compare.getName().toLowerCase().endsWith(".jar")) {
	            	try {
						ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare);
					} catch (InvalidDescriptionException e) {
						msg(sender, ChatColor.RED + compare.getName() + " has an incorect description");
						return;
					}
	            	name = compare.getName();
	            	if(name.toLowerCase().startsWith(pluginName.toLowerCase())) {
	            		files.add(compare);
	            		try {
							pm.loadPlugin(compare);
						} catch (UnknownDependencyException e) {
							msg(sender, ChatColor.RED + compare.getName() + " is missing a dependant plugin");
							return;
						} catch (InvalidPluginException e) {
							msg(sender, ChatColor.RED + compare.getName() + " is not a plugin");
							return;
						} catch (InvalidDescriptionException e) {
							msg(sender, ChatColor.RED + compare.getName() + " has an incorrect description");
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
						    Vars.loaded.increment();
						    Vars.enabled.increment();
						}
					} catch (InvalidDescriptionException e) {
						e.printStackTrace();
						msg(sender, "");
						return;
					}
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
        	if(in)
        		break;
            if (pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
                pm.disablePlugin(pl);
                if (plugins != null && plugins.contains(pl)) {
                    plugins.remove(pl);
                    Vars.unloaded.increment();
                    Vars.disabled.increment();
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
		    }
	    }
        if(!in) {
        	Bukkit.getLogger().info("Not an existing plugin");
        }
        System.gc();
        return;
    }

    @SuppressWarnings("unchecked")
    public static void unload(String pluginName, CommandSender sender) {
    	
    	if(canceled) {
    		msg(sender, ChatColor.RED + "This plugin cannot be unloaded");
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
        	if(in)
        		break;
            if (pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
                pm.disablePlugin(pl);
                if (plugins != null && plugins.contains(pl)) {
                    plugins.remove(pl);
                    Vars.unloaded.increment();
                    Vars.disabled.increment();
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
		    }
	    }
        if(!in) {
        	msg(sender, ChatColor.RED + "Not an existing plugin");
        }
        if(in) {
            msg(sender, ChatColor.GREEN + "Plugin unloaded and disabled");
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
                Vars.disabled.increment();
            }
        }
        return;
    }

    public static void disable(String plugin, CommandSender sender) {
    	if(canceled) {
    		msg(sender, ChatColor.RED + "This plugin cannot be disabled");
    		return;
    	}
    	boolean h = false;
    	Plugin [] plugins = pm.getPlugins();
    	for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                pm.disablePlugin(pl);
                Vars.disabled.increment();
                h = true;
            }
        }
    	if(!h) {
    		msg(sender, ChatColor.GREEN + "Plugin couldn't be disabled");
    		return;
    	}
    	msg(sender, ChatColor.GREEN + "Plugin disabled");
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
                Vars.enabled.increment();
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
                Vars.enabled.increment();
                h = true;
            }
        }
        if(!h) {
        	msg(sender, ChatColor.RED + "Plugin couldn't be enabled");
        	return;
        }
        msg(sender, ChatColor.GREEN + "Plugin enabled");
        return;
    }

    @SuppressWarnings("rawtypes")
	public static void use(String plugin, CommandSender sender) {
        Plugin plug = null;
        if (plugin.trim() == "")
        	plugin = "Reloader";
        Plugin [] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                plug = pl;
            }
        }
        if(plug == null) {
        	msg(sender, ChatColor.RED + "Plugin couldn't be found");
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
        	msg(sender, ChatColor.RED + "Plugin couldn't be found");
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
                msg(sender, ChatColor.GREEN + plug.getName() + " is enabled");
            } else {
                msg(sender, ChatColor.RED + plug.getName() + " Is disabled");
            }
            return true;
        } else {
            msg(sender, "This is not a plugin loaded on plugin server");
            return true;
        }
    }

    public static boolean perm(CommandSender sender, String permission) {
        if(sender.hasPermission(permission)) {
            msg(sender, ChatColor.GREEN + "You have permission " + permission);
        } else {
            msg(sender, ChatColor.RED + "You don't have permission " + permission);
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
	public static boolean perm(String player, CommandSender sender, String permission) {
    	if(Bukkit.getServer().getPlayer(player) != null) {
            Player target = Bukkit.getServer().getPlayer(player);
	        if(target.hasPermission(permission)) {
	            msg(sender, ChatColor.GREEN + target.getName() + " has permission " + permission);
	        } else {
	            msg(sender, ChatColor.RED + target.getName() + " doesn't have permission " + permission);
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
    	msg(sender, "§6----------- §cReloader help §6-----------");
    	msg(sender, "§4/reloader reload <Plugin|all|*|harsh> §6-- §cReload <Plugin>/reload all plugins in the server/reload plugins and load new ones");
    	msg(sender, "§4/reloader disable <Plugin|all|*> §6-- §cDisable <Plugin>");
    	msg(sender, "§4/reloader enable <Plugin|all|*> §6-- §cEnable <Plugin>");
    	msg(sender, "§4/reloader load <File> §6-- §cLoad <File>");
    	msg(sender, "§4/reloader unload <File> §6-- §cUn-Load <File>");
    	msg(sender, "§4/reloader check <Plugin> §6-- §cCheck whether or not <Plugin> is enabled");
    	msg(sender, "§4/reloader info <Plugin> §6-- §cGives info on <Plugin>");
    	msg(sender, "§4/reloader use <Plugin> §6-- §cGives info on how to use <Plugin>");
    	msg(sender, "§4/reloader perm [Player] <Permission> §6-- §cTells you if you or [Player] has <Permission>");
    	msg(sender, "§4/reloader list §6-- §cList plugins in alphabetical order and sorts them by enabled or disabled");
    	msg(sender, "§4/reloader list -v §6-- §cAdds versions to the plugin list");
    	msg(sender, "§4/reloader config [plugin] §6-- §cReload [plugin]'s config or leave blank to reload Reloader's config");
        return true;
    }
    
    public static boolean aHelp(CommandSender sender) {
    	msg(sender, "§6----------- §cReloader help §6-----------");
    	msg(sender, "§4/reloader list §6-- §cList plugins in alphabetical order and sorts them by enabled or disabled");
    	msg(sender, "§4/reloader list -v §6-- §cList plugins in alphabetical order and sorts them by enabled or disabled with version included");
        return true;
    }
    
    public static boolean exempt(String name) {
    	for(String ex : Bukkit.getPluginManager().getPlugin("Reloader").getConfig().getStringList("exempt")) {
    		if(ex.toLowerCase().startsWith(name.toLowerCase())){
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
	            if(!pl.getName().toLowerCase().startsWith("reloader") && !(Utils.exempt(pl.getName()))) {
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
}
