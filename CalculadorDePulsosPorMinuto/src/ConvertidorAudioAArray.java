import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ConvertidorAudioAArray {
	
	private static final String PULSACIONES_TEST_WAV = "c:/Users/David/Desktop/pulsacion-test.wav";
	
	private String fileName;
	
	public ConvertidorAudioAArray(String fileNameAudio) {
		this.fileName = fileNameAudio;
	}

	public short[] convertirAudio() throws UnsupportedAudioFileException, IOException {
		File myFile = new File(PULSACIONES_TEST_WAV);
		byte[] samples;

		AudioInputStream is = AudioSystem.getAudioInputStream(myFile);
		DataInputStream dis = new DataInputStream(is);
		try {
		    AudioFormat format = is.getFormat();
		    samples = new byte[(int)(is.getFrameLength() * format.getFrameSize())];
		    dis.readFully(samples);
		}
		finally {
		    dis.close();
		}
		
		return ConvertidorByteArray.toShortArray(samples);
	}
	
	public void remuestrearAudio() {
		File inputFile = new File(this.fileName);
		File outputFile = new File(PULSACIONES_TEST_WAV);
        AudioInputStream stream=null;

        try {
            stream=AudioSystem.getAudioInputStream(inputFile);
            stream = convertSampleRate(600.0f, stream);

            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, outputFile);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public AudioInputStream convertSampleRate(float fSampleRate,AudioInputStream sourceStream)
    {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(),
        		fSampleRate,
        		sourceFormat.getSampleSizeInBits(),
        		sourceFormat.getChannels(),
        		sourceFormat.getFrameSize(),
        		fSampleRate,sourceFormat.isBigEndian()
        );
        
        return AudioSystem.getAudioInputStream(targetFormat,sourceStream);
    }
}
