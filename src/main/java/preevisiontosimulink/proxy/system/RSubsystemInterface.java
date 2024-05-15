package preevisiontosimulink.proxy.system;

public class RSubsystemInterface implements ISubsystemInterface {
    private String name;
    private SimulinkSubsystem parent;

    public RSubsystemInterface(int index, SimulinkSubsystem parent) {
        this.name = "RConn" + index;
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
