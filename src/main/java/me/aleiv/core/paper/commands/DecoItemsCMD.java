package me.aleiv.core.paper.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.DecoItemsManager.DecoTag;
import me.aleiv.core.paper.objects.DecoItem;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("decoitems")
@CommandPermission("admin.perm")
public class DecoItemsCMD extends BaseCommand {

    private @NonNull Core instance;

    public DecoItemsCMD(Core instance) {
        this.instance = instance;

        var manager = instance.getCommandManager();

        manager.getCommandCompletions().registerStaticCompletion("num", "0");
        manager.getCommandCompletions().registerStaticCompletion("name", "name");

        manager.getCommandCompletions().registerAsyncCompletion("bool", c -> {
            return ImmutableList.of("true", "false");
        });

        manager.getCommandCompletions().registerAsyncCompletion("decotag", c -> {
            return Arrays.stream(DecoTag.values()).map(val -> val.toString()).toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("materials", c -> {
            return Arrays.stream(Material.values()).map(val -> val.toString()).toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("decoitems", c -> {
            var decoItemsManager =  instance.getDecoItemsManager();
            var decoItems = decoItemsManager.getDecoItems();
            return decoItems.keySet().stream().toList();
        });

    }

    @Subcommand("give")
    @CommandCompletion("@decoitems")
    public void give(Player sender, String decoItemName){

        var inv = sender.getInventory();
        var decoItemsManager =  instance.getDecoItemsManager();
        var decoItems = decoItemsManager.getDecoItems();

        if(decoItems.containsKey(decoItemName)){
            
            var decoItem = decoItems.get(decoItemName);
            inv.addItem(decoItem.getItemStack());
            sender.sendMessage(ChatColor.YELLOW + "Given sender" + decoItem.getName() + ".");

        }else{
            sender.sendMessage(ChatColor.RED + "Deco Item " + decoItemName + " doesn't exist.");
        }
        
        
    }

    @Subcommand("tag")
    @CommandCompletion("@decotag @decoitems")
    public void tag(CommandSender sender, DecoTag decoTag, String decoItemName){

        var decoItemsManager =  instance.getDecoItemsManager();
        var decoItems = decoItemsManager.getDecoItems();

        if(decoItems.containsKey(decoItemName)){
            
            var decoItem = decoItems.get(decoItemName);
            var tags = decoItem.getDecoTags();

            if(tags.contains(decoTag)){
                tags.remove(decoTag);
                sender.sendMessage(ChatColor.YELLOW + "Deco Tag removed from " + decoItem.getName() + ".");
            }else{
                tags.add(decoTag);
                sender.sendMessage(ChatColor.YELLOW + "Deco Tag added to " + decoItem.getName() + ".");
            }
            instance.pushJson();

        }else{
            sender.sendMessage(ChatColor.RED + "Deco Item " + decoItemName + " doesn't exist.");
        }
        
        
    }

    @Subcommand("add")
    @CommandCompletion("@num @materials @decotag @name")
    public void add(CommandSender sender, int customModelData, Material material, DecoTag decoTag, String decoItemName) {

        var decoItemsManager =  instance.getDecoItemsManager();
        var decoItems = decoItemsManager.getDecoItems();

        var existModelData = !decoItems.values().stream().filter(cb -> cb.getCustomModelData() == customModelData).toList().isEmpty();

        if (decoItems.containsKey(decoItemName)) {

            sender.sendMessage(ChatColor.RED + "Can't add Deco Item " + decoItemName + ", name already registered.");

        }else if(existModelData){

            sender.sendMessage(ChatColor.RED + "Can't add Deco Item " + decoItemName + ", custom model data already exist.");

        }else {

            List<DecoTag> list = new ArrayList<>();
            list.add(decoTag);
            
            var customBlock = new DecoItem(decoItemName, customModelData, material, list);

            decoItems.put(decoItemName, customBlock);
            instance.pushJson();
            sender.sendMessage(ChatColor.YELLOW + "Added new Deco Item " + decoItemName + ".");
        }

    }

    @Subcommand("remove")
    @CommandCompletion("@decoitems")
    public void remove(CommandSender sender, String decoItemName) {
        var decoItemsManager =  instance.getDecoItemsManager();
        var decoItems = decoItemsManager.getDecoItems();

        if (decoItems.containsKey(decoItemName)) {
            decoItems.remove(decoItemName);
            instance.pushJson();
            sender.sendMessage(ChatColor.YELLOW + "Removed Deco Item " + decoItemName + ".");

        } else {

            sender.sendMessage(ChatColor.RED + "Deco Item " + decoItemName + " doesn't exist.");
        }

    }

    @Subcommand("list")
    public void list(CommandSender sender) {
        var decoItemsManager =  instance.getDecoItemsManager();
        var decoItems = decoItemsManager.getDecoItems();

        sender.sendMessage(ChatColor.YELLOW + "Custom block list: " + ChatColor.WHITE + decoItems.keySet().toString() + ".");
    }

    @Subcommand("tags")
    @CommandCompletion("@decoitems")
    public void tags(CommandSender sender, String decoItemName){

        var decoItemsManager =  instance.getDecoItemsManager();
        var decoItems = decoItemsManager.getDecoItems();

        if(decoItems.containsKey(decoItemName)){
            
            var decoItem = decoItems.get(decoItemName);

            sender.sendMessage(ChatColor.YELLOW + decoItem.getName() + " tags: " + decoItem.getDecoTags().toString());

        }else{
            sender.sendMessage(ChatColor.RED + "Deco Item " + decoItemName + " doesn't exist.");
        }
        
        
    }


}
