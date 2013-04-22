package cn.ls.integrator.components.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public abstract class AbstractFile {
	protected static Map<String,AbstractFile> factories = new java.util.HashMap<String,AbstractFile>();
	@SuppressWarnings("unchecked")
	protected Map attributes; // Extend the file by arbitrary user defined
								// extensions

	public static interface AbstractFileFilter {
		public boolean accept(AbstractFile file);
	};

	public static AbstractFileFilter AFFAll = new AbstractFileFilter() {
		public boolean accept(AbstractFile file) {
			return true;
		};
	};

	/**
	 * Register an AbstractFile as a 'factory' for given 'scheme'. The factory
	 * will be used to create new AbstractFiles using the AbstractFile.fromURL(
	 * url ) method. The url for files to be created with the factory have the
	 * form [scheme]:[pathname].
	 * 
	 * @param scheme
	 *            String
	 * @param factory
	 *            AbstractFile, used as factory
	 */
	public static void registerFactory(String scheme, AbstractFile factory) {
		factories.put(scheme, factory);
	};

	static {
		registerFactory(AFFile.scheme, new AFFile());
		registerFactory(AFSmbFile.scheme, new AFSmbFile());
	};

	/**
	 * Create a new AbstractFile from the 'url'. 'url' is of the form
	 * [scheme]:[pathname]. The creation will be redirected to the
	 * createFromUrl() method of the factory registered for the [scheme].
	 * 
	 * @param url
	 *            String
	 * @return AbstractFile
	 */
	public static AbstractFile fromURL(String url) throws Exception {
		String scheme = url.split(":")[0];
		AbstractFile factory = factories.get(scheme);
		return factory.createFromURL(url);
	}

	/**
	 * Create a new instance of the receiver's class for the given 'url' of the
	 * form [scheme]:[pathname] This method allows the receiver to be used as a
	 * factory for new abstract files of the same class.
	 * 
	 * @param url
	 *            String
	 * @throws Exception
	 * @return
	 */
	public abstract AbstractFile createFromURL(String url) throws Exception;

	/**
	 * Return true if the file / directory represented by the receiver exists
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract boolean exists() throws Exception;

	/**
	 * Return the absolute url name of the receiver. The result ends on "/" for
	 * a directory and without "/" for usual files.
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract String getAbsoluteURL() throws Exception;

	/**
	 * Return a generic map of attributes free for use of the application
	 * programmer
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getAttributes() {
		if (attributes == null)
			attributes = new java.util.HashMap();
		return attributes;
	}

	/**
	 * Return the file or directory name, i.e. the last element of the full
	 * path. Always without any path separators.
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract String getName();

	/**
	 * Create a new abstract file or directory defined by the combination of the
	 * receiver's url and the 'path'. The receiver must be a directory. The
	 * 'path' contains directory names and possibly a file name, separated by
	 * "/".
	 * 
	 * @param path
	 * @throws Exception
	 * @return
	 */
	public abstract AbstractFile getChild(String path) throws Exception;

	/**
	 * Return the directory where the receiver is located in.
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract AbstractFile getParent() throws Exception;

	/**
	 * The receiver must represent a file. Return an input stream which allows
	 * to read the file contents.
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract InputStream getInputStream() throws Exception;

	/**
	 * The receiver must represent a file. Return an output stream which allows
	 * to write to the file. If 'append' is true, add any new contents to the
	 * end of the file
	 * 
	 * @return
	 * @param append
	 * @throws Exception
	 */
	public abstract OutputStream getOutputStream(boolean append)
			throws Exception;

	/**
	 * Return the length of the file or directory in bytes.
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract long length() throws Exception;

	/**
	 * Return the last modified time of the receiver in milliseconds since
	 * January 1st, 1970
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract long lastModified() throws Exception;

	/**
	 * Return true if the receiver is a directory
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract boolean isDirectory() throws Exception;

	/**
	 * Return true if the receiver is a file
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract boolean isFile() throws Exception;

	/**
	 * Delete the receiver. If the receiver is a directory, also delete its
	 * contents.
	 * 
	 * @throws Exception
	 */
	public abstract void delete() throws Exception;

	/**
	 * Create the receiver directory and if necessary its parents
	 * 
	 * @throws Exception
	 */
	public abstract void mkdirs() throws Exception;

	/**
	 * Rename the file identified by the receiver to the 'target' file. This
	 * effects the actual file identified, both the receiver object and the
	 * target object will remain unchanged.
	 * 
	 * @param target
	 * @throws Exception
	 */
	public abstract void renameTo(AbstractFile target) throws Exception;
	
	public abstract void renameTo(String targetPath) throws Exception;

	/**
	 * List all files or directories which are direct children of the receiver.
	 * 
	 * @throws Exception
	 * @return
	 */
	public abstract AbstractFile[] listFiles() throws Exception;

	/**
	 * List files or directories which are direct children of the receiver and
	 * which match the 'filter'.
	 * 
	 * @param filter
	 * @throws Exception
	 * @return
	 */
	public AbstractFile[] listFiles(AbstractFileFilter filter) throws Exception {
		AbstractFile files[] = listFiles();
		int r = 0;
		for (int i = 0; i < files.length; i++)
			if (filter.accept(files[i]))
				files[r++] = files[i];
		AbstractFile result[] = new AbstractFile[r];
		System.arraycopy(files, 0, result, 0, r);
		return result;
	};

	/**
	 * Copy all bytes from the 'in' stream to the 'out' stream, until the 'in'
	 * stream is finished and return the number of bytes copied. Use 'buffer' to
	 * copy the bytes. Depending on the 'in' stream's implementation the method
	 * can block (idle or busy) until the 'in' stream is finished.
	 * 
	 * <br>
	 * Creation date: (23.08.00 19.14.34)
	 * 
	 * return long
	 * 
	 * @param in
	 *            java.io.InputStream
	 * @param out
	 *            java.io.OutputStream
	 * @param buffer
	 *            byte[]
	 * @exception java.io.IOException
	 *                The exception description.
	 */
	public final static long copy(java.io.InputStream in,
			java.io.OutputStream out, byte buffer[]) throws java.io.IOException {
		int length;
		long totalLength = 0;
		while ((length = in.read(buffer)) >= 0) {
			totalLength += length;
			out.write(buffer, 0, length);
		}
		;
		return totalLength;
	}

	/**
	 * All files in 'files' will be added to the 'zipStream'. All directories in
	 * 'files' will be added recursively including all files and subdirectories,
	 * even empty subdirectories. If 'files' contains a directory exactely
	 * matching the 'basePath', there will be no entry for this directory, only
	 * for the recursive contents.
	 * <p>
	 * File names will be stored with their absolute path names. If such a name
	 * includes the 'basePath' and / or a leading path separator, these parts
	 * will be removed from the name. The 'newBasePath' will be added as a
	 * leading base path.
	 * 
	 * Creation date: (10.02.2002 21:48:43)
	 * 
	 * @param newBasePath
	 * @param zipStream
	 * @param baseURL
	 *            Start of URL pointing to a directory to be removed from the
	 *            beginning of each file's URL
	 * @param files
	 *            Array of AbstracFiles to be copied into the 'zipStream'
	 *            including all subdirectories
	 * @throws Exception
	 */
	public static void zip(ZipOutputStream zipStream, String baseURL,
			String newBasePath, AbstractFile files[]) throws Exception {
		AbstractFile sourceFile;
		String sourceURL;
		String destEntry;
		InputStream sourceStream = null;
		// Ersure baseURL is a canonical directory name ending with "/",
		// newBasePath end without "/" -> remaining zip path never starts with
		// "/"
		if (!baseURL.endsWith("/"))
			baseURL += "/";
		AbstractFile baseDir = AbstractFile.fromURL(baseURL);
		if (!baseDir.isDirectory())
			throw new Exception("'baseURL' must point to valid directory");
		baseURL = baseDir.getAbsoluteURL();
		if (newBasePath.endsWith("/"))
			newBasePath = newBasePath.substring(0, newBasePath.length() - 1);
		Vector<AbstractFile> allFiles = new Vector<AbstractFile>(Arrays.asList(files));
		try {
			for (int i = 0; i < allFiles.size(); i++) { // Note: 'allFiles' may
														// grow when including
														// directories
				sourceFile = allFiles.elementAt(i);
				sourceURL = sourceFile.getAbsoluteURL();
				if (sourceURL.startsWith(baseURL))
					sourceURL = sourceURL.substring(baseURL.length());
				destEntry = newBasePath + sourceURL;
				if (sourceFile.isDirectory()) { // directory, add subelements to
												// 'allFiles'
					if (sourceURL.length() > 0)
						zipStream.putNextEntry(new ZipEntry(destEntry));
					allFiles.addAll(Arrays.asList(sourceFile.listFiles()));
				} else {
					zipStream.putNextEntry(new ZipEntry(destEntry));
					sourceStream = sourceFile.getInputStream();
					copy(sourceStream, zipStream, new byte[16000]);
					sourceStream.close();
				}
				;
				zipStream.closeEntry();
			}
			;
		} finally {
			try {
				sourceStream.close();
			} catch (Exception exc) {
			}
			;
		}
	}

	/**
	 * The 'inputStream' contains data of a zip file which can be processed via
	 * java.util.ZipInputStream. Extract the subdirectory starting with "path"
	 * and all files and directories included in this subdirectory to the
	 * 'directory'. path = "" will extract all files included. All necessary
	 * directories will be created. The 'inputStream' will be closed at the end. <br>
	 * Example: <br>
	 * The 'in' stream contains the following files: a/a1.txt, a/a2.txt,
	 * b/b1.txt, b/b2.txt <br>
	 * unzip( in, "b", AbstractFile.fromURL( "file://c:/x/" ) ) is called on a
	 * Windows machine <br>
	 * This will extract b/b1.txt -> c:\x\b1.txt, b/b2.txt -> c:\x\b2.txt.
	 * 
	 * Creation date: (10.02.2002 21:48:43)
	 * 
	 * @param inputStream
	 * @param directory
	 * @throws Exception
	 */
	public static void unzip(InputStream inputStream, String path,
			AbstractFile directory) throws Exception {
		ZipInputStream in = new ZipInputStream(inputStream);
		ZipEntry entry;
		if (path.length() > 0 && !path.endsWith("/"))
			path += "/";

		for (; (entry = in.getNextEntry()) != null;) { // Process next entry
			String targetName = entry.getName();
			if (!targetName.startsWith(path))
				continue;
			targetName = targetName.substring(path.length());
			AbstractFile targetFile = directory.getChild(targetName);
			if (entry.isDirectory()) {
				targetFile.mkdirs();
			} else {
				targetFile.getParent().mkdirs();
				OutputStream targetStream = targetFile.getOutputStream(false);
				copy(in, targetStream, new byte[16000]);
				targetStream.close();
			}
			;
			in.closeEntry();
		}
		;
		in.close();
	}

}
