package br.com.irisbot.cli.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MySocketCli {
	
	public static void main(String[] args) {
		
		try {
	    	/**
	    	 * 1: 1509216131.494056-in, 1509216131.494056-out
	    	 * 2: 1509216155.494064-in, 1509216155.494064-out
	    	 * 3: 1509216295.494107-in, 1509216295.494107-out
	    	 */
			
			openStream(Paths.get("C:\\\\Users\\\\pedro\\\\Downloads\\\\audios\\\\Exemplo 1\\\\1509216131.494056-out.wav"), 9000);
			openStream(Paths.get("C:\\\\Users\\\\pedro\\\\Downloads\\\\audios\\\\Exemplo 1\\\\1509216131.494056-in.wav"), 9500);
	    	
		}catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	

	private static void openStream(Path path, int port) throws Exception{
    	final Socket cli = new Socket("localhost", port);
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
					BufferedReader br = new BufferedReader(new InputStreamReader(cli.getInputStream(), StandardCharsets.ISO_8859_1));
					String line = "";
			    	StringBuffer sb = new StringBuffer();
			    	while (!br.ready()) Thread.sleep(500);
			    	while((line = br.readLine())!=null) {
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
