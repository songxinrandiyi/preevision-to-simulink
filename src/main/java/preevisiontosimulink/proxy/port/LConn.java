package preevisiontosimulink.proxy.port;

import preevisiontosimulink.proxy.block.SimulinkBlock;

public class LConn implements ISimulinkPort {
	private String name;
	private SimulinkBlock parent;

	public LConn(int index, SimulinkBlock parent) {
		this.name = "LConn" + index;
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
