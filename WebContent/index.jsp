<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Your First WebSocket!</title>
        <!-- <script language="javascript" type="text/javascript">
        	var client = new WebSocket('ws://localhost:8080/AudioWebService/audio-api');
        	
        	audioContext = window.AudioContext || window.webkitAudioContext;
        	var context = new audioContext();
	    	var source = context.createBufferSource();
	    	console.log ('getting ready');
	    	var scriptNode = context.createScriptProcessor(2048, 1, 1);
	    	
	    	client.onopen = function() {
        	    if (!navigator.getUserMedia)
        	      navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia ||
        	    navigator.mozGetUserMedia || navigator.msGetUserMedia;
        	    
        	    navigator.mediaDevices.getUserMedia({audio:true})
        	    .then(function(stream) {
        	    	audioInput = context.createMediaStreamSource(stream);
        	    	console.log(audioInput);
        	    	console.log(stream);
        	    	//source.buffer = buffer;
        	    	console.log ('recording');
        	    }).catch(function(err) {
        	    	alert('Error capturing audio.');
        	    });
        	    var recording = false;
        	    window.startRecording = function() {
        	      recording = true;
        	    }

        	    window.stopRecording = function() {
        	      recording = false;
        	      client.close();
        	    }


        	    scriptNode.onAudioProcess = function(e){ // e = Audio Processing Event
					console.log ('sending');
					var left = e.inputBuffer.getChannelData(0);
					client.send(left);
					//client.send(convertoFloat32ToInt16(left));
				}

        	    function convertoFloat32ToInt16(buffer) {
        	      var l = buffer.length;
        	      var buf = new Int16Array(l)

        	      while (l--) {
        	        buf[l] = buffer[l]*0xFFFF;    //convert to 16 bit
        	      }
        	      return buf.buffer
        	    }
        	};        	          
        	client.onmessage = function (evt) 
            { 
               var received_msg = evt.data;
               alert("Message is received...");
            };
				
            client.onclose = function()
            { 
               // websocket is closed.
               alert("Connection is closed..."); 
            };
        </script> -->
        <script type="text/javascript">
        	var client = new WebSocket('ws://localhost:8080/AudioWebService/audio-api');
        	
        	client.onopen = function() {
        		console.log("olar");
        	}
			var webaudio_tooling_obj = function () {
			
			    var audioContext = new AudioContext();			
			    var BUFF_SIZE_RENDERER = 2048
			
			    var audioInput = null,
			    microphone_stream = null,
			    gain_node = null,
			    script_processor_node = null,
			    script_processor_analysis_node = null,
			    analyser_node = null;
			
			    if (!navigator.getUserMedia)
			        navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia ||
			    navigator.mozGetUserMedia || navigator.msGetUserMedia;
			
			    if (navigator.getUserMedia){
			
			        navigator.getUserMedia({audio:true}, 
			            function(stream) {
			                start_microphone(stream);
			            },
			            function(e) {
			                alert('Error capturing audio.');
			            }
			            );
			
			    } else { alert('getUserMedia not supported in this browser.'); }
			
			    // ---
			
			    function show_some_data(given_typed_array, num_row_to_display, label) {
			
			        var size_buffer = given_typed_array.length;
			        var index = 0;
			
			        console.log("__________ " + label);
			
			        if (label === "time") {
			
			            for (; index < num_row_to_display && index < size_buffer; index += 1) {
			
			                var curr_value_time = (given_typed_array[index] / 128) - 1.0;
			
			                //console.log(curr_value_time);
			            }
			
			        } else if (label === "frequency") {
			
			            for (; index < num_row_to_display && index < size_buffer; index += 1) {
			
			                //console.log(given_typed_array[index]);
			            }
			
			        } else {
			
			            throw new Error("ERROR - must pass time or frequency");
			        }
			    }
			
			    function process_microphone_buffer(event) {
			
			        var i, N, inp, microphone_output_buffer;
			        microphone_output_buffer = event.inputBuffer.getChannelData(0);
			        console.log("microphone_output_buffer");
			        client.send(microphone_output_buffer);
			    }
			
			    function start_microphone(stream){
					var myBuffer;
			        gain_node = audioContext.createGain();
			        gain_node.connect( audioContext.destination );
			
			        var source = audioContext.createBufferSource();
			        
			        microphone_stream = audioContext.createMediaStreamSource(stream);
			        microphone_stream.connect(gain_node); 
			
			        script_processor_node = audioContext.createScriptProcessor(BUFF_SIZE_RENDERER, 1, 1);
			        script_processor_node.connect(gain_node);
			        script_processor_node.onaudioprocess = process_microphone_buffer;
					
			        microphone_stream.connect(script_processor_node);
			        // --- enable volume control for output speakers
			
			       /* document.getElementById('volume').addEventListener('change', function() {
			
			            var curr_volume = this.value;
			            gain_node.gain.value = curr_volume;
			
			            console.log("curr_volume ", curr_volume);
			        });
			
			        script_processor_analysis_node = audioContext.createScriptProcessor(2048, 1, 1);
			        script_processor_analysis_node.connect(gain_node);
			
			        analyser_node = audioContext.createAnalyser();
			        analyser_node.smoothingTimeConstant = 0;
			        analyser_node.fftSize = 2048;
			
			        microphone_stream.connect(analyser_node);
			
			        analyser_node.connect(script_processor_analysis_node);
			
			        var buffer_length = analyser_node.frequencyBinCount;
			
			        var array_freq_domain = new Uint8Array(buffer_length);
			        var array_time_domain = new Uint8Array(buffer_length);
			
			        console.log("buffer_length " + buffer_length);
			
			        script_processor_analysis_node.onaudioprocess = function(audioProcessingEvent) {
			        	console.log(audioProcessingEvent);
			            // get the average for the first channel
			            analyser_node.getByteFrequencyData(array_freq_domain);
			            analyser_node.getByteTimeDomainData(array_time_domain);
			            
			
			            // draw the spectrogram
			            if (microphone_stream.playbackState == microphone_stream.PLAYING_STATE) {
			            	audioContext.decodeAudioData(audioProcessingEvent, function(buffer) {
			            	    myBuffer = buffer;
			            	    console.log("myBuffer");
			            	    console.log(myBuffer);
			            	}, function(e){"Error with decoding audio data" + e.err});
			            	//client.send(myBuffer);
			            	//show_some_data(array_freq_domain, 5, "frequency");
			                //show_some_data(array_time_domain, 5, "time");
			            } 
			        };*/
			    }
			
			}(); //  webaudio_tooling_obj = function()
			client.onmessage = function (evt) { 
               var received_msg = evt.data;
               alert("Message is received...");
            };
				
            client.onclose = function() { 
               // websocket is closed.
               alert("Connection is closed..."); 
            };
			</script>
</head>
<body>

    <p>Volume</p>
    <input id="volume" type="range" min="0" max="1" step="0.1" value="0.5"/>

</body>
</html>