package net.anawesomguy.adventofcode;

import org.jetbrains.annotations.Range;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdventDay {
    @Range(from = 1, to = 25)
    int day();
}
