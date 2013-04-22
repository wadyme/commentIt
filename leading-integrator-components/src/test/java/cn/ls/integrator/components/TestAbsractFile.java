package cn.ls.integrator.components;

import org.junit.Test;

import cn.ls.integrator.components.file.AFSmbFile;
import cn.ls.integrator.components.file.AbstractFile;

public class TestAbsractFile {

	@Test
	public void testGetAbstractUrl() throws Exception{
		String url = "smb://administrator:001@192.168.1.143/share/aa.txt";
		AbstractFile file = AFSmbFile.fromURL(url);
//		String url = "d://ws";
//		AbstractFile file = new AFFile(url);
//		file.mkdirs();
		System.out.println(file.exists());
		System.out.println(file.getName());
		System.out.println(file.getAbsoluteURL());
		System.out.println();
	}
}
