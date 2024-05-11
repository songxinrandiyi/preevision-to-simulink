package preevisiontosimulink.proxy;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

public interface ISimulinkBlock extends ISimulinkParameterizable {
    String getName();
	List<SimulinkParameter<?>> getParameters();
	<T> void addParameter(SimulinkParameter<T> parameter);
	<T> void setParameter(String name, T value);
    List<SimulinkPort> getInputs();
    List<SimulinkPort> getOutputs();
    ISimulinkSystem getParent();
    void generateModel(MatlabEngine matlab);
    void initialize();
}