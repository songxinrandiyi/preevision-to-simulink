package preevisiontosimulink.proxy;

public class SimulinkPort implements ISimulinkPort {
    private int index;
    private SimulinkBlock parent;

    public SimulinkPort(int index, SimulinkBlock parent) {
        this.index = index;
        this.parent = parent;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public SimulinkBlock getParent() {
        return parent;
    }
}
