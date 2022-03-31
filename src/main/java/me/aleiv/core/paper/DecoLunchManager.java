package me.aleiv.core.paper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import lombok.Data;
import me.aleiv.core.paper.objects.DecoItem;

@Data
public class DecoLunchManager{

    Core instance;

    public static HashMap<String, DecoItem> decoItems = new HashMap<>();

    public DecoLunchManager(Core instance) {
        this.instance = instance;

        initSpecialDecoItems();
        initAnnotations();

    }

    private void initSpecialDecoItems() {
        try {

            // NAME | CUSTOM MODEL DATA | MATERIAL | DECOTAGS
                

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't register special Deco Items, Json data is not present.");
        }

    }

    private void put(String name, int customModelData, Material material, List<DecoTag> decoTags) {
        decoItems.put(name, new DecoItem(name, customModelData, material, decoTags));
    }

    private void put(String name, int customModelData, List<DecoTag> decoTags) {
        decoItems.put(name, new DecoItem(name, customModelData, Material.RABBIT_HIDE, decoTags));
    }

    public void spawnDecoStand(Location loc, Player player, DecoItem decoItem) {
        var world = loc.getWorld();
        var location = new Location(world, loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D);

        var stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setRotation(player.getLocation().getYaw()+180, 0);
        stand.setInvisible(true);
        stand.setGravity(false);

        for (var equipmentSlot : EquipmentSlot.values()) {
            for (var lock : LockType.values()) {
                stand.addEquipmentLock(equipmentSlot, lock);
            }
        }

        var decoTags = decoItem.getDecoTags();
        var equip = stand.getEquipment();
        var item = decoItem.getItemStack();

        if(decoTags.contains(DecoTag.HEAD)){
            equip.setHelmet(item);
        }else if(decoTags.contains(DecoTag.MAIN_HAND)){
            equip.setItemInMainHand(item);
        }else if(decoTags.contains(DecoTag.OFF_HAND)){
            equip.setItemInOffHand(item);
        }
        

    }

    public DecoItem getDecoItem(String name) {
        return decoItems.containsKey(name) ? decoItems.get(name) : null;
    }

    public boolean isDecoItem(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
            return false;

        var meta = item.getItemMeta();
        var name =  meta.getDisplayName();

        return decoItems.containsKey(name);
    }

    public DecoItem getDecoItem(ItemStack item) {
        var meta = item.getItemMeta();
        if (isDecoItem(item)) {
            var data = meta.getCustomModelData();
            return decoItems.values().stream().filter(deco -> deco.getCustomModelData() == data).findAny().orElse(null);
        }
        return null;
    }

    public DecoItem getDecoItem(Location loc) {
        var world = loc.getWorld();
        var location = new Location(world, loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D);
        return world.getNearbyEntities(location, 0.5, 0.5, 0.5, entity -> entity instanceof ArmorStand)
                .stream().map(stand -> (ArmorStand) stand).filter(stand -> isDecoStand(stand))
                .map(stand -> getDecoItem(stand)).findAny().orElse(null);

    }

    public ArmorStand getDecoStand(Location loc) {
        var world = loc.getWorld();
        var location = new Location(world, loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D);
        return world.getNearbyEntities(location, 0.5, 0.5, 0.5, entity -> entity instanceof ArmorStand)
                .stream().map(stand -> (ArmorStand) stand).filter(stand -> isDecoStand(stand)).findAny().orElse(null);
    }

    public DecoItem getDecoItem(ArmorStand stand) {
        var equip = stand.getEquipment();
        var item = equip.getHelmet();
        return getDecoItem(item);
    }

    public boolean isDecoStand(ArmorStand stand) {
        var equip = stand.getEquipment();
        var helmet = equip.getHelmet();
        return helmet != null && isDecoItem(helmet);
    }

    private void initAnnotations() {
        var manager = instance.getCommandManager();

        manager.getCommandCompletions().registerAsyncCompletion("bool", c -> {
            return ImmutableList.of("true", "false");
        });

        manager.getCommandCompletions().registerAsyncCompletion("decotag", c -> {
            return Arrays.stream(DecoTag.values()).map(val -> val.toString()).toList();
        });

        manager.getCommandCompletions().registerAsyncCompletion("decoitems", c -> {
            return decoItems.keySet().stream().toList();
        });

    }

    public enum DecoTag {
        SIT, UNBREAKABLE, BARRIER, OFF_HAND, MAIN_HAND, HEAD
    }

}
