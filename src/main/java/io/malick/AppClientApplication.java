package io.malick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class AppClientApplication {
	
	@Autowired
	private EurekaClient client;	

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	public static void main(String[] args) {
		SpringApplication.run(AppClientApplication.class, args);
	}
	
	@RequestMapping("/")
	public String callService(){
		//Calls instances in Roundrobin fashion
		
		RestTemplate restTemplate = restTemplateBuilder.build();
		InstanceInfo info = client.getNextServerFromEureka("service", false);
		String baseurl = info.getHomePageUrl();
		ResponseEntity<String> response = restTemplate.exchange(baseurl, HttpMethod.GET, null, String.class);
		
		return response.getBody();
	}

}

