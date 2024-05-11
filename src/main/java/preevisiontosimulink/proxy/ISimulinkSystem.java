package preevisiontosimulink.proxy;

import java.util.List;

public interface ISimulinkSystem {
    ISimulinkBlock addBlock(ISimulinkBlock block);
    ISimulinkBlock getBlock(String name);
    SimulinkRelation addRelation(SimulinkRelation relation);
    void generateModel();
    List<ISimulinkBlock> getBlockList();
    List<SimulinkRelation> getRelationList();
    String getModelName();
}

