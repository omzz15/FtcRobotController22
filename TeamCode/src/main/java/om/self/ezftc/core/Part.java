package om.self.ezftc.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * annotation to identify robot parts and allow selective part loading from settings in robot
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Part {
    String value();
}
