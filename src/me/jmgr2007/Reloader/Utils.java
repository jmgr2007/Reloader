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
	
	public Utils(String name, CommandSender sender) {
		if(exempt(name))
			msg(sender, ChatColor.RED + "This plugin is exempt");
		canceled = exempt(name);
	}

	public Utils(String name) {
		canceled = exempt(name);
	}

    public static void load(final String pluginName) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        
        
        boolean there = false;
        
        for(Plugin pl : pm.getPlugins())
        	if(pl.getName().toLowerCase().startsWith(pluginName))
        		there = true;
        
        if(there) {
        	System.out.print("Plugin already enabled");
        	return;
        } else {
	        String name = "";
	        String path = pm.getPlugin("Reloader").getDataFolder().getParent();
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
							Bukkit.getServer().getPluginManager().loadPlugin(compare);
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
        PluginManager pm = Bukkit.getServer().getPluginManager();
        
        boolean there = false;
        
        for(Plugin pl : pm.getPlugins())
        	if(pl.getName().toLowerCase().startsWith(pluginName))
        		there = true;
        
        if(there) {
        	msg(sender, "§c" + "Plugin already enabled");
        	return;
        } else {
	        String name = "";
	        String path = ReloaderListener.plugin.getDataFolder().getParent();
	        File folder = new File(path);
	        ArrayList<File> files = new ArrayList<File>();
	        File[] listOfFiles = folder.listFiles();
	        for (File compare : listOfFiles) {
	            if (compare.isFile() && compare.getName().endsWith(".jar")) {
	            	try {
						name = ReloaderListener.plugin.getPluginLoader().getPluginDescription(compare).getName();
					} catch (InvalidDescriptionException e) {
						msg(sender, "§c" + compare.getName() + " has an incorect description");
						return;
					}
	            	if(name.toLowerCase().startsWith(pluginName.toLowerCase())) {
	            		files.add(compare);
	            		try {
							Bukkit.getServer().getPluginManager().loadPlugin(compare);
						} catch (UnknownDependencyException e) {
							msg(sender, "§c" + compare.getName() + "is missing a dependant plugin");
							return;
						} catch (InvalidPluginException e) {
							msg(sender, "§c" + compare.getName() + "is not a plugin");
							return;
						} catch (InvalidDescriptionException e) {
							msg(sender, "§c" + compare.getName() + " has an incorrect description");
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
        msg(sender, "§aPlugin loaded and enabled");
        return;
    }
    
/*    public static boolean fload(final String pluginName, CommandSender sender) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        String name = "";
        String pname = "";
        if (pluginName.toLowerCase().endsWith(".jar")) {
            name = pluginName.replaceAll(".jar", "");
        } else {
            name = pluginName;
        }
        String path = "./plugins";
        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                int num = i;
                if (files.toLowerCase().startsWith(name.toLowerCase())) {
                    try {
                        pm.loadPlugin(listOfFiles[num]);
                        pname = listOfFiles[num].getName();
                        pname = pname.replaceAll(".jar", "");
                        Vars.loaded.increment();
                    } catch (UnknownDependencyException e) {
                        msg(sender, ChatColor.RED + "Not a plugin file OR has an underlying problem");
                    } catch (InvalidPluginException e) {
                        msg(sender, ChatColor.RED + "Not a plugin file OR has an underlying problem");
                    } catch (InvalidDescriptionException e) {
                        msg(sender, ChatColor.RED + "Not a plugin file OR has an underlying problem");
                    }
                }
            }
        }
        Plugin[] plugins = pm.getPlugins();
        for(int i = 0; i < plugins.length; i++) {
            if(plugins[i].getName().equalsIgnoreCase(pname)) {
                pm.enablePlugin(plugins[i]);
                Vars.enabled.increment();
            }
        }
        return true;
    }*/

    @SuppressWarnings("unchecked")
    public static void unload(String pluginName) {
    	
    	if(canceled)
    		return;
    	
    	pluginName = pluginName.toLowerCase().trim();
        PluginManager manager = Bukkit.getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager) manager;
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

        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
        	if(in)
        		break;
            if (pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
                manager.disablePlugin(pl);
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
			    for (Plugin plu : Bukkit.getServer().getPluginManager().getPlugins()) {
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
    	
    	if(canceled)
    		return;
    	
    	pluginName = pluginName.toLowerCase().trim();
        PluginManager manager = Bukkit.getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager) manager;
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

        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
        	if(in)
        		break;
            if (pl.getName().toLowerCase().startsWith(pluginName.toLowerCase())) {
                manager.disablePlugin(pl);
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
			    for (Plugin plu : Bukkit.getServer().getPluginManager().getPlugins()) {
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
        	msg(sender, "§cNot an existing plugin");
        }
        if(in) {
            msg(sender, "§aPlugin unloaded and disabled");
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
    	if(canceled)
    		return;
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
    		msg(sender, "§cPlugin couldn't be disabled");
    		return;
    	}
    	msg(sender, "§aPlugin disabled");
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
        	msg(sender, "§cPlugin couldn't be enabled");
        	return;
        }
        msg(sender, "§aPlugin enabled");
        return;
    }

    @SuppressWarnings("rawtypes")
	public static void use(String plugin, CommandSender sender) {
        Plugin plug = null;
        Plugin [] plugins = pm.getPlugins();
        for(Plugin pl : plugins) {
            if(pl.getName().toLowerCase().startsWith(plugin.toLowerCase())) {
                plug = pl;
            }
        }
        if(plug == null) {
        	msg(sender, "§cPlugin couldn't be found");
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
            msg(sender, "§cCommands: ");    
            for (int i = 0; i < parsedCommands.size(); i++) {

                String pluginCommand = parsedCommands.get(i);

                if (commandsOut.length() + pluginCommand.length() > 55) {
                    msg(sender, commandsOut.toString());
                    commandsOut = new StringBuilder();
                }

                if (parsedCommands.size() > 0) {
                    msg(sender, "§c* §a/" + pluginCommand);
                } else {
                    msg(sender, "§c* §a/" + pluginCommand);
                }

            }

            out.add(commandsOut.toString());

            if(plug.getDescription().getPermissions() != null) {
                List<Permission> perms = plug.getDescription().getPermissions();
                if(perms.size() != 0)
                    msg(sender, "§cPermissions:");
                for(int i = 0; i < perms.size(); i++) {
                    msg(sender, "§c* §a" + perms.get(i).getName());
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
        if(plug == null) {
        	msg(sender, "§cPlugin couldn't be found");
        	return;
        }
        
        if(plugin != null) {
            msg(sender, "§cPlugin info: §a" + plug.getName());
            if(plug.getDescription().getAuthors() != null) {
                String author = "";
                List<String> authors = plug.getDescription().getAuthors();
                for(int i = 0; i < authors.size(); i++) {
                    if(i == 0)
                        author = authors.get(i);
                    if(i > 1)
                        author = author + ", " + authors.get(i);
                }
                msg(sender, "§cAuthor(s): §a" + author);
            }
            if(plug.getDescription().getDescription() != null)
                msg(sender, "§cDescription: §a" + plug.getDescription().getDescription());
            if(plug.getDescription().getVersion() != null)
                msg(sender, "§cVersion: §a" + plug.getDescription().getVersion());
            if(plug.getDescription().getWebsite() != null) 
                msg(sender, "§cWebsite: §a" + plug.getDescription().getWebsite());
            if(plug.getDescription().getDepend() != null) {
                msg(sender, "§cRequired plugins");
                    List<String> depends = plug.getDescription().getDepend();
                    for(int i = 0; i < depends.size(); i++) {
                            msg(sender, "§c* §a" + depends.get(i));
                    }
                }
            if(plug.getDescription().getSoftDepend() != null) {
                msg(sender, "§cRecommended plugins");
                    List<String> depends = plug.getDescription().getSoftDepend();
                    for(int i = 0; i < depends.size(); i++) {
                            msg(sender, "§c* §a" + depends.get(i));
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
                msg(sender, "§a" + plug.getName() + " is enabled");
            } else {
                msg(sender, "§c" + plug.getName() + " Is disabled");
            }
            return true;
        } else {
            msg(sender, "This is not a plugin loaded on plugin server");
            return true;
        }
    }

    public static boolean perm(CommandSender sender, String permission) {
        if(sender.hasPermission(permission)) {
            msg(sender, "§aYou have permission " + permission);
        } else {
            msg(sender, "§cYou don't have permission " + permission);
        }
        return true;
    }
    
    @SuppressWarnings("deprecation")
	public static boolean perm(String player, CommandSender sender, String permission) {
    	if(Bukkit.getServer().getPlayer(player) != null) {
            Player target = Bukkit.getServer().getPlayer(player);
	        if(target.hasPermission(permission)) {
	            msg(sender, "§a" + target.getName() + " has permission " + permission);
	        } else {
	            msg(sender, "§c" + target.getName() + " doesn't have permission " + permission);
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
        	msg(sender, "§a" + plugins.length + " plugins loaded");
        if(!enabled.isEmpty()) {
            msg(sender, "§6Enabled:");
            String enable = "";
            for(int i = 0; i < enabled.size(); i++) {
                enable = enable + ", "  + enabled.get(i);
            }
            enable = enable.replaceFirst(", ", "");
            msg(sender, "§a" + enable);
        }
        if(!disabled.isEmpty()) {
            String disable = "";
            msg(sender, "§6Disabled:");
            for(int i = 0; i < disabled.size(); i++) {
                disable = disable + ", " + disabled.get(i);
            }
            disable = disable.replaceFirst(", ", "");
            msg(sender, "§c" + disable);
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
        	msg(sender, "§a" + plugins.length + " plugins loaded");
        if(!enabled.isEmpty()) {
            msg(sender, "§6Enabled:");
            String enable = "";
            for(int i = 0; i < enabled.size(); i++) {
                enable = enable + ", "  + enabled.get(i) + " §7" + pm.getPlugin(enabled.get(i)).getDescription().getVersion() + "§a";
            }
            enable = enable.replaceFirst(", ", "");
            msg(sender, "§a" + enable);
        }
        if(!disabled.isEmpty()) {
            String disable = "";
            msg(sender, "§6Disabled:");
            for(int i = 0; i < disabled.size(); i++) {
                disable = disable + ", " + disabled.get(i) + " §7" + pm.getPlugin(enabled.get(i)).getDescription().getVersion() + "§c";
            }
            disable = disable.replaceFirst(", ", "");
            msg(sender, "§c" + disable);
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
    	msg(sender, "§4/reloader reload <Plugin|all|*|harsh> §6-- §cReload <Plugin>/reload all plugins in the server/reload plugins and load new one");
    	msg(sender, "§4/reloader disable <Plugin|all|*> §6-- §cDisable <Plugin>");
    	msg(sender, "§4/reloader enable <Plugin|all|*> §6-- §cEnable <Plugin>");
    	msg(sender, "§4/reloader load <File> §6-- §cLoad <File>");
    	msg(sender, "§4/reloader unload <File> §6-- §cUn-Load <File>");
    	msg(sender, "§4/reloader check <Plugin> §6-- §cCheck whether or not <Plugin> is enabled");
    	msg(sender, "§4/reloader info <Plugin> §6-- §cGives info on <Plugin>");
    	msg(sender, "§4/reloader use <Plugin> §6-- §cGives info on how to use <Plugin>");
    	msg(sender, "§4/reloader perm [Player] <Permission> §6-- §cTells you if you or [Player] has <Permission>");
    	msg(sender, "§4/reloader list §6-- §cList plugins in alphabetical order and sorts them by enabled or disabled");
    	msg(sender, "§4/reloader list §6-- §cList plugins in alphabetical order and sorts them by enabled or disabled with version included");
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
    		if(name.equalsIgnoreCase(ex))
    			return true;
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
}
