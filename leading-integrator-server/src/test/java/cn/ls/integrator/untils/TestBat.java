package cn.ls.integrator.untils;

import java.io.IOException;

import org.junit.Test;

import cn.ls.integrator.core.log.mongo.MongoServiceUtils;


public class TestBat {
	@Test
	public void exec() throws IOException{
		MongoServiceUtils.startMongoDB();
	}
	@Test
	public void stopTest() throws IOException{
		MongoServiceUtils.stopMongoDB();
	}

}
