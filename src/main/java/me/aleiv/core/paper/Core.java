package me.aleiv.core.paper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.commands.DecoItemsCMD;
import me.aleiv.core.paper.listeners.CanceledListener;
import me.aleiv.core.paper.listeners.DecoItemsListener;
import me.aleiv.core.paper.objects.DecoItem;
import me.aleiv.core.paper.utilities.JsonConfig;
import me.aleiv.core.paper.utilities.NegativeSpaces;
import me.aleiv.core.paper.utilities.TCT.BukkitTCT;

@SpigotPlugin
public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private @Getter PaperCommandManager commandManager;
    private @Getter Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private @Getter DecoItemsManager decoItemsManager;

    @Override
    public void onEnable() {
        instance = this;

        BukkitTCT.registerPlugin(this);
        NegativeSpaces.registerCodes();
        
        //COMMANDS
        
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new DecoItemsCMD(this));

        //MANAGER

        this.decoItemsManager = new DecoItemsManager(this);
        pullJson();

        //LISTENERS

        Bukkit.getPluginManager().registerEvents(new CanceledListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DecoItemsListener(this), this);
        

    }

    @Override
    public void onDisable() {

    }

    public void pushJson(){
        try {
            var list = decoItemsManager.getDecoItems();
            var jsonConfig = new JsonConfig("decoitems.json");
            var json = gson.toJson(list);
            var obj = gson.fromJson(json, JsonObject.class);
            jsonConfig.setJsonObject(obj);
            jsonConfig.save();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void pullJson(){
        try {
            var jsonConfig = new JsonConfig("decoitems.json");
            var list = jsonConfig.getJsonObject();
            var iter = list.entrySet().iterator();
            var map = decoItemsManager.getDecoItems();

            while (iter.hasNext()) {
                var entry = iter.next();
                var name = entry.getKey();
                var value = entry.getValue();
                var obj = gson.fromJson(value, DecoItem.class);
                map.put(name, obj);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}