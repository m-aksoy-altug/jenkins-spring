package com.jenkins.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class SpringControllerTest {
	
	
	@InjectMocks
	private SpringController springController;
	
	@Mock
	private HttpServletRequest httpServletRequest;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
   }
		
	@Test
    public void testOkResponse() {
        when(httpServletRequest.getLocalAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getLocalName()).thenReturn("localhost");
        when(httpServletRequest.getProtocol()).thenReturn("HTTP/1.1");
        when(httpServletRequest.getLocalPort()).thenReturn(8080);
        when(httpServletRequest.getRemoteAddr()).thenReturn("192.168.1.1");
        when(httpServletRequest.getRemoteHost()).thenReturn("remoteHost");
        when(httpServletRequest.getRemotePort()).thenReturn(5050);
        when(httpServletRequest.getServerName()).thenReturn("serverName");
	    ResponseEntity<ObjectNode> response = springController.senddata();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
	
	@Test
    public void testBadResponse() {
		when(httpServletRequest.getLocalAddr()).thenThrow(new RuntimeException("Simulated Exception"));
		ResponseEntity<ObjectNode> response = springController.senddata();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
