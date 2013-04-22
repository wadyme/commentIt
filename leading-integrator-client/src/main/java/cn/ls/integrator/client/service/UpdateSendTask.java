
package cn.ls.integrator.client.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateSendTask complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateSendTask">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="scheduleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oldName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="task" type="{http://service.server.integrator.ls.cn/}task" minOccurs="0"/>
 *         &lt;element name="taskContext" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateSendTask", propOrder = {
    "scheduleName",
    "oldName",
    "task",
    "taskContext"
})
public class UpdateSendTask {

    protected String scheduleName;
    protected String oldName;
    protected Task task;
    protected String taskContext;

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
     * Gets the value of the oldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldName() {
        return oldName;
    }

    /**
     * Sets the value of the oldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldName(String value) {
        this.oldName = value;
    }

    /**
     * Gets the value of the task property.
     * 
     * @return
     *     possible object is
     *     {@link Task }
     *     
     */
    public Task getTask() {
        return task;
    }

    /**
     * Sets the value of the task property.
     * 
     * @param value
     *     allowed object is
     *     {@link Task }
     *     
     */
    public void setTask(Task value) {
        this.task = value;
    }

    /**
     * Gets the value of the taskContext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskContext() {
        return taskContext;
    }

    /**
     * Sets the value of the taskContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskContext(String value) {
        this.taskContext = value;
    }

}
