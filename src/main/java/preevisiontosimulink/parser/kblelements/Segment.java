package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Segment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Segment {

	@XmlAttribute(name = "id")
	private String id;

	@XmlElement(name = "Id")
	private String segmentId;

	@XmlElement(name = "Alias_id")
	private AliasId aliasId;

	@XmlElement(name = "Virtual_length")
	private VirtualLength virtualLength;

	@XmlElement(name = "Physical_length")
	private PhysicalLength physicalLength;

	@XmlElement(name = "End_node")
	private String endNode;

	@XmlElement(name = "Start_node")
	private String startNode;

	@XmlElement(name = "Cross_section_area_information")
	private CrossSectionAreaInformation crossSectionAreaInformation;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLargeId() {
		return segmentId;
	}

	public void setLargeId(String segmentId) {
		this.segmentId = segmentId;
	}

	public AliasId getAliasId() {
		return aliasId;
	}

	public void setAliasId(AliasId aliasId) {
		this.aliasId = aliasId;
	}

	public VirtualLength getVirtualLength() {
		return virtualLength;
	}

	public void setVirtualLength(VirtualLength virtualLength) {
		this.virtualLength = virtualLength;
	}

	public PhysicalLength getPhysicalLength() {
		return physicalLength;
	}

	public void setPhysicalLength(PhysicalLength physicalLength) {
		this.physicalLength = physicalLength;
	}

	public String getEndNode() {
		return endNode;
	}

	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public String getStartNode() {
		return startNode;
	}

	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	public CrossSectionAreaInformation getCrossSectionAreaInformation() {
		return crossSectionAreaInformation;
	}

	public void setCrossSectionAreaInformation(CrossSectionAreaInformation crossSectionAreaInformation) {
		this.crossSectionAreaInformation = crossSectionAreaInformation;
	}
}
