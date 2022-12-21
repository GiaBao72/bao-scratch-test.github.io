package rrs.model.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import rrs.model.utils.FileUpload;

@Service
public class FileService implements FileUpload {
	
	@Autowired private ServletContext context;
		
	@Override // read SERVER folders contain files
	public String pointingFolder(String...directories) {
		String uri = uri(directories);
		File file = new File(context.getRealPath(""), uri);
		uri = pathServer(uri); // get path on the server;
		return file.isFile() 
			? uri.substring(0, uri.lastIndexOf('/'))
			: file.canRead() ? uri : null;
	}
	
	@Override // read all files're name
	public String[] fileNames(Boolean fileOrDir, String...directories) {
		File directory = new File(pathLocal(directories));

		return fileOrDir==null ? directory.list() // only file or folder else fileOrDir is null
			: directory.list((dir, name) -> fileOrDir==name.lastIndexOf(".")>-1);
	}
	
	public String saveFolder(String...directories) {
		return mkdirs(directories).getName();
	}
	
	@Override
	public String saveFile(MultipartFile file, String...directories) {
		return this.saveFile(null, file, directories);
	}
	
	@Override
	public String saveFile(String fileName, MultipartFile file, String...directories) {
		try {
			String directory = mkdirs(directories).getAbsolutePath();
			// check fileName
			boolean check = fileName == null;
			if(!check) check = fileName.isEmpty();
			
			Path path = Paths.get(directory, check ? file.getOriginalFilename() : fileName);
			if(!path.toFile().exists()) {
				file.transferTo(path);
				return check ? file.getOriginalFilename() : fileName;
			} else System.err.println("File name's "+fileName+" already exists, cannot be saved.");
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	@Override // return all files're name saved
	public List<String> saveFile(MultipartFile[] files, String...directories) {
		if(files == null) return new ArrayList<>();
		String directory = mkdirs(directories).getAbsolutePath();
		
		List<String> list = new LinkedList<>();
		for(MultipartFile file : files) {
			try {
				String fileName = file.getOriginalFilename();
				Path path = Paths.get(directory, fileName);
				if(!path.toFile().exists()) {
					file.transferTo(path);
					list.add(fileName);
				} else System.err.println("File name's "+fileName+" already exists, cannot be saved.");
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		} return list;
	}
	
	@Override // delete file
	public void deleteFile(String uri) throws IOException {
		if(uri == null) return;
		File file = new File(context.getRealPath(uri(uri)));
		if(!file.delete()) FileUtils.deleteDirectory(file);
		
	}
	
	@Override // delete all files
	public void deleteFiles(String[] fileNames, String...directories) {
		if(fileNames == null || directories == null) return;
		
		String path = pathLocal(directories);
		for(String fileName : fileNames) {
			new File(
					new StringBuilder(path).append("/").append(fileName).toString()
			).deleteOnExit();
		}
	}
	
	
	// Create URI methods
	
	// return uri concat multiple path's parameters to path - EX: /data/images/...
	public static final String uri(String...directories) {
		if(directories == null) return DEFAULT_FOLDER;

		StringBuilder str;
		if(directories[0].startsWith(DEFAULT_FOLDER))
			str = new StringBuilder();
		else str = new StringBuilder(DEFAULT_FOLDER);
		
		for(String directory : directories) str.append("/").append(directory);
		return str.toString();
	}
	
	// get local on this PC - EX: file://C:/.../src/main/data/images/...
	public String pathLocal(String...directories) {
		return context.getRealPath(uri(directories));
	}
	
	// get url on server - EX: http://localhost:8080/data/images/...
	String pathServer(String path) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).toUriString();
	}

	// get file and and check doesn't exists to make folders
	private File mkdirs(String...directories) {
		File file = new File(pathLocal(directories));
		if(!file.exists()) file.mkdirs();
		return file;
	}

}
