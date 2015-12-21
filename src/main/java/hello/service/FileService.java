package hello.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import hello.PropertiConfig;

@Component("fileService")
public class FileService {
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);
	
	@Autowired private PropertiConfig propertiConfig;

	public void htmlToResponce(String siteName, String siteType, HttpServletResponse response) {
		Map<String, Object> contentFileTypes = propertiConfig.getContentFileTypes();
		logger.debug(contentFileTypes.toString());
		Map pages = (Map)contentFileTypes.get("pages");
		Map pageContent = (Map) pages.get(siteName);
		logger.debug(""+pageContent);
		String siteFileName ;
		if(pageContent == null){
			siteFileName = siteName;
		}else{
			String fileTypeName = (String) pageContent.get("fileType");
			logger.debug(fileTypeName);
			Map fileTypes = (Map)contentFileTypes.get("fileTypes");
			Map fileType = (Map)fileTypes.get(fileTypeName);
			siteFileName = (String) fileType.get(siteType);
		}
		logger.debug(siteFileName);
		try {
			File file = new File(propertiConfig.getApplicationHome()+propertiConfig.innerWebapp+"/html/"
					+ siteFileName
					+ ".html");
			logger.debug(file.toString());
			Files.copy(file.toPath(), response.getOutputStream());
		} catch (IOException e) {
			InputStream stream = new ByteArrayInputStream(siteName.getBytes(StandardCharsets.UTF_8));
			try {
				IOUtils.copy(stream, response.getOutputStream());
			} catch (IOException e1) {}
			e.printStackTrace();
		}
	}

	public Map<String, Object> readJsonFromFileName(String fileName) {
		String fileLongName = propertiConfig.folderDb + fileName;
//		fileLongName = fileLongName.trim();
		File file = new File(fileLongName);
		logger.debug(file.toString());
		return readJsonFromFullFileName(file);
	}

	private void readTxtFile(String fileName) {
		String line = null;
		// FileReader reads text files in the default encoding.
		FileReader fileReader;
		try {
			fileReader = new FileReader(fileName);
			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException ex) {
			System.out.println(
					"Error reading file '" 
							+ fileName + "'");
			// Or we could just do this: 
			// ex.printStackTrace();
		}

	}

	private Map<String, Object> readJsonFromFullFileName(File file) {
		Map<String, Object> readJsonFileToJavaObject = null;
		try {
			readJsonFileToJavaObject = mapper.readValue(file, Map.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return readJsonFileToJavaObject;
	}

	ObjectMapper mapper = new ObjectMapper();

	public void saveJsonToFile(Map<String, Object> javaObjectToJson, String fileName) {
		File file = new File(propertiConfig.folderDb + fileName);
		System.out.println(file);
		ObjectWriter writerWithDefaultPrettyPrinter = mapper.writerWithDefaultPrettyPrinter();
		System.out.println(23);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			writerWithDefaultPrettyPrinter.writeValue(fileOutputStream, javaObjectToJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		propertiConfig.setNullContentFileTypes();
	}

	public Map<String, Object> readJsonFromFile(String fileName) {
		File file = new File(propertiConfig.folderDb + fileName);
		Map<String, Object> readJsonFileToJavaObject = null;
		try {
			readJsonFileToJavaObject = mapper.readValue(file, Map.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return readJsonFileToJavaObject;
	}

	public void backup(String fileName) {
		DateTime today = new DateTime();
		String timestampStr = propertiConfig.yyyyMMddHHmmssDateFormat.format(today.toDate());
		try {
			Files.copy(new File(propertiConfig.folderDb + fileName).toPath()
			, new File(propertiConfig.folderDb + "backup/" + fileName +"."+ timestampStr).toPath()
			, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
