package com.jenkins.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class SpringController {

	private static final Logger log = LoggerFactory.getLogger(SpringController.class);

	@Autowired
	private HttpServletRequest request;

	@GetMapping(path = "/dummy", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ObjectNode> senddata() {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode responseNode = objectMapper.createObjectNode();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		String json = "{\"date\":\"failed\"}";
		HashMap<String, String> responseHashMap = new HashMap<String, String>();
		try {
			responseHashMap.put("Sending random value", "to Kafka");
			responseHashMap.put("Local IP Address", request.getLocalAddr());
			responseHashMap.put("Local Name", request.getLocalName());
			responseHashMap.put("Local Protocal", request.getProtocol());
			responseHashMap.put("Local Port", request.getLocalPort() + "");
			responseHashMap.put("Remote IP Address", request.getRemoteAddr());
			responseHashMap.put("Remote Host", request.getRemoteHost());
			responseHashMap.put("Remote Port", request.getRemotePort() + "");
			responseHashMap.put("Server Name", request.getServerName());
			json = objectMapper.writeValueAsString(responseHashMap);
			log.info("Json:" + json.toString());
			JsonNode node = objectMapper.readTree(json);
			return new ResponseEntity<>((ObjectNode) node, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>((ObjectNode) responseNode, headers, HttpStatus.BAD_REQUEST);
		}

	}

}
