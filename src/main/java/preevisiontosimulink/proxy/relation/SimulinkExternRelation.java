package preevisiontosimulink.proxy.relation;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.proxy.port.ISimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class SimulinkExternRelation implements ISimulinkRelation {
	private ISimulinkPort outPort;
	private ISimulinkSystem parent;
	private String name;
	private String subsystemName;
	private String portName;
	private int direction;

	public SimulinkExternRelation(ISimulinkPort outPort, String subsystemName, String portName, ISimulinkSystem parent,
			int direction) {
		this.subsystemName = subsystemName;
		this.portName = portName;
		this.name = outPort.getParent().getName() + "_" + subsystemName + "_" + portName;
		this.outPort = outPort;
		this.parent = parent;
		this.direction = direction;
	}

	@Override
	public ISimulinkSystem getParent() {
		return parent;
	}

	@Override
	public void generateModel(MatlabEngine matlab) {
		String destinationBlockPath = subsystemName + "/" + portName;

		// Implementation for generating the Simulink relation
		String sourceBlockPath = outPort.getParent().getName() + "/" + outPort.getName();

		String parentPath = parent.getName();
		ISimulinkSystem currentParent = parent.getParent();
		while (currentParent != null) {
			parentPath = currentParent.getName() + "/" + parentPath;
			currentParent = currentParent.getParent();
		}

		if (direction == 0) {
			try {
				matlab.eval("add_line('" + parentPath + "', '" + sourceBlockPath + "', '" + destinationBlockPath
						+ "', 'autorouting', 'on')");

				System.out.println("Simulink relation generated: " + sourceBlockPath + " -> " + destinationBlockPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				matlab.eval("add_line('" + parentPath + "', '" + destinationBlockPath + "', '" + sourceBlockPath
						+ "', 'autorouting', 'on')");

				System.out.println("Simulink relation generated: " + destinationBlockPath + " -> " + sourceBlockPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}
}
