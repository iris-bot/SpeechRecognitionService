package br.com.irisbot.asr.ws;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.irisbot.asr.core.DetectSilence;

public class MySocketListener {
	
	
	public static void instance(final int key) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					String channel = key>=500?"cli":"agt";
					String callId = orchestrator.getCallId(key>=500?key-500:key);
					
					ServerSocket srv = new ServerSocket(9000+key);
					srv.setSoTimeout(30000);

					System.out.println("aguardando..."+srv.getLocalPort());
					
					Socket cli = srv.accept();
					cli.setSoTimeout(6000);
					
					System.out.println("aceito.."+srv.getLocalPort());
					
					InputStream is = cli.getInputStream();
					OutputStream os = cli.getOutputStream();
					DetectSilence ds = new DetectSilence(channel, callId);
					/*
					 * Fica rodando enquanto estiver aberto
					 */
					ds.detectSilenceFromStream(srv,is, os);
					
					orchestrator.releasePort(key);
					
				}catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
