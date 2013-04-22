package cn.ls.integrator.components.utils;


/**
 * A pluggable strategy to be able to convert objects <a
 * href="http://activemq.apache.org/camel/type-converter.html">to different
 * types</a> such as to and from String, InputStream/OutputStream,
 * Reader/Writer, Document, byte[], ByteBuffer etc
 * 
 * @version $Revision: 713950 $
 */
public interface TypeConverter {

    /**
     * Converts the value to the specified type
     * 
     * @param type the requested type
     * @param value the value to be converted
     * @return the converted value
     * @throws NoTypeConversionAvailableException if conversion not possible
     */
    <T> T convertTo(Class<T> type, Object value);
}
