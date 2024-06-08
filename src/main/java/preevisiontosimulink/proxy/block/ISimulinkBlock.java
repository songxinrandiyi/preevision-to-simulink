package preevisiontosimulink.proxy.block;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

import preevisiontosimulink.proxy.port.ISimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public interface ISimulinkBlock extends ISimulinkParameterizable {
    String getName();
	void setName(String name);
	List<SimulinkParameter<?>> getParameters();
	<T> void addParameter(SimulinkParameter<T> parameter);
	<T> void setParameter(String name, T value);
	ISimulinkPort addInPort(ISimulinkPort port);
	ISimulinkPort getInPort(int index);
    List<ISimulinkPort> getInPorts();
	ISimulinkPort addOutPort(ISimulinkPort port);
	ISimulinkPort getOutPort(int index);
	List<ISimulinkPort> getOutPorts();
    ISimulinkSystem getParent();
    void generateModel(MatlabEngine matlab);
    void initialize();
    String generateCombinedPath();
}