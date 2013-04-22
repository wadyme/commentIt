package cn.ls.integrator.server;

public class IntegratorAppServer {

	public static void main(String[] args) throws Exception {
		if(args.length == 0 || "start".equals(args[0])){
			IntegratorWinService.start(args);
		} else if("stop".equals(args[0])){
			IntegratorWinService.stop(args);
		}
	}
}
