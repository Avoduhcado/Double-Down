package core.audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.easyogg.OggClip;

import core.Theater;

public class Track {

	/** .OGG audio clip */
	private OggClip clip;
	/** Name of audio clip. Used to identify in Ensemble */
	private String name;
	/** Current volume of clip */
	private float volume;
	/** If clip should loop */
	private boolean loop;
	/** Timer for fading in/out */
	private float fading;
	/** Fade in time */
	private float fadeIn;
	/** Fade out time */
	private float fadeOut;
	
	/**
	 * Track to be used for ambiance or background effects.
	 * 
	 * @param ref File name of audio clip to load
	 */
	public Track(String ref) {
		try {
			// Load clip
			clip = new OggClip(new FileInputStream(System.getProperty("resources") + "/music/" + ref + ".ogg"));
			name = ref;
			volume = 1f;
			loop = true;
		} catch (FileNotFoundException e) {
			System.err.println("Audio track: " + ref + " was not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Track to be used for ambiance or background effects.
	 * Support for fading in, adjustable volume, and looping.
	 * 
	 * @param ref File name of audio clip to load
	 * @param volume Volume of audio clip
	 * @param fadeIn Time in seconds for track to fade in to full volume
	 * @param loop True if clip should loop
	 */
	public Track(String ref, float volume, float fadeIn, boolean loop) {
		try {
			// Load clip
			clip = new OggClip(new FileInputStream(System.getProperty("resources") + "/music/" + ref + ".ogg"));
			name = ref;
			this.volume = volume;
			// Set fade in timer
			if(fadeIn != 0) {
				this.fadeIn = fadeIn;
				// If fading in, it will use master volume to stop
				this.volume = 0f;
				fading = fadeIn;
			}
			this.loop = loop;
		} catch (FileNotFoundException e) {
			System.err.println("Audio track: " + ref + " was not found!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update any fading settings
	 */
	public void update() {
		// If track should be fading an Ensemble isn't muted
		if(fading != 0 && Ensemble.get().getMasterVolume() > 0f) {
			// Fading in
			if(fading > 0) {
				// Adjust fade in timer
				fadeIn += Theater.getDeltaSpeed(0.025f);
				// Adjust volume
				clip.setGain(volume += (((Ensemble.get().getMasterVolume() != 1f ? Ensemble.get().getMasterVolume() : volume) / fading) * Theater.getDeltaSpeed(0.025f)));
				// Check if fading has ended
				if(fadeIn >= fading) {
					fading = 0;
					fadeIn = 0;
					clip.setGain(1f);
				}
			// Fading out
			} else if(fading < 0) {
				// Adjust fade out timer
				fadeOut -= Theater.getDeltaSpeed(0.025f);
				// Adjust volume
				clip.setGain(volume += (((Ensemble.get().getMasterVolume() != 1f ? Ensemble.get().getMasterVolume() : volume) / fading) * Theater.getDeltaSpeed(0.025f)));
				// Check if fading has ended
				if(fadeOut <= fading || clip.getGain() == 0f) {
					fading = 0;
					fadeOut = 0;
					clip.setGain(0f);
				}
			}
		}
	}
	
	/**
	 * Play this track.
	 */
	public void play() {
		// If current volume is greater than master volume
		// Play at master volume instead
		if(Ensemble.get().getMasterVolume() < volume)
			clip.setGain(Ensemble.get().getMasterVolume());
		else
			clip.setGain(volume);
		
		if(loop)
			clip.loop();
		else
			clip.play();
	}
	
	/**
	 * Pause this track.
	 */
	public void pause() {
		clip.pause();
	}
	
	/**
	 * Resume this track.
	 */
	public void resume() {
		if(clip.isPaused())
			clip.resume();
	}
	
	/**
	 * Stop this tracks.
	 */
	public void stop() {
		clip.stop();
	}
	
	/**
	 * Called if master volume was changed.
	 * 
	 * @param masterVolume of Ensemble
	 */
	public void adjustVolume(float masterVolume) {
		// If current volume is greater than master volume
		// Use master volume instead
		if(masterVolume < volume) {
			clip.setGain(masterVolume);
		} else
			clip.setGain(volume);
	}
	
	/**
	 * 
	 * @return Track's audio clip
	 */
	public OggClip getClip() {
		return clip;
	}
	
	/**
	 * 
	 * @return Track name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return Fade in timer
	 */
	public float getFadeIn() {
		return fadeIn;
	}
	
	/**
	 * 
	 * @param fadeIn time to fade in in seconds
	 */
	public void setFadeIn(float fadeIn) {
		fading = fadeIn;
	}
	
	/**
	 * 
	 * @return Fade out timer
	 */
	public float getFadeOut() {
		return fadeOut;
	}
	
	/**
	 * 
	 * @param fadeOut time to fade out in seconds
	 */
	public void setFadeOut(float fadeOut) {
		fading = fadeOut;
	}
	
	/**
	 * 
	 * @return True if track is looping
	 */
	public boolean isLoop() {
		return loop;
	}
	
	/**
	 * 
	 * @param loop true to make track loop
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
}
