package preevisiontosimulink.proxy.port;

import preevisiontosimulink.proxy.block.SimulinkBlock;

public class SimulinkPort implements ISimulinkPort {
	private String name;
	private SimulinkBlock parent;

	public SimulinkPort(int index, SimulinkBlock parent) {
		this.name = "" + index;
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
