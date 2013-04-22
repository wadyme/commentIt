package cn.ls.integrator.server.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


import cn.ls.integrator.core.utils.SystemEnvUtility;

public class Log4jZipServlet extends HttpServlet {

	private static final long serialVersionUID = 7458036724151020284L;
	protected static final Log logger = LogFactory.getLog(Log4jZipServlet.class);
	public final String DEFAULT_LOG_NAME="LI_LOG.log";
	public static String pathSeparator = System.getProperty("file.separator");
	public final int limitDays= 100;
	
	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			String currentDate = getCurrentDate();
			
			String startDate = request.getParameter("startDate")==null?currentDate:request.getParameter("startDate");
			String endDate = request.getParameter("endDate")==null?startDate:request.getParameter("endDate");
			String zipName = UUID.randomUUID().toString() + ".ZIP";
			String path = SystemEnvUtility.getConfigLogPath();
			String fileName =  path + "\\" + DEFAULT_LOG_NAME;
			String downFileName = "系统日志文件";
			//startDate = "2011-04-20";
			//endDate = "2011-06-22";
			int diffTwoDate = diffTwoDate(endDate, startDate);
			if(diffTwoDate>limitDays||diffTwoDate<0){
				return;
			}
			/**
			 * 这个集合就是你想要打包的所有文件， 这里假设已经准备好了所要打包的文件
			 */
			List<File> files = new ArrayList<File>();
			if(diffTwoDate==0){
				String newfileName = "";
				downFileName =  downFileName+"("+startDate+")";
				if(!startDate.equalsIgnoreCase(currentDate)){
					newfileName = fileName + "." +startDate;
				}else{
					newfileName = fileName;
				}
				File newFile = new File(newfileName);	
				if(newFile.exists()){
					files.add(newFile);
				}
			}else{
				downFileName = downFileName+"("+startDate+"~"+endDate+")";
				for(int d=0;d<diffTwoDate;d++ ){
					
					String newdate = addDate(startDate, d);
					String newfileName = "";
					if(!newdate.equalsIgnoreCase(currentDate)){
						newfileName = fileName + "." +newdate;
					}else{
						newfileName = fileName;
					}
					File newFile = new File(newfileName);	
					if(newFile.exists()){
						files.add(newFile);
					}
					
				}
			}
			downFileName = new String(downFileName.getBytes(), "iso-8859-1");
			
