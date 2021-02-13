package org.wlcp.wlcpapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.wlcp.wlcpapi.datamodel.master.Username;
import org.wlcp.wlcpapi.dto.UserRegistrationDto;
import org.wlcp.wlcpapi.repository.UsernameRepository;
import org.wlcp.wlcpapi.service.UsernameService;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UsernameController.class)
public class TestUsernameController {
	
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private UsernameRepository usernameRepository;
    
    @MockBean
    private UsernameService usernameService;
    
    @Test
    public void testGetUsernameByUsernameIdSuccess() throws Exception {
    	when(usernameRepository.findById(any(String.class))).thenReturn(Optional.of(new Username()));
    	mvc.perform(get("/usernameController/getUsername/usernameid")
    			  .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    		      .contentType(MediaType.APPLICATION_JSON))
    		      .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testRegisterUserSuccess() throws UnsupportedEncodingException, Exception {
    	Username username = new Username("usernameid", "test", "test", "test", "test@test.com");
    	when(usernameService.registerUser(any(UserRegistrationDto.class))).thenReturn(username);
    	mvc.perform(post("/usernameController/registerUser")
  			  .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
  		      .contentType(MediaType.APPLICATION_JSON)
  		      .content(new ObjectMapper().writeValueAsString(username)))
  		      .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

}
