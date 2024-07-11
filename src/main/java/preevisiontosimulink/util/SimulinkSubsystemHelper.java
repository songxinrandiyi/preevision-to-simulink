package preevisiontosimulink.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.library.InPort;
import preevisiontosimulink.library.LConnection;
import preevisiontosimulink.library.OutPort;
import preevisiontosimulink.library.RConnection;
import preevisiontosimulink.proxy.system.ISimulinkSystem;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSubsystemType;

public class SimulinkSubsystemHelper {

	public static void arrangeSteckerType(MatlabEngine matlab, List<LConnection> inConnections, String combinedPath,
			String name) {
		try {
			if (inConnections != null && inConnections.size() > 0) {
				matlab.eval(
						"h = getSimulinkBlockHandle('" + combinedPath + "/" + inConnections.get(0).getName() + "')");
				matlab.eval("pos = get_param(h,'Position')");
				matlab.eval("pos1 = [pos(1)+100 pos(2)-15 pos(3)+100 pos(4)]");
				matlab.eval("pos2 = [pos1(1)+70 pos1(2)-70 pos1(3)+70 pos1(4)-70]");
				matlab.eval("pos3 = [pos2(1)+100 pos2(2)-10 pos2(3)+120 pos2(4)+10]");
				matlab.eval(
						"set_param('" + combinedPath + "/" + inConnections.get(0).getName() + "_I', 'Position', pos1)");
				matlab.eval("set_param('" + combinedPath + "/" + inConnections.get(0).getName()
						+ "_PS', 'Position', pos2)");
				matlab.eval("set_param('" + combinedPath + "/" + inConnections.get(0).getName()
						+ "_Display', 'Position', pos3)");

				for (int i = 1; i < inConnections.size(); i++) {
					LConnection port = inConnections.get(i);
					matlab.eval("pos = [pos(1) pos(2)+200 pos(3) pos(4)+200]");
					matlab.eval("pos1 = [pos(1)+100 pos(2)-15 pos(3)+100 pos(4)]");
					matlab.eval("pos2 = [pos1(1)+70 pos1(2)-70 pos1(3)+70 pos1(4)-70]");
					matlab.eval("pos3 = [pos2(1)+100 pos2(2)-10 pos2(3)+120 pos2(4)+10]");
					matlab.eval("set_param('" + combinedPath + "/" + port.getName() + "', 'Position', pos)");
					matlab.eval("set_param('" + combinedPath + "/" + port.getName() + "_I', 'Position', pos1)");
					matlab.eval("set_param('" + combinedPath + "/" + port.getName() + "_PS', 'Position', pos2)");
					matlab.eval("set_param('" + combinedPath + "/" + port.getName() + "_Display', 'Position', pos3)");
				}

				matlab.eval("pos = [pos(1)+700 pos(2)+100 pos(3)+700 pos(4)+100]");
				matlab.eval("set_param('" + combinedPath + "/" + name + "_E', 'Position', pos)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void arrangeKabelType(MatlabEngine matlab, List<LConnection> inConnections,
			List<RConnection> outConnections, String combinedPath) {
		try {
			if (inConnections != null && inConnections.size() > 0 && outConnections != null
					&& outConnections.size() > 0) {
				LConnection inPort = inConnections.get(0);
				RConnection outPort = outConnections.get(0);

				matlab.eval("h = getSimulinkBlockHandle('" + combinedPath + "/" + inPort.getName() + "')");
				matlab.eval("pos = get_param(h,'Position')");
				matlab.eval("pos1 = [pos(1)+700 pos(2) pos(3)+700 pos(4)]");
				matlab.eval("pos2 = [pos(1)+100 pos(2)-12 pos(3)+115 pos(4)+12]");
				matlab.eval("pos3 = [pos(1)+200 pos(2) pos(3)+200 pos(4)]");
				matlab.eval("pos4 = [pos3(1) pos3(2)-100 pos3(3) pos3(4)-80]");
				matlab.eval("pos5 = [pos4(1)+100 pos4(2)+10 pos4(3)+100 pos4(4)-10]");
				matlab.eval("pos6 = [pos5(1)+100 pos5(2)-10 pos5(3)+120 pos5(4)+10]");

				matlab.eval("set_param('" + combinedPath + "/" + outPort.getName() + "', 'Position', pos1)");
				// matlab.eval("set_param('" + combinedPath + "/" + "I', 'Position', pos2)");
				matlab.eval("set_param('" + combinedPath + "/" + "R', 'Position', pos3)");
				matlab.eval("set_param('" + combinedPath + "/" + "U', 'Position', pos4)");
				matlab.eval("set_param('" + combinedPath + "/" + "PS', 'Position', pos5)");
				matlab.eval("set_param('" + combinedPath + "/" + "Display', 'Position', pos6)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void reorderConnectionsForExcel(SimulinkSubsystem subsystem) {
		// Reorder inConnections
		Collections.sort(subsystem.getInConnections(), new Comparator<LConnection>() {
			@Override
			public int compare(LConnection c1, LConnection c2) {
				return StringUtils.extractNumber(c1.getName(), 0) - StringUtils.extractNumber(c2.getName(), 0);
			}
		});

		// Reorder outConnections
		Collections.sort(subsystem.getOutConnections(), new Comparator<RConnection>() {
			@Override
			public int compare(RConnection c1, RConnection c2) {
				return StringUtils.extractNumber(c1.getName(), 0) - StringUtils.extractNumber(c2.getName(), 0);
			}
		});
	}

	public static void reorderConnectionsRecursivelyForExcel(SimulinkSubsystem subsystem) {
		// Reorder connections for the current subsystem
		reorderConnectionsForExcel(subsystem);

		// Reorder connections for each child subsystem
		for (SimulinkSubsystem childSubsystem : subsystem.getSubsystemList(SimulinkSubsystemType.KABEL)) {
			reorderConnectionsRecursivelyForExcel(childSubsystem);
		}
	}

	public static void reorderConnections(SimulinkSubsystem subsystem) {
		// Reorder inConnections
		Collections.sort(subsystem.getInConnections(), new Comparator<LConnection>() {
			@Override
			public int compare(LConnection c1, LConnection c2) {
				return StringUtils.convertStringToInt(c1.getName()) - StringUtils.convertStringToInt(c2.getName());
			}
		});

		// Reorder outConnections
		Collections.sort(subsystem.getOutConnections(), new Comparator<RConnection>() {
			@Override
			public int compare(RConnection c1, RConnection c2) {
				return StringUtils.convertStringToInt(c1.getName()) - StringUtils.convertStringToInt(c2.getName());
			}
		});
	}

	public static void reorderConnectionsRecursively(SimulinkSubsystem subsystem) {
		// Reorder connections for the current subsystem
		reorderConnections(subsystem);

		// Reorder connections for each child subsystem
		for (SimulinkSubsystem childSubsystem : subsystem.getSubsystemList(SimulinkSubsystemType.STECKER)) {
			reorderConnectionsRecursively(childSubsystem);
		}
	}
	
	public static void reorderPorts(SimulinkSubsystem subsystem) {
		// Reorder inConnections
		Collections.sort(subsystem.getInPorts(), new Comparator<InPort>() {
			@Override
			public int compare(InPort c1, InPort c2) {
				return StringUtils.convertStringToInt(c1.getName()) - StringUtils.convertStringToInt(c2.getName());
			}
		});

		// Reorder outConnections
		Collections.sort(subsystem.getOutPorts(), new Comparator<OutPort>() {
			@Override
			public int compare(OutPort c1, OutPort c2) {
				return StringUtils.convertStringToInt(c1.getName()) - StringUtils.convertStringToInt(c2.getName());
			}
		});
	}

	public static void reorderPortsRecursively(SimulinkSubsystem subsystem) {
		// Reorder connections for the current subsystem
		reorderPorts(subsystem);

		// Reorder connections for each child subsystem
		for (SimulinkSubsystem childSubsystem : subsystem.getSubsystemList(SimulinkSubsystemType.STECKER)) {
			reorderPortsRecursively(childSubsystem);
		}
	}
	
	public static String generateCombinedPath(ISimulinkSystem parent, String name) {
		StringBuilder pathBuilder = new StringBuilder(name);
		ISimulinkSystem currentParent = parent;
		while (currentParent != null) {
			pathBuilder.insert(0, currentParent.getName() + "/");
			currentParent = currentParent.getParent();
		}
		return pathBuilder.toString();
	}
}
