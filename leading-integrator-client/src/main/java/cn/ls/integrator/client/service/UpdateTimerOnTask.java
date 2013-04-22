
package cn.ls.integrator.client.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateTimerOnTask complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateTimerOnTask">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="scheduleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taskName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oldTimerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timer" type="{http://service.server.integrator.ls.cn/}timer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateTimerOnTask", propOrder = {
    "scheduleName",
    "taskName",
    "oldTimerName",
    "timer"
})
public class UpdateTimerOnTask {

    protected String scheduleName;
    protected String taskName;
    protected String oldTimerName;
    protected Timer timer;

    /**
     * Gets the value of the scheduleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduleName() {
        return scheduleName;
    }

    /**
     * Sets the value of the scheduleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduleName(String value) {
        this.scheduleName = value;
    }

    /**
     * Gets the value of the taskName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the value of the taskName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskName(String value) {
        this.taskName = value;
    }

    /**
     * Gets the value of the oldTimerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldTimerName() {
        return oldTimerName;
    }

    /**
     * Sets the value of the oldTimerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldTimerName(String value) {
        this.oldTimerName = value;
    }

    /**
     * Gets the value of the timer property.
     * 
     * @return
     *     possible object is
     *     {@link Timer }
     *     
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * Sets the value of the timer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Timer }
     *     
     */
    public void setTimer(Timer value) {
        this.timer = value;
    }

}
