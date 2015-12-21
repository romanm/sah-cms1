package hello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.service.FileService;

@Controller
public class TestRest {

	@RequestMapping(value = "/page1/readJsonFromRam", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readJsonFromRam() {
		Map<String, Object> map = new HashMap<>();
		map.put("text", "Hello World");
		map.put("int", 123);
		map.put("date", new Date());
		List<Integer> intList = new ArrayList<Integer>(Arrays.asList(new Integer[]{1,2,3}));
		map.put("intList", intList);
		map.put("port", propertiConfig.port);
		return map;
	}

	@Autowired private PropertiConfig propertiConfig;
}
