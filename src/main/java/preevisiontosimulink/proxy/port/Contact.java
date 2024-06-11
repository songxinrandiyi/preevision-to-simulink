package preevisiontosimulink.proxy.port;

public class Contact {
	private String connectorName;
	private Integer pinNumber;

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public Integer getPinNumber() {
		return pinNumber;
	}

	public void setPinNumber(Integer pinNumber) {
		this.pinNumber = pinNumber;
	}

	public Contact(String connectorName, Integer pinNumber) {
		this.connectorName = connectorName;
		this.pinNumber = pinNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Contact that = (Contact) o;

		if (!connectorName.equals(that.connectorName))
			return false;
		return pinNumber.equals(that.pinNumber);
	}

	@Override
	public int hashCode() {
		int result = connectorName.hashCode();
		result = 31 * result + pinNumber.hashCode();
		return result;
	}
	
    @Override
    public String toString() {
        return "ContactPoint{" +
                "connectorName='" + connectorName + '\'' +
                ", pinNumber=" + pinNumber +
                '}';
    }
}
