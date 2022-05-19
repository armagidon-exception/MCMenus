package ru.armagidon.mcmenusapi.parser.tags;

public @interface CheckBox
{
    boolean checked() default false;
    String checkStatePath() default "";
}
