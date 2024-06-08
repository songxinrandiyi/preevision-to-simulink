package preevisiontosimulink.proxy.port;

public class ContactPoint {
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
	
	public ContactPoint(String connectorName, Integer pinNumber) {
		this.connectorName = connectorName;
		this.pinNumber = pinNumber;
	}
}
