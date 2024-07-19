package preevisiontosimulink.proxy.relation;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class SimulinkSubToSubRelation implements ISimulinkRelation {
	private ISimulinkSystem parent;
	private String name;
	private String firstSubsystemName;
	private String firstPortName;
	private String secondSubsystemName;
	private String secondPortName;

	public SimulinkSubToSubRelation(String firstSubsystemName, String firstPortName, String secondSubsystemName,
			String secondPortName, ISimulinkSystem parent) {
		this.firstSubsystemName = firstSubsystemName;
		this.firstPortName = firstPortName;
		this.secondSubsystemName = secondSubsystemName;
		this.secondPortName = secondPortName;
		this.name = firstSubsystemName + "_" + firstPortName + "_" + secondSubsystemName + "_" + secondPortName;
		this.parent = parent;
	}

	@Override
	public ISimulinkSystem getParent() {
		return parent;
	}

	@Override
	public void generateModel(MatlabEngine matlab) {
		String destinationBlockPath = secondSubsystemName + "/" + secondPortName;

		String sourceBlockPath = firstSubsystemName + "/" + firstPortName;

		String parentPath = parent.getName();
		ISimulinkSystem currentParent = parent.getParent();
		while (currentParent != null) {
			parentPath = currentParent.getName() + "/" + parentPath;
			currentParent = currentParent.getParent();
		}

		try {
			matlab.eval("add_line('" + parentPath + "', '" + sourceBlockPath + "', '" + destinationBlockPath
					+ "', 'autorouting', 'on')");

			System.out.println("Simulink relation generated: " + sourceBlockPath + " -> " + destinationBlockPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return name;
	}
}
