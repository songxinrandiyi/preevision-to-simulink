package preevisiontosimulink.proxy.port;

import preevisiontosimulink.proxy.block.SimulinkBlock;

public class RConnectionPort implements ISimulinkPort {
    private String name;
    private SimulinkBlock parent;

    public RConnectionPort(int index, SimulinkBlock parent) {
        this.name = "RConn" + index;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SimulinkBlock getParent() {
        return parent;
    }
}
