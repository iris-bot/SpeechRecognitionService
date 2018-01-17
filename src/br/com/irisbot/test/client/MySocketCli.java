package br.com.irisbot.test.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.irisbot.test.client.RestClient.Response;

public class MySocketCli {
	
	public static void main(String[] args) {
		
		try {

			openStream("audios/Exemplo 1/1509216131.494056");
			openStream("audios/Exemplo 2/1509216155.494064");
			openStream("audios/Exemplo 3/1509216295.494107");
			
		}catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	

	private static void openStream(final String fileName){
		String[] parts = fileName.split("/");
		String url = "http://softlayer01.iris-bot.com.br/SpeechRecognitionService/orchestrator?cod="+parts[parts.length-1];
		//String url = "http://localhost:8080/SpeechRecognitionService/orchestrator?cod="+parts[parts.length-1];
		System.out.println(url);
		RestClient cli = new RestClient(url) {
			@Override
			public void whenDone(Response resp) {
				try{
					System.out.println(resp.getContent());
					JSONObject json = new JSONObject(new JSONTokener(resp.getContent()));
					System.out.println(json.toString());
					
					connect(new File(fileName+"-out.wav").toPath(), json.getInt("porta_agente"));
					connect(new File(fileName+"-in.wav").toPath(), json.getInt("porta_cliente"));
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		};
		cli.doGet();
	}
	
	private static void connect(Path path, int port) throws Exception{
    	final Socket cli = new Socket("softlayer01.iris-bot.com.br", port); //
    	//final Socket cli = new Socket("localhost", port); //
		final byte[] exemplo = Files.readAllBytes(path);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					cli.getOutputStream().write(exemplo);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
    	
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(cli.getInputStream(), StandardCharsets.US_ASCII));
					String line = "";
			    	StringBuffer sb = new StringBuffer();
			    	while (!br.ready()) Thread.sleep(500);
			    	while((line = br.readLine())!=null && !line.trim().isEmpty()) {
			    		System.out.println(line);
			    		sb.append(line + "\n"); 
			    	}
			    	cli.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		
	}
	
}
