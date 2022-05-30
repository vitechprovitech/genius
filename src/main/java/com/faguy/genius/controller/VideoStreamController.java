package com.faguy.genius.controller;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import static java.lang.Math.min;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faguy.genius.entity.Article;
import com.faguy.genius.entity.User;
import com.faguy.genius.model.DAO.ArticleRepository;




@RestController
public class VideoStreamController {
	
@Autowired
private ServletContext context;
	private static final long CHUNK_SIZE = 1000000L;
	public static  String VideoUploadingDir = "ressources" +File.separator+ "partenaires"+File.separator;
	@Autowired ArticleRepository articleDAO=null;
	//@RequestMapping(value = "/test", method = RequestMethod.GET, path = "/{nom}")
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(){
		
		return "bonjour";
	}
	//@GetMapping(value = "/video", produces = "")
	@RequestMapping( produces = "application/octet-stream" , method = RequestMethod.GET, path="faguy/{typeMedia}/{idArticle}/{fileName}")
	public ResponseEntity<ResourceRegion> getImage(@RequestHeader(value = "Range", required = false) String rangeHeader, @PathVariable("idArticle") int idArticle, @PathVariable("typeMedia") String typeMedia,  @PathVariable("fileName") String fichier )
			throws IOException {
		String mybox="";
		System.err.print("entre");
		Article article=articleDAO.findArticleById(idArticle);
		User user=article.getEditedBy();
		String email=user.getEmail();
		switch(typeMedia) {
		case "BOOK"
		:	mybox=email+File.separator+"BOOKS"+File.separator+fichier;
			break;
		case "AUDIO":
			mybox=email+File.separator+"AUDIOS"+File.separator+fichier;
			break;
		case "VIDEO":
			mybox=email+File.separator+"VIDEOS"+File.separator+fichier;
			break;
		}
		String absolutePath = context.getRealPath("resources"+File.separator+"partenaires");
		VideoUploadingDir=absolutePath;
		if (!new File(VideoUploadingDir).exists()) {
			new File(VideoUploadingDir).mkdirs();
		}
		return getVideoRegion(rangeHeader, mybox);

	}
	//@GetMapping(value = "/video", produces = "")
	@RequestMapping( produces = "application/octet-stream" , method = RequestMethod.GET, path="/video/{typeMedia}/{idArticle}/{fileName}")
	public ResponseEntity<ResourceRegion> getVideo(@RequestHeader(value = "Range", required = false) String rangeHeader, @PathVariable("idArticle") int idArticle, @PathVariable("typeMedia") String typeMedia,  @PathVariable("fileName") String fichier )
			throws IOException {
		String mybox="";
		System.err.println("entré");
		Article article=articleDAO.findArticleById(idArticle);
		User user=article.getEditedBy();
		String email=user.getEmail();
		switch(typeMedia) {
		case "BOOK"
		:	mybox=email+File.separator+"BOOKS"+File.separator+fichier;
			break;
		case "AUDIO":
			mybox=email+File.separator+"AUDIOS"+File.separator+fichier;
			break;
		case "VIDEO":
			mybox=email+File.separator+"VIDEOS"+File.separator+fichier;
			break;
		}
		String absolutePath = context.getRealPath("resources"+File.separator+"partenaires");
		VideoUploadingDir=absolutePath;
		if (!new File(VideoUploadingDir).exists()) {
			new File(VideoUploadingDir).mkdirs();
		}
		return getVideoRegion(rangeHeader, mybox);

	}
	

	public ResponseEntity<ResourceRegion> getVideoRegion(String rangeHeader, String mybox) throws IOException {
		String absolutePath = context.getRealPath("resources"+File.separator+"partenaires");
		VideoUploadingDir=absolutePath;
		FileUrlResource videoResource = new FileUrlResource(VideoUploadingDir+File.separator+mybox);
		ResourceRegion resourceRegion = getResourceRegion(videoResource, rangeHeader);

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(videoResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(resourceRegion);
	}

	private ResourceRegion getResourceRegion(UrlResource video, String httpHeaders) throws IOException {
		ResourceRegion resourceRegion = null;

		long contentLength = video.contentLength();
		int fromRange = 0;
		int toRange = 0;
		if (StringUtils.isNotBlank(httpHeaders)) {
			String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
			fromRange = Integer.valueOf(ranges[0]);
			if (ranges.length > 1) {
				toRange = Integer.valueOf(ranges[1]);
			} else {
				toRange = (int) (contentLength - 1);
			}
		}

		if (fromRange > 0) {
			long rangeLength = min(CHUNK_SIZE, toRange - fromRange + 1);
			resourceRegion = new ResourceRegion(video, fromRange, rangeLength);
		} else {
			long rangeLength = min(CHUNK_SIZE, contentLength);
			resourceRegion = new ResourceRegion(video, 0, rangeLength);
		}

		return resourceRegion;
	}

}