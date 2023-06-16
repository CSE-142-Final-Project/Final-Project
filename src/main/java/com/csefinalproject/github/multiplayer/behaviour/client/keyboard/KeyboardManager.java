package com.csefinalproject.github.multiplayer.behaviour.client.keyboard;

import com.buildingjavaprograms.drawingpanel.PanelInput;

/**
 * This class manages the keyboard inputs and allows for easy chatting to happen
 */
public class KeyboardManager {
	private final PanelInput input;
    private final StringBuilder partialSentence;
    private String text;

	/**
	 * This constructor is used to create a new Keyboard Manager
	 * @param input The PanelInput class to use to receive keyboard input
	 */
	public KeyboardManager(PanelInput input) {
		this.input = input;
        this.partialSentence = new StringBuilder();
		this.text = "";
	}

	/**
	 * Runs through all the latest key presses and
	 * @return A boolean that says if we're done chatting or not.
	 */
    public boolean updateChat() {
		// Run through all the keys
		while (this.input.keyAvailable()) {
			// Read the latest key
			char nextKeyPress = this.input.readKey();

			if (nextKeyPress == '\b') {
				// If we have a backspace key pressed, delete the character at the end
				if (partialSentence.length() > 0)
				{
					partialSentence.setLength(partialSentence.length() - 1);
				}
			} else if (nextKeyPress == '\n') {
				// If we have a newline character, save the StringBuilder and delete all the existing text.
				String sentence = partialSentence.toString();
				this.text = sentence;

				// If these are both equal, just delete all the text
				if (sentence.contentEquals(partialSentence))
				{
					partialSentence.delete(0, partialSentence.length());
				}

				// We're done chatting!
				return true;
			} else {
				// Add the latest key press to the end of the StringBuilder
				partialSentence.append(nextKeyPress);
			}
		}

		// Update text in real time
		this.text = partialSentence.toString();

		// We're not done chatting!
		return false;
    }

	/**
	 * Get the current text that the KeyboardManager is getting
	 * @return The string of text currently being typed
	 */
	public String getText() {
		return text;
	}
}
