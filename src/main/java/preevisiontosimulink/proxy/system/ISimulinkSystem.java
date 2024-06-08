package preevisiontosimulink.proxy.system;

import java.util.List;

import preevisiontosimulink.library.DCCurrentSource;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.ISimulinkRelation;

public interface ISimulinkSystem {
	ISimulinkSystem getParent();
	SimulinkSubsystem addSubsystem(SimulinkSubsystem subsystem);
    ISimulinkBlock addBlock(ISimulinkBlock block);
    ISimulinkBlock getBlock(String name);
    ISimulinkRelation addRelation(ISimulinkRelation relation);
    List<ISimulinkBlock> getBlockList();
    List<ISimulinkRelation> getRelationList();
	List<SimulinkSubsystem> getSubsystemList();
	SimulinkSubsystem getSubsystem(String name);
    String getName();
    ISimulinkRelation getRelation(String name);
    List<Resistor> getAllResistorBlocks();
    List<DCCurrentSource> getAllCurrentSourceBlocks();
}

