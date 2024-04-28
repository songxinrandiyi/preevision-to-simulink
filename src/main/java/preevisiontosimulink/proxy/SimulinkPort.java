package preevisiontosimulink.proxy;

public class SimulinkPort implements ISimulinkPort {
    private int index;
    private ISimulinkBlock parent;

    public SimulinkPort(int index, ISimulinkBlock parent) {
        this.index = index;
        this.parent = parent;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public ISimulinkBlock getParent() {
        return parent;
    }
}
