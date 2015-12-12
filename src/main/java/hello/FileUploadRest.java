package hello;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import hello.PropertiConfig;

@Controller
public class FileUploadRest {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadRest.class);

	@Autowired private PropertiConfig propertiConfig;
	
	private String arar(String raion) {
		String dir = "anesthesia-report/2015/"+raion+"/";
		return dir;
	}
	public void copyDbFileToWeb(String fileName) {
		String fromDir = propertiConfig.folderDb+propertiConfig.folderPublicFiles;
		logger.debug("cp "+fromDir+fileName +" "+propertiConfig.applicationViewPath());
		try {
			Path sourceFile = new File(fromDir+fileName).toPath();
			Path targetFile = new File(propertiConfig.applicationViewPath()+fileName).toPath();
			Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/v/readPublicFiles", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readSahYearZwit(Principal userPrincipal) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		final List<String> files = new ArrayList<String>();
		map.put("files", files);
		File dirFile = new File(propertiConfig.applicationViewPath());
		if(dirFile.exists()){
			Path pathHtmlLarge = dirFile.toPath();

			Files.walkFileTree(pathHtmlLarge, new SimpleFileVisitor<Path>() {
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {
					final FileVisitResult visitFile = super.visitFile(file, attrs);
					final String fileName = file.toString().replace(file.getParent().toString(), "").substring(1);
					files.add(fileName);
					return visitFile;
				}
			});
		}
		return map;
	}

	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody String handleFileUpload(
			@RequestParam("file") MultipartFile file
			, @RequestParam("fileName") String fileName
			, Principal userPrincipal){
		
		String dbFileViewDir = propertiConfig.folderDb + propertiConfig.folderPublicFiles;
		logger.debug(dbFileViewDir);
		File dm = openCreateFolder(dbFileViewDir);
logger.debug(""+dm);
logger.debug(""+file);
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = 
						new BufferedOutputStream(new FileOutputStream(new File(dbFileViewDir+fileName)));
				stream.write(bytes);
				stream.close();
				copyDbFileToWeb(fileName);
				return "You successfully uploaded " + fileName + "!";
			} catch (Exception e) {
				return "You failed to upload " + fileName + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + fileName + " because the file was empty.";
		}
	}
	private File openCreateFolder(String dir) {
		logger.debug(dir);
		File file = new File(dir);
		if(!file.exists())
		{
			logger.debug(dir);
			file.mkdirs();
		}
		return file;
	}

	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a file by posting to this same URL.";
	}
	
	
	  @RequestMapping(
              value = "/upload2",
              method = RequestMethod.POST
          )
          public ResponseEntity uploadFile(MultipartHttpServletRequest request) {
logger.debug("-----------------------");
              try {
                  Iterator<String> itr = request.getFileNames();

                  while (itr.hasNext()) {
                      String uploadedFile = itr.next();
                      MultipartFile file = request.getFile(uploadedFile);
                      String mimeType = file.getContentType();
                      String filename = file.getOriginalFilename();
                      byte[] bytes = file.getBytes();
                      logger.debug("-----------------------"+uploadedFile);

//                      FileUpload newFile = new FileUpload(filename, bytes, mimeType);
//
//                      fileUploadService.uploadFile(newFile);
                  }
              }
              catch (Exception e) {
                  return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
              }

              return new ResponseEntity<>("{}", HttpStatus.OK);
          }
	
}
