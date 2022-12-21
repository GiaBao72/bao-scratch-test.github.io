package rrs.model.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * {@link FileService} implements this code ༼ つ ◕_◕ ༽つ
 * @see FileService#uri(String...) concat to path
 */
public interface FileUpload {
	
	public static final String DEFAULT_FOLDER = "/data";
	
	/**
	 * @param directories are contain folders or files
	 * @return the path path to the server - EX: http://localhost:8080/data/images/...
	 */
	public String pointingFolder(String...directories);

	/**
	 * @param fileOrDir get only files or folders else all
	 * @param directories are contain folders or files
	 * @return all file're path with condition's fileOrDir
	 * <h3><b>EX: </b>[abc.png, bcd.jpg, cde.pdf, ...]</h3>
	 */
	public String[] fileNames(Boolean fileOrDir, String...directories);

	/**
	 * @param directories to create
	 * @return directory created;
	 */
	public String saveFolder(String...directories);
	
	/**
	 * @param file is {@link MultipartFile} to save
	 * @param directories are contain folders or files
	 * @return file name has been saved
	 */
	public String saveFile(MultipartFile file, String...directories);
	
	/**
	 * @param file is {@link MultipartFile} to save
	 * @param fileName to set name of file
	 * @param directories are contain folders or files
	 * @return file name has been saved
	 */
	public String saveFile(String fileName, MultipartFile file, String...directories);
	
	/**
	 * @param files are {@link MultipartFile} to save
	 * @param directories are contain folders or files
	 * @return all files're name has been saved
	 */
	public List<String> saveFile(MultipartFile[] files, String...directories);
	
	/**
	 * @param uri to delete
	 * @throws IOException 
	 */
	public void deleteFile(String uri) throws IOException;

	/**
	 * @param fileNames're name to delete in directories
	 * @param directories are contain folders or files
	 */
	public void deleteFiles(String[] fileNames, String...directories);
}
