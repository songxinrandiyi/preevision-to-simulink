package preevisiontosimulink.proxy.relation;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.proxy.system.ISimulinkSystem;

public interface ISimulinkRelation {
	ISimulinkSystem getParent();

	void generateModel(MatlabEngine matlab);

	String getName();
}
