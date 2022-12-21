package rrs.control.rest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rrs.model.services.FileService;
import rrs.model.utils.FileUpload;

@CrossOrigin("*")
@RestController
@RequestMapping({"/rest"})
public class FileRestControl {

	// @formatter:off
	@Autowired private FileUpload service;
	
	// get file's paths
	@GetMapping("/dir/**")
	public ResponseEntity<Object> getList(HttpServletRequest req) throws IOException {
		String uri = getURI(req, "dir/");
		String path = service.pointingFolder(uri);
		if(uri.endsWith(".json")) {
			String data = ((FileService) service).pathLocal(uri);
			return ResponseEntity.ok(FileUtil.readAsString(new File(data)));
		} else {
			List<String> list = Arrays.asList(service.fileNames(true, uri));
			for (int i = 0; i < list.size(); i++) list.set(
				i, new StringBuilder(path).append("/").append(list.get(i)).toString()
			);
			return ResponseEntity.ok(list);
		}
	}
	
	@GetMapping("/dirmap/**")
	public ResponseEntity<Map<String, Object>> getMap(HttpServletRequest req) {
		String uri = getURI(req, "dirmap/");
		
		Map<String, Object> map = Map.of(
			"path",service.pointingFolder(uri),
			"files", Arrays.asList(service.fileNames(null, uri))
		); return ResponseEntity.ok(map);
	}

	@PostMapping("/dir/**") // save file
	public ResponseEntity<List<String>> save(HttpServletRequest req,
			@RequestParam(required = false) MultipartFile[] files) {
		String uri = getURI(req, "dir/");
		return ResponseEntity.ok(files != null 
				? service.saveFile(files, uri) 
				: Arrays.asList(service.saveFolder(uri))
			);
	}

	@DeleteMapping("/dir/**")
	public ResponseEntity<Void> delete(HttpServletRequest req, 
			@RequestParam(required = false) String[] fileNames) throws IOException {
		String uri = getURI(req, "dir/");
		if(fileNames != null) service.deleteFiles(fileNames, uri);
		else service.deleteFile(uri);
		return ResponseEntity.ok().build();
	}
	 
	// @formatter:on
	private String getURI(HttpServletRequest req, String cutAt) {
		String uri = req.getRequestURI();
		return uri.substring(uri.indexOf(cutAt)+cutAt.length());
	}
}