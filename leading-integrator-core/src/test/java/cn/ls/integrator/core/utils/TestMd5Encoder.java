package cn.ls.integrator.core.utils;

import org.junit.Test;

public class TestMd5Encoder {

	@Test
	public void testMd5(){
		String str = "1111111fsfsefsf";
		System.out.println(MD5Encoder.getMD5(str.getBytes()));
	}
}
