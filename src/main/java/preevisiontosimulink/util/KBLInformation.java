package preevisiontosimulink.util;

public class KBLInformation {
    private Integer wireNumber = null;
    private String generalWireOccurrenceId = null;
    private String generalWireId = null;
    private Double length = null;
    private Double crossSectionArea = null;
    private String signalName = null;

    // Getters and setters
    public Integer getWireNumber() {
        return wireNumber;
    }

    public void setWireNumber(Integer wireNumber) {
        this.wireNumber = wireNumber;
    }

    public String getGeneralWireOccurrenceId() {
        return generalWireOccurrenceId;
    }

    public void setGeneralWireOccurrenceId(String generalWireOccurrenceId) {
        this.generalWireOccurrenceId = generalWireOccurrenceId;
    }

    public String getGeneralWireId() {
        return generalWireId;
    }

    public void setGeneralWireId(String generalWireId) {
        this.generalWireId = generalWireId;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getCrossSectionArea() {
        return crossSectionArea;
    }

    public void setCrossSectionArea(Double crossSectionArea) {
        this.crossSectionArea = crossSectionArea;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }
}
