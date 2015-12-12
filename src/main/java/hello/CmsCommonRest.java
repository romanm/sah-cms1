package hello;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.service.JsonToFileService;

@Controller
public class CmsCommonRest {

	@Autowired private JsonToFileService jsonToFileService;
	@Autowired private PropertiConfig propertiConfig;
	
	@RequestMapping(value = "/v/readContent", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readBugTinyWiki() {
		Map<String, Object> readJsonFromFile = jsonToFileService.readJsonFromFileName(propertiConfig.fileCommonContent);
		return readJsonFromFile;
	}

	@RequestMapping(value = "/saveCommonContent", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> saveBugTinyWiki(@RequestBody Map<String, Object> commonContentJavaObject) {
		jsonToFileService.saveJsonToFile(commonContentJavaObject,propertiConfig.fileCommonContent);
		jsonToFileService.backup(propertiConfig.fileCommonContent);
		return commonContentJavaObject;
	}

	@RequestMapping(value = "/v", method = RequestMethod.GET)
	public String view() { return "redirect:/v/v.html?page1"; }
	@RequestMapping(value = "/e", method = RequestMethod.GET)
	public String edit() { return "redirect:/v/v.html?page1"; }

	@RequestMapping(value = "/read_user", method = RequestMethod.GET)
	public  @ResponseBody Principal getRoleTypes(Principal userPrincipal) {
		return userPrincipal;
	}
	
}
