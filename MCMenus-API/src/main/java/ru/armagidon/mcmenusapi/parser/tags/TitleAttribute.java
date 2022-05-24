package ru.armagidon.mcmenusapi.parser.tags;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//Specifies where to get title for this item or menu panel
public @interface TitleAttribute
{
    String title();
    boolean isPath() default true;
}
