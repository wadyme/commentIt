package cn.ls.integrator.components.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AFFile extends AbstractFile{

	protected java.io.File file;
	protected boolean isDirectory = false; // Valid only if file does not
											// exist
	protected static final String scheme = "file";

	/**
	 * Return a canonical file name from the url. Please note that this even
	 * for directories may end without a trailing file separator
	 * 
	 * @param url
	 * @throws Exception
	 * @return
	 */
	public static String fileNameFromURL(String url) throws Exception {
		String result = url.substring((scheme + ":/").length()).replace(
				'/', File.separatorChar);
		return new File(result).getCanonicalPath();
	}

	protected AFFile() {
	}; // For factory creation

	public AFFile(String path) {
		file = new File(path);
		isDirectory = path.endsWith(File.separator);
	};

	public AFFile(File aFile, boolean isDirectory) {
		file = aFile;
		this.isDirectory = isDirectory;
	};

	public AFFile(File aFile) {
		file = aFile;
	};

	public AbstractFile createFromURL(String url) throws Exception {
		return new AFFile(new File(fileNameFromURL(url)), url.endsWith("/"));
	}

	public boolean exists() throws Exception {
		return file.exists();
	}

	public String getAbsoluteURL() throws Exception {
		String result = "file:/"
				+ file.getAbsolutePath().replace(File.separatorChar, '/');
		if (!result.endsWith("/") && isDirectory())
			result += "/";
		return result;
	}

	public String getName() {
		return file.getName();
	}

	public AbstractFile getChild(String path) throws Exception {
		return createFromURL(getAbsoluteURL() + path);
	}

	public AbstractFile getParent() throws Exception {
		return new AFFile(file.getParent());
	}

	public InputStream getInputStream() throws Exception {
		return new FileInputStream(file);
	}

	public OutputStream getOutputStream(boolean append) throws Exception {
		return new FileOutputStream(file, append);
	}

	public boolean isDirectory() throws Exception {
		return file.exists() ? file.isDirectory() : isDirectory;
	}

	public boolean isFile() throws Exception {
		return file.isFile();
	}

	public long lastModified() throws Exception {
		return file.lastModified();
	}

	public long length() throws Exception {
		return file.length();
	}

	public void delete() throws Exception {
		if (file.isDirectory()) { // recursive delete
			String names[] = file.list();
			if (names == null)
				throw new IOException(
						"Could not open file list of directory: "
								+ file.getPath());
			for (int i = 0; i < names.length; i++)
				new AFFile(new File(file, names[i])).delete();
		}
		;
		if (!file.delete())
			throw new IOException("Could not remove file or directory: "
					+ file.getPath());
	}

	public void mkdirs() throws Exception {
		if (exists() && isDirectory())
			return;
		if (!file.mkdirs())
			throw new Exception("Could not create directory");
	}

	public AbstractFile[] listFiles() throws Exception {
		File files[] = file.listFiles();
		AbstractFile result[] = new AbstractFile[files.length];
		for (int i = 0; i < files.length; i++)
			result[i] = new AFFile(files[i]);
		return result;
	}

	public void renameTo(AbstractFile target) throws Exception {
		if (!(target instanceof AFFile))
			throw new Exception("target must be of same type than receiver");
		java.io.File targetFile = ((AFFile) target).file;
		if (!file.renameTo(targetFile))
			renameByCopy(targetFile);
	}

	private void renameByCopy(File targetFile) throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(targetFile);
			byte[] buff = new byte[1024 * 1024];
			int i = 0;
			while ((i = fis.read(buff)) > 0) {
				fos.write(buff, 0, i);
			}
			fos.flush();
			delete();
		} catch (IOException e) {
			throw new Exception("Could not rename file", e);
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
		}

	}

	public String toString() {
		return "file:/"
				+ file.getAbsolutePath().replace(File.separatorChar, '/');
	}

	@Override
	public void renameTo(String targetPath) throws Exception {
		renameTo(new AFFile(targetPath));
	}

}
