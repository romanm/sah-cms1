package hello;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hello.service.FileService;

@Component
public class PropertiConfig {

	private static final Logger logger = LoggerFactory.getLogger(PropertiConfig.class);
	@Autowired private FileService fileService;
	Map contentFileTypes = null;
	public void setNullContentFileTypes(){
		contentFileTypes = null;
	}
	public Map<String, Object> getContentFileTypes(){
		if(contentFileTypes == null){
			contentFileTypes = 
					fileService.readJsonFromFileName(fileCommonContent);
			contentFileTypes.remove("title");
			Map map = (Map)contentFileTypes.get("pages");
			for (String pageName : (Set<String>) map.keySet()) {
				Map pageContent = (Map) map.get(pageName);
				pageContent.remove("title");
				pageContent.remove("html");
			}
		}
		return contentFileTypes;
	}
	@Value("${server.port:0000}")
	public  String port;

	@Value("${application.home:/tmp}")
	private String applicationHome;
	public String getApplicationHome() {
		return applicationHome;
	}
	public String applicationViewPath() {
		return getApplicationHome() + innerWebapp + folderPublicFiles;
	}

	@Value("${folder.db:/tmp}")
	public  String folderDb;

	@Value("${folder.public.files:/view}") 
	public  String folderPublicFiles;

	@Value("${file.common_content:fcc.json}")
	public  String fileCommonContent;



	public final static SimpleDateFormat yyyyMMddHHmmssDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public final static String innerWebapp = "src/main/webapp/";
}
