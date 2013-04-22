
package cn.ls.integrator.client.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for addSendSchedule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addSendSchedule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="scheduleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scheduleTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scheduleDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tasks" type="{http://service.server.integrator.ls.cn/}task" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="timers" type="{http://service.server.integrator.ls.cn/}timer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="taskContexts" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="associates" type="{http://service.server.integrator.ls.cn/}associate" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addSendSchedule", propOrder = {
    "scheduleName",
    "scheduleTitle",
    "scheduleDescription",
    "tasks",
    "timers",
    "taskContexts",
    "associates"
})
public class AddSendSchedule {

    protected String scheduleName;
    protected String scheduleTitle;
    protected String scheduleDescription;
    protected List<Task> tasks;
    protected List<Timer> timers;
    protected List<String> taskContexts;
    protected List<Associate> associates;

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
     * Gets the value of the scheduleTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduleTitle() {
        return scheduleTitle;
    }

    /**
     * Sets the value of the scheduleTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduleTitle(String value) {
        this.scheduleTitle = value;
    }

    /**
     * Gets the value of the scheduleDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduleDescription() {
        return scheduleDescription;
    }

    /**
     * Sets the value of the scheduleDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduleDescription(String value) {
        this.scheduleDescription = value;
    }

    /**
     * Gets the value of the tasks property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tasks property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTasks().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Task }
     * 
     * 
     */
    public List<Task> getTasks() {
        if (tasks == null) {
            tasks = new ArrayList<Task>();
        }
        return this.tasks;
    }

    /**
     * Gets the value of the timers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Timer }
     * 
     * 
     */
    public List<Timer> getTimers() {
        if (timers == null) {
            timers = new ArrayList<Timer>();
        }
        return this.timers;
    }

    /**
     * Gets the value of the taskContexts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taskContexts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaskContexts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTaskContexts() {
        if (taskContexts == null) {
            taskContexts = new ArrayList<String>();
        }
        return this.taskContexts;
    }

    /**
     * Gets the value of the associates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the associates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssociates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Associate }
     * 
     * 
     */
    public List<Associate> getAssociates() {
        if (associates == null) {
            associates = new ArrayList<Associate>();
        }
        return this.associates;
    }

}
