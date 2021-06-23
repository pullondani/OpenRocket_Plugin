package nz.ac.vuw.ecs.ormontecarlosimplugin;

import net.sf.openrocket.startup.OpenRocket;

/**
 * This simply opens OpenRocket using the default swing module.
 * Run this to open OpenRocket with the plugin preloaded.
 * If you want to run the plugin directly from OpenRocket (eg: by running the jar), create an artifact of this plugin
 * in your AppData folder: eg: C:/Users/~/AppData/Roaming/OpenRocket/Plugins
 */
public class OpenRocketStarter {

    /**
     * Simply runs the correct startup method from OpenRocket.
     *
     * @param args args
     */
    public static void main(String[] args) {
        OpenRocket.main(args);
    }
}