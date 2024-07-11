package preevisiontosimulink.proxy.system;

import java.util.List;

import preevisiontosimulink.library.DCCurrentSource;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.library.VoltageSensor;
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

	List<SimulinkSubsystem> getSubsystemList(SimulinkSubsystemType type);

	SimulinkSubsystem getSubsystem(String name);

	String getName();

	ISimulinkRelation getRelation(String name);

	List<Resistor> getAllResistorBlocks();

	List<DCCurrentSource> getAllCurrentSourceBlocks();

	List<VoltageSensor> getAllVoltageSensorBlocks();

	List<SimulinkSubsystem> getSubsystemsContainingString(String searchString);
}
