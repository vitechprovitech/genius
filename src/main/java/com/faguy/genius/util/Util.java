package com.faguy.genius.util;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.faguy.genius.entity.User;
import com.faguy.genius.model.DTO.ProfilDTO;
import com.faguy.genius.model.DTO.UserDTO;
import com.faguy.genius.model.DTO.VilleDTO;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;


@Service
public class Util {
    @Autowired
    private  JavaMailSender mailSender;
	public int generateCode(int min, int max) {
		Random r = new Random();
		int low = 100000;
		int high = 999999;
		int result = r.nextInt(high-low) + low;
		return result;
	}

	   public static boolean validateEmail(String email) {  
		   String regex = "^(.+)@(.+)$";   
		   Pattern pattern = Pattern.compile(regex);
		   Matcher matcher = pattern.matcher(email); 
		   return matcher.matches();
	   }
	   public boolean validatePhone(String phone) {
		   String regex = "^\\d{10}$";   
		   Pattern pattern = Pattern.compile(regex);
		   Matcher matcher = pattern.matcher(phone); 
		   return matcher.matches();
	   }

	/*
	 * public static void generatePDF(File file) { RandomAccessFile raf = null; try
	 * { raf = new RandomAccessFile(file, "r"); } catch (FileNotFoundException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } FileChannel channel
	 * = raf.getChannel(); ByteBuffer buf = null; try { buf =
	 * channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * PDFFile pdffile = null; try { pdffile = new PDFFile(buf); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * // draw the first page to an image PDFPage page = pdffile.getPage(0);
	 * 
	 * //get the width and height for the doc at the default zoom Rectangle rect =
	 * new Rectangle(0,0, (int)page.getBBox().getWidth(),
	 * (int)page.getBBox().getHeight());
	 * 
	 * //generate the image Image img = page.getImage( rect.width, rect.height,
	 * //width & height rect, // clip rect null, // null for the ImageObserver true,
	 * // fill background with white true // block until drawing is done ); }
	 */
	 public static String getMd5(String input)
	    {
	        try {
	  
	            // Static getInstance method is called with hashing MD5
	            MessageDigest md = MessageDigest.getInstance("MD5");
	  
	            // digest() method is called to calculate message digest
	            //  of an input digest() return array of byte
	            byte[] messageDigest = md.digest(input.getBytes());
	  
	            // Convert byte array into signum representation
	            BigInteger no = new BigInteger(1, messageDigest);
	  
	            // Convert message digest into hex value
	            String hashtext = no.toString(16);
	            while (hashtext.length() < 32) {
	                hashtext = "0" + hashtext;
	            }
	            return hashtext;
	        } 
	  
	        // For specifying wrong message digest algorithms
	        catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
	    }
   @Autowired
    private JavaMailSender javaMailSender;
    
    public void sendEmail(String email, String message, String titre) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(titre);
        msg.setText(message);
        
        javaMailSender.send(msg);
    }
    public UserDTO toUserDTO(User user) {
    	return new UserDTO(user.getLogin(), user.getName(), new ProfilDTO(user.getProfil().getId(), user.getProfil().getProfil(), user.getProfil().getDetails()), new VilleDTO(user.getCity().getNom()), user.getEmail(),  user.getPhone(), user.getAddress(), user.getBirthDate().getTime(), user.getPassword(), user.getMAC(), user.getStatut());
    	
    }

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        
        mailSender.setUsername("fagme2009@gmail.com");
        mailSender.setPassword("12/05/2018");
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        
        return mailSender;
    }
}
