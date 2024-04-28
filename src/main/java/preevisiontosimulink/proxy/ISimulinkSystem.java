package preevisiontosimulink.proxy;

import java.util.List;

public interface ISimulinkSystem {
    void addBlock(ISimulinkBlock block);
    void addRelation(SimulinkRelation relation);
    <T> void addParameter(SimulinkParameter<T> parameter);
    void generateModel();
    List<ISimulinkBlock> getBlockList();
    List<SimulinkRelation> getRelationList();
    String getModelName();
}

