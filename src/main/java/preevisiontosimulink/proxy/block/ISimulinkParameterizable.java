package preevisiontosimulink.proxy.block;

import java.util.List;

public interface ISimulinkParameterizable {
    List<SimulinkParameter<?>> getParameters();
    <T> void addParameter(SimulinkParameter<T> parameter);
    <T> void setParameter(String name, T value);
}