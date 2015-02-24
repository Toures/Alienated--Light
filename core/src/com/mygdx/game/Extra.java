package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Extra {

	//Sound Utilities
	void playSound(String sound) {
		Sound audio = Gdx.audio.newSound(Gdx.files.internal(sound));
		audio.play();
		audio.dispose();
	}
	
	void playSound(String sound, float volume) {
		Sound audio = Gdx.audio.newSound(Gdx.files.internal(sound));
		audio.play(volume);
		audio.dispose();
	}
	
	void playSound(String sound, float volume, float pitch, float pan) {
		Sound audio = Gdx.audio.newSound(Gdx.files.internal(sound));
		audio.play(volume, pitch, pan);
		audio.dispose();
	}
}
