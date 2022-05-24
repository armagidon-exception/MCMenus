package ru.armagidon.mcmenusapi.parser.tags;

import ru.armagidon.mcmenusapi.style.attributes.MenuLookType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LookAndFeelAttribute {
    MenuLookType lookType() default MenuLookType.NORMAL;
    int size() default 1;
}
