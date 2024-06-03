package preevisiontosimulink.proxy.relation;

import preevisiontosimulink.proxy.port.ISimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

import com.mathworks.engine.MatlabEngine;

public interface ISimulinkRelation {
    ISimulinkSystem getParent();
    void generateModel(MatlabEngine matlab);
    String getName();
}

