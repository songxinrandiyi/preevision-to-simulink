package preevisiontosimulink.proxy.system;

public class LSubsystemInterface implements ISubsystemInterface {
    private String name;
    private SimulinkSubsystem parent;

    public LSubsystemInterface(int index, SimulinkSubsystem parent) {
        this.name = "LConn" + index;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SimulinkSubsystem getParent() {
        return parent;
    }
}
