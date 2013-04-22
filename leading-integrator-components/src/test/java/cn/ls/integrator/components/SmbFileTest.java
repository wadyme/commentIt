package cn.ls.integrator.components;

import org.junit.Test;

import cn.ls.integrator.components.file.AFSmbFile;

public class SmbFileTest {
	@Test
	public void test() throws Exception{
		AFSmbFile file = new AFSmbFile("smb://administrator:001@192.168.1.143/temp");
		file.getInputStream();
	}

}
