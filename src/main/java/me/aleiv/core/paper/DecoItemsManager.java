package me.aleiv.core.paper;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import lombok.Data;
import me.aleiv.core.paper.objects.DecoItem;

@Data
public class DecoItemsManager{

    Core instance;

    public HashMap<String, DecoItem> decoItems = new HashMap<>();

    public DecoItemsManager(Core instance) {
        this.instance = instance;

    }

    public void spawnDecoStand(Location loc, Player player, DecoItem decoItem) {
        var world = loc.getWorld();
        var location = new Location(world, loc.getBlockX() + 0.5D, loc.getBlockY(), loc.getBlockZ() + 0.5D);

        var stand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setRotation(player.getLocation().getYaw()+180, 0);
        stand.setInvisible(true);
        stand.setSmall(true);
        stand.setBasePlate(true);

        var tags = decoItem.getDecoTags();

        stand.setGravity(tags.contains(DecoTag.GRAVITY));
        stand.setGlowing(tags.contains(DecoTag.GLOWING));

        if(player.getGameMode() == GameMode.CREATIVE){
            stand.setInvulnerable(tags.contains(DecoTag.UNBREAKABLE));
        }

        for (var equipmentSlot : EquipmentSlot.values()) {
            for (var lock : LockType.values()) {
                stand.addEquipmentLock(equipmentSlot, lock);
            }
        }

        var equip = stand.getEquipment();
        var item = decoItem.getItemStack();

        equip.setHelmet(item);
        

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
            return decoItems.values().stream().filter(deco -> deco.getCustomModelData() == data && item.getType() == deco.getMaterial()).findAny().orElse(null);
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

    public enum DecoTag {
        SIT, UNBREAKABLE, STAND, HAT, NONE, BARRIER, GRAVITY, GLOWING
    }

}
