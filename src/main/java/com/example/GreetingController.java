package com.example;

import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;



@RestController
public class GreetingController {
	
	String path="getid.log";
	BufferedWriter out = null;
    
    @RequestMapping("/search")
    public String search(@RequestParam(value="id1", defaultValue="2251253715")long id1,@RequestParam(value="id2", defaultValue="2180737804")long id2) {
 
 
    	AcademicSearch academic =new AcademicSearch(id1,id2);
    	JSONArray result = academic.getHopResults();
    	
    	/*
    	 * 
        try {
        	out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true)));
			out.write(Long.toString(id1)+","+Long.toString(id2)+"\n");
            out.close();   
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}; */
    	
    	return result.toString();
    	
        
    }
    
}
