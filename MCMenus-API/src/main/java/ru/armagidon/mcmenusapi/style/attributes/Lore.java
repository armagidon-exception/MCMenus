package ru.armagidon.mcmenusapi.style.attributes;

import com.google.common.collect.ImmutableList;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@ToString
public class Lore implements Attribute<List<String>>
{
    private List<String> lore;

    private Lore(List<String> lore) {
        this.lore = lore;
    }

    public List<String> getLore() {
        return lore;
    }

    public static Lore of(String... lore) {
        return new Lore(Arrays.asList(lore));
    }

    @Override
    public List<String> get() {
        return ImmutableList.copyOf(lore);
    }

    @Override
    public synchronized void set(List<String> newLore) {
        this.lore = newLore;
    }
}
