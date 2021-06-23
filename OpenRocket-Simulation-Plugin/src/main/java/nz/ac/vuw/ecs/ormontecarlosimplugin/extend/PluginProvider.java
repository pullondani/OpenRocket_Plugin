package nz.ac.vuw.ecs.ormontecarlosimplugin.extend;

import net.sf.openrocket.plugin.Plugin;
import net.sf.openrocket.simulation.extension.AbstractSimulationExtensionProvider;

@Plugin
public class PluginProvider extends AbstractSimulationExtensionProvider {
    /**
     * Sole constructor.
     */
    protected PluginProvider() {
        super(OrSimulationExtension.class, "OR Simulation Plugin");
    }
}
