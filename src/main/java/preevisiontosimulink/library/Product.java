package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class Product extends SimulinkBlock {
	private static int num = 1;

	public Product(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "Product";
		this.BLOCK_PATH = "simulink/Math Operations/Product";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		this.inPorts.add(new SimulinkPort(1, this));
		this.inPorts.add(new SimulinkPort(2, this));
		this.outPorts.add(new SimulinkPort(1, this));
		
		this.parameters.add(new SimulinkParameter<String>("Inputs", this));
		this.parameters.add(new SimulinkParameter<String>("Orientation", this));
	}
}
