package me.aleiv.core.paper.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import fr.mrmicky.fastinv.ItemBuilder;
import lombok.Data;
import me.aleiv.core.paper.DecoLunchManager.DecoTag;
import net.md_5.bungee.api.ChatColor;

@Data
public class DecoItem {

    String name;
    int customModelData;
    Material material;
    List<DecoTag> decoTags = new ArrayList<>();

    public DecoItem(String name, int customModelData, Material material, List<DecoTag> decoTags) {
        this.name = name;
        this.customModelData = customModelData;
        this.material = material;
        this.decoTags = decoTags;
    }

    public ItemStack getItemStack() {
        List<String> lines = new ArrayList<>();

        return new ItemBuilder(material).meta(meta -> meta.setCustomModelData(customModelData))
                .name(name).addLore(lines).flags(ItemFlag.HIDE_ATTRIBUTES).build();
    }

    public String formatName(String string) {
        var str = string.toLowerCase();
        var array = str.toCharArray();

        var upper = true;
        var count = 0;
        for (char c : array) {
            if (upper) {
                array[count] = Character.toUpperCase(c);
                upper = false;
            } else if (c == '_') {
                upper = true;
            }
            count++;
        }

        var newString = String.valueOf(array);
        return newString.replace("_", " ");
    }

}
