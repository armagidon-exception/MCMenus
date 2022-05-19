package ru.armagidon.mcmenusapi.style;

@FunctionalInterface
public interface PreprocessorUnit
{
    String process(String input);
}
