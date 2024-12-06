package preevisiontosimulink.proxy.system;

import java.util.List;

import preevisiontosimulink.library.DCCurrentSource;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.library.VoltageSensor;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.connection.ISimulinkConnection;

public interface ISimulinkSystem {
	ISimulinkSystem getParent();

	SimulinkSubsystem addSubsystem(SimulinkSubsystem subsystem);

	ISimulinkBlock addBlock(ISimulinkBlock block);

	ISimulinkBlock getBlock(String name);

	ISimulinkConnection addConnection(ISimulinkConnection connection);

	List<ISimulinkBlock> getBlockList();

	List<ISimulinkConnection> getConnectionList();

	List<SimulinkSubsystem> getSubsystemList(SimulinkSubsystemType type);

	SimulinkSubsystem getSubsystem(String name);

	String getName();

	ISimulinkConnection getConnection(String name);

	List<Resistor> getAllResistorBlocks();

	List<DCCurrentSource> getAllCurrentSourceBlocks();

	List<VoltageSensor> getAllVoltageSensorBlocks();

	List<SimulinkSubsystem> getSubsystemsContainingString(String searchString);
}
