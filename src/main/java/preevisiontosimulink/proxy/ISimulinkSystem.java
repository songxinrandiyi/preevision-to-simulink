package preevisiontosimulink.proxy;

import java.util.List;

public interface ISimulinkSystem {
	ISimulinkSystem getParent();
	SimulinkSubsystem addSubsystem(SimulinkSubsystem subsystem);
    ISimulinkBlock addBlock(ISimulinkBlock block);
    ISimulinkBlock getBlock(String name);
    SimulinkRelation addRelation(SimulinkRelation relation);
    List<ISimulinkBlock> getBlockList();
    List<SimulinkRelation> getRelationList();
	List<SimulinkSubsystem> getSubsystemList();
    String getName();
}

