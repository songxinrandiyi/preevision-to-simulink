package preevisiontosimulink.proxy.block;

public class SimulinkParameter<T> {
	private String name;
	private SimulinkBlock parent;
	private T value = null;

	public SimulinkParameter(String name, SimulinkBlock parent) {
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public SimulinkBlock getParent() {
		return parent;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}