package ru.armagidon.mcmenusapi.style.attributes;

import lombok.ToString;

@ToString
public class Title implements Attribute<String>
{
    private volatile String title;

    private Title(String title) {
        this.title = title;
    }

    public static Title of(String input) {
        return new Title(input);
    }
    @Override
    public String get() {
        return title;
    }

    @Override
    public synchronized void set(String title) {
        this.title = title;
    }

    @Override
    public String getDefault() {
        return "";
    }

    @Override
    public void setDefault(String newDefault) {

    }
}
