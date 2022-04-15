package me.aleiv.core.paper.events;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.aleiv.core.paper.objects.DecoItem;

public class DecoItemClickEvent extends Event {
    
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({"java:S116", "java:S1170"})
    private final @Getter HandlerList Handlers = HandlerList;

    private final @Getter ArmorStand stand;
    private final @Getter DecoItem decoItem;
    private final @Getter Player player;

    public DecoItemClickEvent(ArmorStand stand, DecoItem decoItem, Player player){

        this.stand = stand;
        this.decoItem = decoItem;
        this.player = player;

    }

}