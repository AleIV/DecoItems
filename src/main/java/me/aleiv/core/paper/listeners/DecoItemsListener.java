package me.aleiv.core.paper.listeners;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.DecoItemsManager.DecoTag;

public class DecoItemsListener implements Listener {

    Core instance;

    public DecoItemsListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        var entity = e.getEntity();
        if (entity instanceof ArmorStand stand) {

            var equip = stand.getEquipment();
            var helmet = equip.getHelmet();
            var manager = instance.getDecoItemsManager();

            if (helmet != null && manager.isDecoItem(helmet)) {
                var decoItem = manager.getDecoItem(helmet);

                var drops = e.getDrops();
                drops.add(decoItem.getItemStack());

            }
        }
    }

    // DECOLUNCH

    @EventHandler
    public void onPlace(PlayerInteractAtEntityEvent e) {
        var entity = e.getRightClicked();
        if (entity instanceof ArmorStand stand) {
            var player = e.getPlayer();
            var equip = stand.getEquipment();
            var helmet = equip.getHelmet();
            var manager = instance.getDecoItemsManager();

            if (helmet != null && manager.isDecoItem(helmet)) {
                var decoItem = manager.getDecoItem(helmet);
                var decoTags = decoItem.getDecoTags();

                if (decoTags.contains(DecoTag.SIT) && stand.getPassengers().isEmpty()) {

                    stand.addPassenger(player);
                }

            }

        }
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        var action = e.getAction();
        var hand = e.getHand();
        var player = e.getPlayer();
        var equipment = player.getEquipment();
        if(hand == null) return;

        var item = equipment.getItem(hand);
        var block = e.getClickedBlock();

        if (item == null)
            return;

        var manager = instance.getDecoItemsManager();

        if (manager.isDecoItem(item)) {
            var decoItem = manager.getDecoItem(item);
            var tags = decoItem.getDecoTags();

            if (tags.contains(DecoTag.HAT)
                    && (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR)) {

                if (equipment.getHelmet() != null) {
                    var helmet = equipment.getHelmet();
                    equipment.setItem(hand, helmet);
                    equipment.setHelmet(item);
                } else {
                    equipment.setHelmet(item);
                    equipment.setItem(hand, null);
                }

            } else if (tags.contains(DecoTag.STAND) && action == Action.RIGHT_CLICK_BLOCK) {
                var loc = block.getLocation();
                block = block.getRelative(e.getBlockFace());
                loc = block.getLocation();
                manager.spawnDecoStand(loc, player, decoItem);
                
                if (item.getAmount() == 1) {
                    equipment.setItem(hand, null);
                } else {
                    item.setAmount(item.getAmount() - 1);
                    equipment.setItem(hand, item);
                }

                if (tags.contains(DecoTag.BARRIER)) {
                    loc.getBlock().setType(Material.BARRIER);
                }

            }

        }

        if (block != null && block.getType() == Material.BARRIER) {
            var loc = block.getLocation();
            var decoItemStand = manager.getDecoStand(loc);

            if (decoItemStand != null) {
                // remove decoitem from block
                var decoItem = manager.getDecoItem(decoItemStand);
                var tags = decoItem.getDecoTags();

                if (action == Action.LEFT_CLICK_BLOCK) {

                    if (!tags.contains(DecoTag.UNBREAKABLE)) {
                        block.setType(Material.AIR);
                        decoItemStand.remove();

                        loc.getWorld().dropItemNaturally(loc, decoItem.getItemStack());
                    }

                } else if (action == Action.RIGHT_CLICK_BLOCK) {

                    if (tags.contains(DecoTag.SIT) && decoItemStand.getPassengers().isEmpty()) {

                        decoItemStand.addPassenger(player);
                    }

                }

            } /*else {
                block.setType(Material.AIR);
                // remove barrier that doesnt have deco stand
            }*/

        }

    }

}
