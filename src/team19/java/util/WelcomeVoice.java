package team19.java.util;

import java.util.HashMap;
import java.util.Map;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class WelcomeVoice extends Thread {
	private String defaultSentence = ",Welcome";
	private String VisiterName = "";
	public static String VOICENAME = "kevin16";
	public static Map<String, WelcomeVoice> map = new HashMap<String, WelcomeVoice>();

	public WelcomeVoice(String s) {
		this.setVisiterName(s);

	}

	public static void addToMap(String s1) {
		if (!WelcomeVoice.map.containsKey(s1)) {
			WelcomeVoice.map.put(s1, new WelcomeVoice(s1));
			WelcomeVoice.map.get(s1).start();
			// System.out.println(s1+" starts");
		} else {
			// welcomeVoice.map.get(s1).run();
			// System.out.println(s1+" exists");
		}
	}

	@Override
	public void run() {
		Voice voice;
		VoiceManager vm = VoiceManager.getInstance();
		voice = vm.getVoice(VOICENAME);
		voice.setStyle("business");

		try {
			voice.allocate();
			voice.speak(this.getVisiterName() + defaultSentence);
//			System.out.println(" thread sleep");
			Thread.sleep(10000);
//			System.out.println(" thread awake");
			map.remove(this.getVisiterName());
//			System.out.println(" thread remove");
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			voice.deallocate();
		}

	}

	public String getVisiterName() {
		return VisiterName;
	}

	public void setVisiterName(String visiterName) {
		VisiterName = visiterName;
	}

}