			/*File directory = new File(path);
			if (directory.isDirectory()) {
				File[] fl = directory.listFiles();
				for (File f : fl) {
					files.add(f);
				}
			}
			*/
			File file = new File(zipName);
			if (!file.exists()) {
				file.createNewFile();
			}
			//默认的下载文件名称
			//要是弹出那个下载的框
			response.addHeader("Content-Disposition", "attachment; filename="+downFileName+".zip");
			response.setContentType("application/octet-stream");
			// 创建文件输出流
			FileOutputStream fous = new FileOutputStream(file);
			ZipOutputStream zipOut = new ZipOutputStream(fous);
			zipFile(files, zipOut);
			zipOut.close();
			fous.close();
			OutputStream out = response.getOutputStream();
			BufferedInputStream bin = new BufferedInputStream(
					new FileInputStream(file.getPath()));
			byte[] buf = new byte[bin.available()];
			int len = 0;
			while ((len = bin.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			bin.close();
			/** 最后的操作是把创建的临时文件删除 */
			try {
				File f = new File(file.getPath());
				f.delete();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		} catch (Exception e) {
			logger.error("系统异常:日志下载失败，["+e.toString()+"]");
		}

	}


	/**
	 * 把接受的全部文件打成压缩包
	 * 
	 * @param List
	 *            <File>;
	 * @param org
	 *            .apache.tools.zip.ZipOutputStream
	 */
	public void zipFile(List<File> files, ZipOutputStream outputStream) {
		int size = files.size();
		for (int i = 0; i < size; i++) {
			File file =  files.get(i);
			zipFile(file, outputStream);
		}
	}

	/**
	 * 根据输入的文件与输出流对文件进行打包
	 * 
	 * @param File
	 * @param org
	 *            .apache.tools.zip.ZipOutputStream
	 */
	public void zipFile(File inputFile, ZipOutputStream ouputStream) {
		try {
			if (inputFile.exists()) {
				/**
				 * 如果是目录的话这里是不采取操作的， 至于目录的打包正在研究中
				 */
				if (inputFile.isFile()) {
					FileInputStream in = new FileInputStream(inputFile);
					BufferedInputStream bins = new BufferedInputStream(in, 512);
					// org.apache.tools.zip.ZipEntry
					ZipEntry entry = new ZipEntry(inputFile.getName());
					ouputStream.putNextEntry(entry);
					// 向压缩文件中输出数据
					int nNumber;
					byte[] buffer = new byte[512];
					while ((nNumber = bins.read(buffer)) != -1) {
						ouputStream.write(buffer, 0, nNumber);
					}
					// 关闭创建的流对象
					bins.close();
					in.close();
				} else {
					try {
						File[] files = inputFile.listFiles();
						for (int i = 0; i < files.length; i++) {
							zipFile(files[i], ouputStream);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取当前系统日期(非数据库日期,格式为yyyy-mm-dd)
	 * @return String
	 */
	private static String getCurrentDate()
	{
		GregorianCalendar lgc = new GregorianCalendar();
		String year = String.valueOf(lgc.get(Calendar.YEAR));
		String month = String.valueOf(lgc.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
		{
			month = "0" + month;
		}
		String date = String.valueOf(lgc.get(Calendar.DATE));
		if (date.length() == 1)
		{
			date = "0" + date;
		}
		return year + "-" + month + "-" + date;
	}
	
	/**
	 * 求两个日期之间的天数差,当参数错误时返回-1
	 * @param firstDate 第一个日期(格式yyyy-mm-dd)
	 * @param secDate   第二个日期(格式yyyy-mm-dd)
	 * @return 两日期之间的天数差
	 */
	private static int diffTwoDate(String firstDate, String secDate){
		long diffTime = -1L;
		try{
			Calendar lastCal = Calendar.getInstance();
			Calendar firstCal = Calendar.getInstance();
	
			int firstMonth = Integer.parseInt(firstDate.substring(5, 7));
			int lastMonth = Integer.parseInt(secDate.substring(5, 7));
	
			lastCal.set(Integer.parseInt(secDate.substring(0, 4)), lastMonth - 1, Integer.parseInt(secDate.substring(8, 10)));
			firstCal.set(Integer.parseInt(firstDate.substring(0, 4)), firstMonth - 1, Integer.parseInt(firstDate.substring(8, 10)));
			Date first = firstCal.getTime();
			Date last = lastCal.getTime();
			long lastTime = last.getTime();
			long firstTime = first.getTime();
			long chaTime = firstTime - lastTime;
			diffTime = chaTime / 1000 / 60 / 60 / 24;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return (int) diffTime;
	}
	
	/**
	 * 日期的追加处理
	 * @param strOriDate 原日期(格式yyyy-mm-dd)
	 * @param nLen       变化天数的范围
	 * @return
	 */
	private static String addDate(String strOriDate, int nLen)
	{
		int nY = Integer.parseInt(strOriDate.substring(0, 4));
		int nM = Integer.parseInt(strOriDate.substring(5, 7));
		int nD = Integer.parseInt(strOriDate.substring(8, 10));
		GregorianCalendar gmt = new GregorianCalendar(nY, nM - 1, nD);
	    gmt.add(Calendar.DAY_OF_YEAR, nLen);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(gmt.getTime());
		return strDate;
	}
	
	public static void main(String[] args) {
		System.out.println("===>"+diffTwoDate("2000-1-01","2000-01-01"));
	}
}
