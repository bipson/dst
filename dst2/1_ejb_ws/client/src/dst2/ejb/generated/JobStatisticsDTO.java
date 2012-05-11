
package dst2.ejb.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jobStatisticsDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jobStatisticsDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="execDTO" type="{http://webservice.dst2/}executionDTO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="gridname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jobStatisticsDTO", propOrder = {
    "execDTO",
    "gridname"
})
public class JobStatisticsDTO {

    @XmlElement(nillable = true)
    protected List<ExecutionDTO> execDTO;
    protected String gridname;

    /**
     * Gets the value of the execDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the execDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExecDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExecutionDTO }
     * 
     * 
     */
    public List<ExecutionDTO> getExecDTO() {
        if (execDTO == null) {
            execDTO = new ArrayList<ExecutionDTO>();
        }
        return this.execDTO;
    }

    /**
     * Gets the value of the gridname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGridname() {
        return gridname;
    }

    /**
     * Sets the value of the gridname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGridname(String value) {
        this.gridname = value;
    }

}
