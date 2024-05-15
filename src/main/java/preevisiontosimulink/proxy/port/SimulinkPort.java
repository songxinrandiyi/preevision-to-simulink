package preevisiontosimulink.proxy.port;

import preevisiontosimulink.proxy.block.SimulinkBlock;

public class SimulinkPort implements ISimulinkPort {
    private String name;
    private SimulinkBlock parent;

    public SimulinkPort(String name, SimulinkBlock parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    public SimulinkBlock getParent() {
        return parent;
    }
}
