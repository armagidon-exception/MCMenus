package ru.armagidon.mcmenusapi.parser.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CheckBoxTag
{
    String checkStateTexturePath() default "";
    String checkStatePlaceholder() default "";
    String uncheckStatePlaceholder() default "";
}
