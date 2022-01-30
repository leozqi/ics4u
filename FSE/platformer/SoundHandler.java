// ------------------------------------------------------------------------- //
// The SoundHandler class provides a global singleton instance from which    //
// objects may register and access different sound clips.                    //
//                                                                           //
// All methods are by author unless otherwise stated in method header.       //
//                                                                           //
// Package:  platformer                                                      //
// Filename: SoundHandler.java                                               //
// Author:   Leo Qi                                                          //
// Class:    ICS4U St. Denis                                                 //
// Date due: Jan. 30, 2022.                                                  //
// ------------------------------------------------------------------------- //

package platformer;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.io.*;
import java.net.URL;

public class SoundHandler {

	// Create only one "global" instance of SoundHandler
	private static SoundHandler inst = new SoundHandler(); // Store all sounds

	// Store sounds as within a hashmap so they can be accessed by key
	private HashMap<String, Clip> soundmap = new HashMap<String, Clip>(); 

	/**
	 * Stores all game-wide sound files.
	 *
	 * All objects may access sound files already stored with the
	 * appropriate String key, and register new keys that may overwrite
	 * existing keys. Playback is also possible with the `playSound` method.
	 *
	 * Data protection is achieved through use of getters / setters.
	 */
	private SoundHandler() { /* No need to instantiate */ }


	/**
	 * Get singleton SoundHandler object used by all game elements.
	 *
	 * Only necessary to change a Setting value, not to use a value; to
	 * just reference a value, static methods are available.
	 */
	public static SoundHandler get() {
		return inst;
	} /* End method get */


	/**
	 * Register a sound file corresponding to a specific key.
	 *
	 * This method uses lines of code from the in-class Sound.java example.
	 *
	 * If a key is already in use, it will be overwritten. Java's
	 * SoundTechnology only supports .WAV, .AIFF, and .AU files.
	 *
	 * <https://docs.oracle.com/javase/8/docs/technotes/guides/sound/index.html>
	 *
	 * For other file types, this method WILL RETURN FALSE.
	 *
	 * @param key String to use as key for future access
	 * @param filename Name of the sound file (in relative path `/resources/`
	 */
	public boolean register(String key, String filename) {
		Clip clip;
		try {
			// Get file relative to current classpath
			URL url = this.getClass().getResource("/resources/" + filename);

			// Create sound
			File sound = new File(url.toURI());
			/* BEGIN COPIED CODE */
			AudioInputStream stream = AudioSystem.getAudioInputStream(sound);
			clip = AudioSystem.getClip();
			clip.open(stream);
			/* END COPIED CODE */
		} catch (Exception e) {
			return false; // Failed to process sound
		}
		// No error; store clip
		synchronized(this) {
			this.soundmap.put(key, clip);
		}
		return true; // Success
	} /* End method register */


	/**
	 * Play selected sound.
	 *
	 * This method is synchronized so that the correct sound is made.
	 * The monitor used is from the SoundHandler class, not the clip,
	 * because of a thread race condition occuring when two sounds
	 * of the same key are played at the same time. A lock by clip will
	 * allow neither of them to play, freezing the game indefinitely.
	 * A synchronized lock on the SoundHandler will.
	 *
	 * @param key Key of the sound to play.
	 * @param looped Whether or not sound should loop continuously.
	 * @return true if successful, else false.
	 */
	public boolean playSound(String key, boolean looped) {
		Clip clip = this.soundmap.get(key); // Get registered clip
		if (clip == null) { return false; } // No key exists

		synchronized(this) {
			clip.setFramePosition(0);
			if (looped) {
				clip.loop(clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
		}

		return true;
	} /* End method playSound */


	/**
	 * Stop all currently playing sounds.
	 */
	public void stopSounds() {
		// Get hashmap values as an array
		for (Clip clip : this.soundmap.values()) {
			// Stop sounds, reset playback
			clip.stop();
			clip.setFramePosition(0);
		}
	} /* End method stopSounds */

} /* End class SoundHandler */
