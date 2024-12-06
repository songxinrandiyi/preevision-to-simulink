package preevisiontosimulink.proxy.connection;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.proxy.system.ISimulinkSystem;

public interface ISimulinkConnection {
	ISimulinkSystem getParent();

	void generateModel(MatlabEngine matlab);

	String getName();
}
