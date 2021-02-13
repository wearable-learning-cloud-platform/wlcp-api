package org.wlcp.wlcpapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.wlcp.wlcpapi.datamodel.master.ObjectStore;
import org.wlcp.wlcpapi.service.ObjectStoreService;
import org.wlcp.wlcpapi.service.UsernameService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ObjectStoreController.class)
public class TestObjectStoreController {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private ObjectStoreService objectStoreService;
    
    @MockBean
    private UsernameService usernameService;
    
    @Test
    public void testGetFileSuccess() throws UnsupportedEncodingException, Exception {
    	ObjectStore objectStore = new ObjectStore("testfile", "image/png", new byte[5]);
    	when(objectStoreService.getFile(any(String.class))).thenReturn(objectStore);
    	mvc.perform(get("/objectStoreController/files/testfile")
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    		    .contentType(MediaType.APPLICATION_JSON))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
    @Test
    public void testUploadFileSuccess() throws UnsupportedEncodingException, Exception {
    	MockMultipartFile file = new MockMultipartFile("file", new byte[1024]);
    	when(objectStoreService.store(file)).thenReturn(new ObjectStore());
    	mvc.perform(multipart("/objectStoreController/uploadFile").file(file)
    			.header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIifQ.CFsDr9KnYtLCfXkvDyyDyEUdnu5RPPyFQ32jiKMQ8NsbmTfMwVmIeWO0AJxYs8uyPP4txkvy4sP4T6asVN5cIw")
    		    .contentType(MediaType.APPLICATION_OCTET_STREAM))
    		    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
    
}
