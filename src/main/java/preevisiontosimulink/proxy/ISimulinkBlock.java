package preevisiontosimulink.proxy;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

public interface ISimulinkBlock extends ISimulinkParameterizable {
    String getName();
    String getPath();
    List<SimulinkPort> getInputs();
    List<SimulinkPort> getOutputs();
    void generateModel(MatlabEngine matlab);
    void initialize();
}