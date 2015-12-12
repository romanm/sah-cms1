package hello;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiConfig {

	@Value("${server.port:0000}")
	public  String port;

	@Value("${application.home:/tmp}")
	public  String applicationHome;

	@Value("${folder.db:/tmp}")
	public  String folderDb;

	@Value("${folder.public.files:/view}") 
	public  String folderPublicFiles;

	@Value("${file.common_content:fcc.json}")
	public  String fileCommonContent;

	public String applicationViewPath() {
		return applicationHome + "src/main/webapp/" + folderPublicFiles;
	}

	public final static SimpleDateFormat yyyyMMddHHmmssDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
}
