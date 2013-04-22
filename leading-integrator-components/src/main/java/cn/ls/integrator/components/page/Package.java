package cn.ls.integrator.components.page;

/**
 * 包含分包相关信息接口
 * 
 * @author wanl
 * 
 * @since 1.0
 */
public class Package {
	/**
	 * 包数
	 * 
	 * @param total
	 *            总数
	 * @param size
	 *            包大小
	 * @return
	 */
	public static long getPackageSize(long total, long size) {
		if (size == 0)
			size = Integer.MAX_VALUE;
		long totalPackages = 0;
		if (total % size == 0) {
			totalPackages = total / size;
		} else {
			totalPackages = (total / size) + 1;
		}
		return totalPackages;
	}

	/**
	 * 获取最后
	 * 
	 * @param total
	 * @param size
	 * @param currentPage
	 * @param totalPages
	 * @return
	 */
	public static long getLastIndex(long total, long size, long currentPage,
			long totalPages) {
		long lastIndex = 0;
		if (total < size) {// 总记录数小于每页最数
			lastIndex = total;
		} else if (size ==1) {
			lastIndex = total;
		} else if ((total % size == 0)
				|| (total % size != 0 && currentPage < totalPages)) {// 不是最后一页
			lastIndex = currentPage * size;

		} else if (total % size != 0 && currentPage == totalPages) {// 最后一页
			lastIndex = total;
		}
		return lastIndex;
	}

}
