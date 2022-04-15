package me.aleiv.core.paper.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.aleiv.core.paper.objects.DecoItem;

public class DecoItemPlaceEvent extends Event {
    
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({"java:S116", "java:S1170"})
    private final @Getter HandlerList Handlers = HandlerList;

    private final @Getter EquipmentSlot hand;
    private final @Getter ItemStack item;
    private final @Getter Block block;
    private final @Getter DecoItem decoItem;
    private final @Getter Player player;
    private final @Getter boolean canceled;

    public DecoItemPlaceEvent(EquipmentSlot hand, ItemStack item, Block block, DecoItem decoItem, Player player, boolean canceled){

        this.hand = hand;
        this.item = item;
        this.block = block;
        this.decoItem = decoItem;
        this.player = player;
        this.canceled = canceled;

    }

}