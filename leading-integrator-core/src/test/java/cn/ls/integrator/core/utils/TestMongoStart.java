package cn.ls.integrator.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestMongoStart {
	
	private static final String mongoLockFile = SystemEnvUtility.getLiHomePath() 
			+ SystemEnvUtility.pathSeparator + "data" + SystemEnvUtility.pathSeparator + "mongod.lock";

	public static void main(String[] args) {
		try {
			new File(mongoLockFile).delete();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String commond = getMongoPath() + "mongod" 
				+ " --logpath \""+SystemEnvUtility.getLiHomePath()+"\\data\\log\\MongoDB.log\" " +
				" -v --quiet --dbpath \""+ SystemEnvUtility.getLiHomePath()
				+"\\data\" --directoryperdb";
		try {
			execuCommand(commond);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void execuCommand(String command) throws IOException{
		new RunThread(command).start();
	}
	
	private static String getMongoPath(){
		return SystemEnvUtility.getLiHomePath() 
			+ SystemEnvUtility.pathSeparator + "db"
			+ SystemEnvUtility.pathSeparator + "bin"
			+ SystemEnvUtility.pathSeparator;
	}
	
	static class RunThread extends Thread {
		String commond ;
		
		public RunThread( String commond) {
			this.commond = commond;
		}
		@Override
		public void run() {
			try {
				Process p = Runtime.getRuntime().exec(commond);
				BufferedInputStream in = new BufferedInputStream(p.getInputStream());   
	            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));   
	            String lineStr;   
	            while ((lineStr = inBr.readLine()) != null)   
	                //获得命令执行后在控制台的输出信息   
	                System.out.println(lineStr);// 打印输出信息   
	            //检查命令是否执行失败。   
	            if (p.waitFor() != 0) {   
	                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束   
	                    System.err.println("命令执行失败!");   
	            }   
	            inBr.close();   
	            in.close();   

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
