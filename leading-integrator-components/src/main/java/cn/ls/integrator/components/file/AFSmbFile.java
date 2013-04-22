package cn.ls.integrator.components.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class AFSmbFile extends AbstractFile{
	protected SmbFile smbFile;
	protected static final String scheme = "smb";
	static final Pattern pwdPattern = Pattern.compile("/|(:[^(/@)]*@)");

	protected AFSmbFile() {
	}; // For factory creation

	public AFSmbFile(String pathname) throws Exception {
		smbFile = new SmbFile(pathname);
	};

	public AFSmbFile(SmbFile file) {
		smbFile = file;
	};

	public AbstractFile createFromURL(String url) throws Exception {
		return new AFSmbFile(url);
	}

	public boolean exists() throws Exception {
		return smbFile.exists();
	}

	public String getAbsoluteURL() throws Exception {
		return smbFile.getCanonicalPath();
	}

	public String getName() {
		return smbFile.getName().replaceAll("/", "");
	}

	public AbstractFile getChild(String path) throws Exception {
		return createFromURL(getAbsoluteURL() + path);
	}

	public AbstractFile getParent() throws Exception {
		return new AFSmbFile(smbFile.getParent());
	}

	public InputStream getInputStream() throws Exception {
		return new SmbFileInputStream(smbFile);
	}

	public OutputStream getOutputStream(boolean append) throws Exception {
		return new SmbFileOutputStream(smbFile, append);
	}

	public boolean isDirectory() throws Exception {
		return smbFile.isDirectory();
	}

	public boolean isFile() throws Exception {
		return smbFile.isFile();
	}

	public long lastModified() throws Exception {
		return smbFile.lastModified();
	}

	public long length() throws Exception {
		return smbFile.length();
	}

	public void delete() throws Exception {
		smbFile.delete();
	}

	public void mkdirs() throws Exception {
		if (exists() && isDirectory())
			return;
		smbFile.mkdirs();
	}

	public AbstractFile[] listFiles() throws Exception {
		SmbFile files[] = smbFile.listFiles();
		AbstractFile result[] = new AbstractFile[files.length];
		String orgPath = smbFile.getPath();
		for (int i = 0; i < files.length; i++)
			result[i] = new AFSmbFile(orgPath + files[i].getName());
		return result;
	}

	public void renameTo(AbstractFile target) throws Exception {
		if (!(target instanceof AFSmbFile))
			throw new Exception("target must be of same type than receiver");
		smbFile.renameTo(((AFSmbFile) target).smbFile);
	}

	/**
	 * Return a string representing the receiver path in the form smb://....
	 * ; suppress password
	 * 
	 * @return
	 */
	public String toString() {
		String path = smbFile.getCanonicalPath().substring(6); // remove
																// smb://
																// from path
		Matcher m = pwdPattern.matcher(path);
		if (m.find() && path.charAt(m.start()) == ':')
			path = m.replaceFirst(":...@");
		return "smb://" + path;
	}

	@Override
	public void renameTo(String targetPath) throws Exception {
		renameTo(new AFSmbFile(targetPath));
	}
}
