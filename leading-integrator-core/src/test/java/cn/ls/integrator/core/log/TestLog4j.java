package cn.ls.integrator.core.log;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
public class TestLog4j {
	protected final Log log = LogFactory.getLog(getClass());

    //private final static Logger log = Logger.getLogger(TestLog4j.class);
    
    private final static String TEST_MONGO_SERVER_HOSTNAME = "localhost";
    private final static int    TEST_MONGO_SERVER_PORT     = 27017;
    private final static String TEST_DATABASE_NAME         = "log4mongotest";
    private final static String TEST_COLLECTION_NAME       = "logevents";
    
//    private final static String MONGODB_APPENDER_NAME = "MongoDB";
    
    private final Mongo           mongo;
//    private final MongoDbAppender appender;
    private DBCollection    collection;
    
    
    public TestLog4j()
        throws Exception
    {
    	this.getClass();
        mongo    = new Mongo(TEST_MONGO_SERVER_HOSTNAME, TEST_MONGO_SERVER_PORT);
//        appender = (MongoDbAppender)Logger.getRootLogger().getAppender(MONGODB_APPENDER_NAME);
    }
    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
      Mongo mongo = new Mongo(TEST_MONGO_SERVER_HOSTNAME, TEST_MONGO_SERVER_PORT);
      mongo.dropDatabase(TEST_DATABASE_NAME);
    }
    
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
      Mongo mongo = new Mongo(TEST_MONGO_SERVER_HOSTNAME, TEST_MONGO_SERVER_PORT);
      mongo.dropDatabase(TEST_DATABASE_NAME);
    }

    
    @Before
    public void setUp()
        throws Exception
    {
        // Ensure both the appender and the JUnit test use the same collection object - provides consistency across reads (JUnit) & writes (Log4J)
        collection = mongo.getDB(TEST_DATABASE_NAME).getCollection(TEST_COLLECTION_NAME);
        collection.drop();
//        appender.setCollection(collection);

        mongo.getDB(TEST_DATABASE_NAME).requestStart();
    }
    
    
    @After
    public void tearDown()
        throws Exception
    {
        mongo.getDB(TEST_DATABASE_NAME).requestDone();
    }


    @Test
    public void testSingleLogEntry()
        throws Exception
    {
        log.trace("Trace entry");
        
        assertEquals(1L, countLogEntries());
        assertEquals(1L, countLogEntriesAtLevel("trace"));
        assertEquals(0L, countLogEntriesAtLevel("debug"));
        assertEquals(0L, countLogEntriesAtLevel("info"));
        assertEquals(0L, countLogEntriesAtLevel("warn"));
        assertEquals(0L, countLogEntriesAtLevel("error"));
        assertEquals(0L, countLogEntriesAtLevel("fatal"));
        
        // verify log entry content
        DBObject entry = collection.findOne();
        assertNotNull(entry);
        assertEquals("TRACE", entry.get("level"));
        assertEquals("Trace entry", entry.get("message"));
    }
    
    
    @Test
    public void testTimestampStoredNatively()
    	throws Exception
    {
    	log.debug("Debug entry");
    	
    	assertEquals(1L, countLogEntries());
    	
    	// verify timestamp - presence and data type
        DBObject entry = collection.findOne();
        assertNotNull(entry);
        assertTrue("Timestamp is not present in logged entry", entry.containsField("timestamp"));
        assertTrue("Timestamp of logged entry is not stored as native date", (entry.get("timestamp") instanceof java.util.Date));
    }


    @Test
    public void testAllLevels()
        throws Exception
    {
        log.trace("Trace entry");
        log.debug("Debug entry");
        log.info("Info entry");
        log.warn("Warn entry");
        log.error("Error entry");
        log.fatal("Fatal entry");
        
        assertEquals(6L, countLogEntries());
        assertEquals(1L, countLogEntriesAtLevel("trace"));
        assertEquals(1L, countLogEntriesAtLevel("debug"));
        assertEquals(1L, countLogEntriesAtLevel("info"));
        assertEquals(1L, countLogEntriesAtLevel("warn"));
        assertEquals(1L, countLogEntriesAtLevel("error"));
        assertEquals(1L, countLogEntriesAtLevel("fatal"));
    }
    
    
    @Test
    public void testLogWithException()
        throws Exception
    {
        log.error("Error entry", new RuntimeException("Here is an exception!"));
            
        assertEquals(1L, countLogEntries());
        assertEquals(1L, countLogEntriesAtLevel("error"));
        
        // verify log entry content
        DBObject entry = collection.findOne();
        assertNotNull(entry);
        assertEquals("ERROR", entry.get("level"));
        assertEquals("Error entry", entry.get("message"));
        
        // verify throwable presence and content
        assertTrue("Throwable is not present in logged entry", entry.containsField("throwables"));
        BasicDBList throwables = (BasicDBList)entry.get("throwables");
        assertEquals(1, throwables.size());
        
        DBObject throwableEntry = (DBObject)throwables.get("0");
        assertTrue("Throwable message is not present in logged entry", throwableEntry.containsField("message"));
        assertEquals("Here is an exception!", throwableEntry.get("message"));
    }
    
    
    @Test
    public void testLogWithChainedExceptions()
        throws Exception
    {
        Exception rootCause = new RuntimeException("I'm the real culprit!");
        
        log.error("Error entry", new RuntimeException("I'm an innocent bystander.", rootCause));
        
        assertEquals(1L, countLogEntries());
        assertEquals(1L, countLogEntriesAtLevel("error"));
        
        // verify log entry content
        DBObject entry = collection.findOne();
        assertNotNull(entry);
        assertEquals("ERROR", entry.get("level"));
        assertEquals("Error entry", entry.get("message"));
        
        // verify throwable presence and content
        assertTrue("Throwable is not present in logged entry", entry.containsField("throwables"));
        BasicDBList throwables = (BasicDBList)entry.get("throwables");
        assertEquals(2, throwables.size());
        
        DBObject rootEntry = (DBObject)throwables.get("0");                 
        assertTrue("Throwable message is not present in logged entry", rootEntry.containsField("message"));
        assertEquals("I'm an innocent bystander.", rootEntry.get("message"));
        
        DBObject chainedEntry = (DBObject)throwables.get("1");                 
        assertTrue("Throwable message is not present in logged entry", chainedEntry.containsField("message"));
        assertEquals("I'm the real culprit!", chainedEntry.get("message"));
    }
    
    
    private long countLogEntries()
    {
        return(collection.getCount());
    }
    
    private long countLogEntriesAtLevel(final String level)
    {
        return(countLogEntriesWhere(BasicDBObjectBuilder.start().add("level", level.toUpperCase()).get()));
    }
    
    private long countLogEntriesWhere(final DBObject whereClause)
    {
    	return collection.getCount(whereClause);
    }
    
    public void test111(){
    	log.info("start++++++++++++++++++++++++");
    	 
        // Logger logger = Logger.getLogger(TestLog4j.class); 
 
     	log.error("test error");
     	log.info("##############################################################");
     	try{
     		throw new RuntimeException("ddddddddddddddddddddddddddddddd");
     	}catch (Exception ex) {
			log.error(ex.toString());
		}

     	log.info("end  ++++++++++++++++++++++++");
    }
    
    public static void main(String[] args) throws Exception { 
      TestLog4j z = new TestLog4j();
      
      z.test111();
    } 

}
