package nz.ac.vuw.ecs.ormontecarlosimplugin.util;

import net.sf.openrocket.rocketcomponent.Parachute;
import net.sf.openrocket.rocketcomponent.RocketComponent;

import java.util.List;

/**
 * Some helpful util methods for dealing with open rocket.
 */
public class OpenRocketUtil {
    /**
     * Recursively find parachute components and add to a list.
     *
     * @param component  component to search through
     * @param parachutes list of parachutes to add to
     */
    public static void getParachutes(RocketComponent component, List<Parachute> parachutes) {
        if (component instanceof Parachute) {
            parachutes.add((Parachute) component);
        }

        for (RocketComponent c : component.getChildren()) {
            getParachutes(c, parachutes);
        }
    }


}
