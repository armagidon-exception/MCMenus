package ru.armagidon.mcmenusapi.utils.parser.o2m.tags;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LinkTag
{
    String id();
    String texture();
}
