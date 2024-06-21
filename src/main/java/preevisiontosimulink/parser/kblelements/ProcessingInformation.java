package preevisiontosimulink.parser.kblelements;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Processing_information")
public class ProcessingInformation {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "Instruction_type")
    private String instructionType;

    @XmlElement(name = "Instruction_value")
    private String instructionValue;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getInstructionValue() {
        return instructionValue;
    }

    public void setInstructionValue(String instructionValue) {
        this.instructionValue = instructionValue;
    }
}
