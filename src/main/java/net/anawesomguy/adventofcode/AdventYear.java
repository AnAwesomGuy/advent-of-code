package net.anawesomguy.adventofcode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface AdventYear {
    int year();

    Class<? extends Puzzle>[] puzzleClasses();

    boolean searchPackage() default false;
}

