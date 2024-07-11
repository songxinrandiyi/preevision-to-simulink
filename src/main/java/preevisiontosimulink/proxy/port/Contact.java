package preevisiontosimulink.proxy.port;

public class Contact {
	private String name;
	private Integer pinNumberTo;
	private Integer pinNumberFrom;

	public Contact(String connectorName, Integer pinNumberTo, Integer pinNumberFrom) {
		this.name = connectorName;
		this.pinNumberTo = pinNumberTo;
		this.pinNumberFrom = pinNumberFrom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPinNumberTo() {
		return pinNumberTo;
	}

	public void setPinNumberTo(Integer pinNumberTo) {
		this.pinNumberTo = pinNumberTo;
	}

	public Integer getPinNumberFrom() {
		return pinNumberFrom;
	}

	public void setPinNumberFrom(Integer pinNumberFrom) {
		this.pinNumberFrom = pinNumberFrom;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Contact that = (Contact) o;

		if (!name.equals(that.name))
			return false;
		return pinNumberTo.equals(that.pinNumberTo);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + pinNumberTo.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "ContactPoint{" + "connectorName='" + name + '\'' + ", pinNumber=" + pinNumberTo + '}';
	}
}
