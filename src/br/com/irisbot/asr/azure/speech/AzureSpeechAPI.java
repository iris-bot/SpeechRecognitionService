package br.com.irisbot.asr.azure.speech;

import java.io.File;
import java.nio.file.Files;

import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.irisbot.asr.ws.RestClient;
import br.com.irisbot.asr.ws.RestClient.Response;

public class AzureSpeechAPI {
  //public static HashMap<String, String> transcriptions = new HashMap<>();

  public static String main(File file) throws Exception  {
	String trans = "";
	try {
		
		RestClient cli = RestClient.synchronousRequest("https://speech.platform.bing.com/speech/recognition/conversation/cognitiveservices/v1?language=pt-BR&format=simple");
		cli.addRequestHeader("Ocp-Apim-Subscription-Key", "a2f32f5de7364beb80571338febd4a96");
		cli.addRequestHeader("Content-type", "audio/wav; codec=audio/pcm; samplerate=16000");
		cli.addRequestHeader("Accept", "application/json");
		Response resp = cli.doPost(Files.readAllBytes(file.toPath()));
		
		JSONObject json = new JSONObject(new JSONTokener(resp.getContent()));
		trans = json.getString("DisplayText");
	} catch (Exception e) {
		e.printStackTrace();
	}
    return trans.trim();
  }

}
