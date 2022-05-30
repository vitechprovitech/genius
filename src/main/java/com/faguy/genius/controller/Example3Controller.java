package com.faguy.genius.controller;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
 
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faguy.genius.entity.Article;
import com.faguy.genius.entity.User;
import com.faguy.genius.model.DAO.ArticleRepository;
import com.faguy.genius.model.DAO.UserRepository;
import com.faguy.genius.util.MediaTypeUtils;


 

@RestController
public class Example3Controller {
 
    private  String DIRECTORY = "resources"+File.separator+"partenaires"+File.separator+"fagme2006@yahoo.fr"+File.separator+"VIDEOS"+File.separator;
    private static final String DEFAULT_FILE_NAME = "VID-20191027-WA0016";
 
    @Autowired
    private ServletContext servletContext;
    @Autowired ArticleRepository articleDAO;
    @Autowired UserRepository userDAO;
    // http://localhost:8080/download3?fileName=abc.zip
    // Using HttpServletResponse
	@RequestMapping( method = RequestMethod.GET, path="/telecharger/{type}/{filename}/{idArticle}")
    public void downloadFile3(HttpServletResponse resonse,
    		 @PathVariable("type") String typeMedia, @PathVariable("filename") String fileName, @PathVariable("idArticle") int idArticle ) throws IOException {
		Article a=articleDAO.findArticleById(idArticle);
		if(a!=null) {
			
		}
		User u=a.getEditedBy();	
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);
        switch(typeMedia) {
        case "AUDIO":
        	DIRECTORY="resources"+File.separator+"partenaires"+File.separator+"fagme2006@yahoo.fr"+File.separator+"AUDIOS"+File.separator;
        	break;
        case "VIDEO":
    	    DIRECTORY = "resources"+File.separator+"partenaires"+File.separator+u.getEmail()+File.separator+"VIDEOS"+File.separator;
        	break;
        case "BOOK":
        	DIRECTORY="resources"+File.separator+"partenaires"+File.separator+u.getEmail()+File.separator+"BOOKS"+File.separator;
        	break;
        }
        File file = new File(servletContext.getRealPath(DIRECTORY + fileName));
 
        // Content-Type
        // application/pdf
        resonse.setContentType(mediaType.getType());
 
        // Content-Disposition
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
 
        // Content-Length
        resonse.setContentLength((int) file.length());
 
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());
 
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        try {
	        while ((bytesRead = inStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }        	
        }catch(Exception e) {
        	e.printStackTrace();
        }

        outStream.flush();
        inStream.close();
    }
 
}