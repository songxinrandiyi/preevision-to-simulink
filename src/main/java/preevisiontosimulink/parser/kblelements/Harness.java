package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Harness")
@XmlAccessorType(XmlAccessType.FIELD)
public class Harness {
	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Part_number")
	private String partNumber;

	@XmlElement(name = "Company_name")
	private String companyName;

	@XmlElement(name = "Version")
	private String version;

	@XmlElement(name = "Abbreviation")
	private String abbreviation;

	@XmlElement(name = "Description")
	private String description;

	@XmlElement(name = "Car_classification_level_2")
	private String carClassificationLevel2;

	@XmlElement(name = "Model_year")
	private String modelYear;

	@XmlElement(name = "Content")
	private String content;

	@XmlElement(name = "Connection")
	private List<Connection> connections;

	@XmlElement(name = "Connector_occurrence")
	private List<ConnectorOccurrence> connectorOccurrences;

	@XmlElement(name = "General_wire_occurrence")
	private List<GeneralWireOccurrence> generalWireOccurrences;

	@XmlElement(name = "Terminal_occurrence")
	private List<TerminalOccurrence> terminalOccurrences;

	@XmlElement(name = "Module")
	private List<Module> modules;

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	// Getter und Setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCarClassificationLevel2() {
		return carClassificationLevel2;
	}

	public void setCarClassificationLevel2(String carClassificationLevel2) {
		this.carClassificationLevel2 = carClassificationLevel2;
	}

	public String getModelYear() {
		return modelYear;
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Connection> getConnections() {
		return connections;
	}

	public void setConnections(List<Connection> connections) {
		this.connections = connections;
	}

	public List<ConnectorOccurrence> getConnectorOccurrences() {
		return connectorOccurrences;
	}

	public void setConnectorOccurrences(List<ConnectorOccurrence> connectorOccurrences) {
		this.connectorOccurrences = connectorOccurrences;
	}

	public List<GeneralWireOccurrence> getGeneralWireOccurrences() {
		return generalWireOccurrences;
	}

	public void setGeneralWireOccurrences(List<GeneralWireOccurrence> generalWireOccurrences) {
		this.generalWireOccurrences = generalWireOccurrences;
	}

	public List<TerminalOccurrence> getTerminalOccurrences() {
		return terminalOccurrences;
	}

	public void setTerminalOccurrences(List<TerminalOccurrence> terminalOccurrences) {
		this.terminalOccurrences = terminalOccurrences;
	}
}
