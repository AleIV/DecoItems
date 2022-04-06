package me.aleiv.core.paper.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import me.aleiv.core.paper.Core;

public class CanceledListener implements Listener{
    
    Core instance;

    public CanceledListener(Core instance){
        this.instance = instance;
    }

    @EventHandler
    public void onRenameCustomModel(PrepareAnvilEvent e){
        var inv = e.getInventory();
        var item = inv.getItem(0);
        var manager = instance.getDecoItemsManager();
        var result = e.getResult();
        if(item != null && manager.isDecoItem(item)){
            e.setResult(null);

        }else if(result != null && manager.isDecoItem(result)){
            e.setResult(null);
        }
    }

    @EventHandler
    public void onCraftCustomModel(PrepareItemCraftEvent e){
        var table = e.getInventory().getContents();
        var manager = instance.getDecoItemsManager();

        for (var item : table) {
            if(item != null && item.hasItemMeta() && manager.isDecoItem(item)){
                e.getInventory().setResult(null);
                return;
            }
        }

    }

    @EventHandler
    public void onStandDrop(ItemSpawnEvent e){
        var item = e.getEntity();
        if(item.getItemStack().getType() == Material.ARMOR_STAND){
            e.setCancelled(true);
        }
    }
    
}
