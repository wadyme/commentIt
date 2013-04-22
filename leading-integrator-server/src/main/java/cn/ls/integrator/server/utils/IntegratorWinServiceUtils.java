package cn.ls.integrator.server.utils;

import java.io.IOException;

import cn.ls.integrator.core.utils.SystemEnvUtility;

public class IntegratorWinServiceUtils {
	private static final String stopLIFile = SystemEnvUtility.getLiHomePath() + "/bin/shutdown.bat";
	public static void stopIntegratorService() throws IOException{
		Runtime.getRuntime().exec(stopLIFile);
	}
}
