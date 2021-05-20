package ru.armagidon.mcmenusapi.utils.parser.o2m.tags;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CheckBoxTag
{
    String id();
    String texture();
    boolean checked() default false;
}
