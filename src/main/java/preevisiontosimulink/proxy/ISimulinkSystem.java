package preevisiontosimulink.proxy;

import java.util.List;

public interface ISimulinkSystem {
    void addBlock(ISimulinkBlock block);
    ISimulinkBlock getBlock(String name);
    void addRelation(SimulinkRelation relation);
    void generateModel();
    List<ISimulinkBlock> getBlockList();
    List<SimulinkRelation> getRelationList();
    String getModelName();
}

