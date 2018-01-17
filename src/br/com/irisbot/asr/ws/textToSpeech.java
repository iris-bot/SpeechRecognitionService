package br.com.irisbot.asr.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import com.google.api.client.util.IOUtils;
import com.google.common.io.Files;
import com.google.gson.JsonObject;

import br.com.irisbot.asr.ws.RestClient.Response;

@WebServlet("/textToSpeech")
public class textToSpeech extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	private static final HashMap<Integer, String> FILA = new HashMap<>();
	
	public textToSpeech() {
        super();
    }

	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		
//		RestClient cli = new RestClient("https://centauro-cadu.mybluemix.net/tts?text="+request.getParameter("text")) {
//			@Override
//			public void whenDone(Response resp) {
//				byte[] bytes = resp.getContent().getBytes();
//				File wav = null;
//				try{
//					wav = File.createTempFile("audio", "iris.wav"); 
//					Files.write(bytes, wav);
//					
//					response.setContentType("application/x-wav");
//					response.setHeader("Content-Disposition", "filename=\""+wav.getName()+"\"");
//				    IOUtils.copy(new FileInputStream(wav), response.getOutputStream());
//					
//				}catch (Exception e) {
//					try{
//						response.sendError(400);
//					}catch (Exception ex) {}
//				}
//			}
//		};
		
		InputStream in = new URL("https://centauro-cadu.mybluemix.net/tts?text="+URLEncoder.encode(request.getParameter("text"))).openStream();
		IOUtils.copy(in, response.getOutputStream());
		
	}
	

}
