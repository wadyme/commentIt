
package cn.ls.integrator.client.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for timerType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="timerType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="month"/>
 *     &lt;enumeration value="day"/>
 *     &lt;enumeration value="week"/>
 *     &lt;enumeration value="interval"/>
 *     &lt;enumeration value="manual"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "timerType")
@XmlEnum
public enum TimerType {

    @XmlEnumValue("month")
    MONTH("month"),
    @XmlEnumValue("day")
    DAY("day"),
    @XmlEnumValue("week")
    WEEK("week"),
    @XmlEnumValue("interval")
    INTERVAL("interval"),
    @XmlEnumValue("manual")
    MANUAL("manual");
    private final String value;

    TimerType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimerType fromValue(String v) {
        for (TimerType c: TimerType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
