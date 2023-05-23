package com.buildingjavaprograms.drawingpanel;/*
 * =====================================================================
 * PanelInput.java
 * Simple class for handling all "game" interactions for an interactive
 * app built upon the DrawingPanel class provided by Reges & Stepp.
 * (http://www.buildingjavaprograms.com/drawingpanel/DrawingPanel.java)
 *
 * author: Alec McTavish, Innovation Lab High School, Bothell, WA
 * version: 1.0, 2022/12/11
 * (make sure to also update version string in Javadoc header below!)
 * =====================================================================
 *
 * The PanelInput class provides a simple interface for getting
 * synchronous/"polling" access to a DrawingPanel's mouse and keyboard
 * information and events. Having created a DrawingPanel, a client of the
 * class simply constructs a PanelInput with the DrawingPanel as its source
 * and then polls for and responds to events and state as desired.
 * See JavaDoc comments below for more information.
 */

import java.util.*;
import java.awt.Point;

/**
 *
 * PanelInput is a class for accessing keyboard and mouse events generated
 * by a DrawingPanel object. No knowledge of events, listeners, etc. required.
 * Intended for use with the {@code DrawingPanel} class, written by
 * Reges &amp; Stepp. Here's where you can find the {@code DrawingPanel}
 * <a href="http://www.buildingjavaprograms.com/drawingpanel/DrawingPanel.java">code</a>
 * and
 * <a href="http://www.buildingjavaprograms.com/drawingpanel/javadoc/">documentation</a>
 *
 * <H2>Description:</H2>
 *
 * <p>
 * The {@code PanelInput} class provides a simple interface for getting
 * synchronous/polling access to a {@code DrawingPanel}'s mouse and keyboard
 * information and events. Having created a {@code DrawingPanel}, a client of
 * the class simply constructs a {@code PanelInput} object with the
 * {@code DrawingPanel} as its source and then polls for and responds to
 * events and state as desired.
 * </p>
 *
 * <p>
 * The intent is to provide a reasonable way for students to access input
 * generated via user interactions with a {@code DrawingPanel} window,
 * using only the coding knowledge needed for creating said window and
 * its contents, plus the basics of Java {@code if/else} statements.
 * Later, when students learn how to create their own custom classes/objects,
 * they can replace their use of this code with appropriate classes and
 * associated event listeners and lambdas, as described in Reges's &amp;
 * Stepp's {@code DrawingPanel}
 * <a href="http://www.buildingjavaprograms.com/drawingpanel/javadoc/">documentation</a>.
 * </p>
 *
 * <H2>Example basic usage:</H2>
 *
 * <p>
 * Here is a canonical example of creating a {@code UserInput} object and using it
 * to see and respond to mouse and keyboard interactions with a {@code DrawingPanel}
 * </p>
 *
 * <pre>
 * // basic usage example
 * DrawingPanel panel = new DrawingPanel();
 * PanelInput input = new PanelInput(panel);
 * Graphics g = panel.getGraphics();
 * // if/when needed, can clear out queued click and/or keyboard events before monitoring
 * input.flushAllEvents();
 * // ^^ also available: input.flushKeyboardEvents(), input.flushMouseEvents()
 * for(;;) {
 *   // check for and respond to mouse and keyboard state and events
 *   // Some examples given below; not an exhaustive list
 *   if (input.mouseDrag()) {
 *     // mouse moved within DrawingPanel window, with one of its buttons depressed
 *     // write code here to respond to mouse being dragged
 *     // e.g. draw line from previous mouse position to current mouse position
 *     // to get current mouse position within DrawingPanel window:
 *     Point curr = input.getMouseLoc();
 *   }
 *   // ^^ other mouse state available: input.mouseDown(), input.mouseInDisplay()
 *   if (input.keyDown('s')) {
 *     // state check: 's' key is currently down (remains true until key released)
 *     // write code here to respond to that state (e.g. move player in video game)
 *   }
 *   if (input.keyAvailable()) {
 *     // keystroke (key down then up) detected while DrawingPanel window had focus
 *     // write code here to respond to keystroke
 *     // to get the character that was typed (from the queue of pending keystrokes)
 *     char typed = input.readKey();
 *   }
 *   // ^^ note: a program will generally use just one of the above key check approaches
 *   if (input.clickAvailable()) {
 *     // mouse click detected within DrawingPanel window
 *     // write code here to respond to mouse being clicked
 *     // to get the location of the mouse click (from the queue of pending mouse clicks)
 *     Point clickLoc = input.readClick();
 *   }
 *   // good practice to cede control for a moment before checking again for events
 *   panel.sleep(20); // sleep for 20 msec, can be as little as 1 msec
 * }

 * </pre>
 *
 *
 * @author Alec McTavish (Innovation Lab HS, Bothell, WA)
 * @version 1.0, 2022/12/11
 */
@SuppressWarnings("JavadocReference")
public class PanelInput {
  // delay [ms] between checks for an event, if no event available
  private static final int EVENT_POLLING_INTERVAL = 50;
  // maximum backlog of events to keep in any queue (keyboard, mouse)
  private static final int MAX_QUEUE_LENGTH = 40;

  // DrawingPanel being monitored
  private DrawingPanel source;

  // used for cross-thread safety code
  private Object lock;

  // mouse stuff
  private Point currentMousePos;
  private boolean mouseInDisplay;
  private boolean mouseDown;
  private boolean dragging;
  private Queue<Point> mouseClicks;

  // keyboard stuff
  private Queue<Character> keysTyped;
  private HashSet<Character> keysDown;

	/**
	 * Constructs an object to monitor a {@code DrawingPanel}'s user input state/events
	 * @param panel the {@code DrawingPanel} object to monitor
	 */
  public PanelInput(DrawingPanel panel) {
    this.source = panel;
    this.lock = new Object();
    this.currentMousePos = new Point();
    this.mouseDown = this.dragging = this.mouseInDisplay = false;
    this.mouseClicks = new LinkedList<Point>();
    this.keysTyped = new LinkedList<Character>();
    this.keysDown = new HashSet<Character>();

    this.createKeyboardHandlers();
    this.createMouseHandlers();
  }

  // methods to set up event handlers / listeners
  // ============================================

  private void createKeyboardHandlers() {
    this.source.onKeyDown(keyCode -> {
      synchronized(this.lock) {
        // DrawingPanel gives char as code, so shift matters
        this.keysDown.add(keyCode);
        this.keysTyped.add(keyCode);
        trimQueue(this.keysTyped);
      }
    });
    this.source.onKeyUp(keyCode -> {
      synchronized(this.lock) {
        // remove both the lower- and upper-case letters associated with the
        // key being released. For non-letters, just ends up asking for the
        // same character to be released twice. Not a problem - remove handles
        // that just fine.
        this.keysDown.remove(Character.toLowerCase(keyCode));
        this.keysDown.remove(Character.toUpperCase(keyCode));
        // ^^ DrawingPanel gives char as code, so if shift state changes
        // while key is down, keyCode will be different when key goes up,
        // and a simple remove(keyCode) would not result in the key being
        // from keysDown. Code above  addresses this problem for all letter
        // keys, but not for the many keys where shifted and unshifted
        // characters are completely different (e.g. '1'/'!').
        //
        // For a robust system, would need DrawingPanel code changed to send
        // keyCode instead of keyChar, or would need mapping here that gives
        // (normal, shifted) values for each non-letter key. Explicitly
        // deciding here to make letters work reliably, and not worry about
        // the others. Students wishing for more robust behavior can choose
        // to implement the above mapping in their own code, or move away
        // from using this simplified input-handling framework.
      }
    });
  }

  private void createMouseHandlers() {
    this.source.onMouseEnter((x, y) -> this.mouseInDisplay = true);
    this.source.onMouseExit((x, y) -> this.mouseInDisplay = false);
    this.source.onMouseMove((x, y) -> {
      this.currentMousePos = this.scaledMousePos(x, y);
      this.dragging = false;
    });
    this.source.onMouseDrag((x, y) -> {
      this.currentMousePos = this.scaledMousePos(x, y);
      this.dragging = true;
    });
    this.source.onMouseClick((x, y) -> {
      synchronized(this.lock) {
        this.mouseClicks.add(this.scaledMousePos(x, y));
        trimQueue(this.mouseClicks);
      }
    });
    this.source.onMouseDown((x, y) -> {
       this.mouseDown = true;
    });
    this.source.onMouseUp((x, y) -> {
      this.dragging = this.mouseDown = false;
    });
  }

  // no need to keep a huge backlog of events, as is almost certain
  // to happen if a program is not checking & consuming them...
  private static void trimQueue(Queue<?> queue) {
    while (queue.size() > MAX_QUEUE_LENGTH) {
      queue.remove();
    }
  }

  // methods to clear state / event backlog
  // ======================================

  /**
   * Clears out all pending mouse events, so that
   * the next one that occurs will be the one you see
   */
  public void flushMouseEvents() {
    this.mouseClicks.clear();
  }

  /**
   * Clears out all pending keyboard events, so that
   * the next one that occurs will be the one you see
   */
  public void flushKeyboardEvents() {
    this.keysTyped.clear();
  }

  /**
   * Clears out all pending mouse and keyboard events, so
   * that the next ones that occur will be the ones you see
   */
  public void flushAllEvents() {
    this.flushMouseEvents();
    this.flushKeyboardEvents();
  }

  // Mouse info methods
  // ==================

  // returns given location, scaled properly for current DrawingPanel zoom
  private Point scaledMousePos(int x, int y) {
    int zoom = this.source.getZoom();
    return new Point(x / zoom, y / zoom);
  }

  /**
   * gets the current mouse location within the DrawingPanel
   * @return the current mouse location
   */
  public Point getMouseLoc() {
    return new Point(this.currentMousePos);
  }

  /**
   * checks whether the mouse is currently within the DrawingPanel's display
   * @return true if mouse is within DrawingPanel's display
   */
  public boolean mouseInDisplay() {
    return this.mouseInDisplay;
  }

  /**
   * checks if mouse is currently dragging (moving with mouse button pressed) within the DrawingPanel
   * @return true if the mouse is currently dragging within the DrawingPanel
   */
  public boolean mouseDrag() {
    return this.dragging;
  }

  /**
   * checks if any mouse button is currently down. (Does not distinguish between left/right/center)
   * @return true if a mouse button is currently down
   */
  public boolean mouseDown() {
    return this.mouseDown;
  }

  /**
   * Checks for pending mouse click events. You will usually call
   * {@link readClick} after this method returns true.
   * @return true if there are any mouse click events available for processing.
   */
  public boolean clickAvailable() {
    synchronized(this.lock) {
      return !this.mouseClicks.isEmpty();
    }
  }

  /**
   * Reads the first click from the queue of available click events. Note that
   * this is a blocking call -- it will not return until there is an event to
   * process. You will usually want to call {@link clickAvailable} first, and
   * call this method only when you know there is a click event to read
   * @return location of click
   */
  public Point readClick() {
    while (!this.clickAvailable()) {
      this.source.sleep(EVENT_POLLING_INTERVAL);
    }
    synchronized(this.lock) {
      return this.mouseClicks.remove();
    }
  }

  // Keyboard info methods
  // =====================

  /**
   * Checks for pending key pressed events. You will usually call
   * {@link readKey} after this method returns true.
   * @return true if there are any key pressed events available for processing
   */
  public boolean keyAvailable() {
    return !this.keysTyped.isEmpty();
  }

  /**
   * Reads the first key from the queue of available key pressed events. Note
   * that this is a blocking call -- it will not return until there is an event
   * to process. You will usually want to call {@link keyAvailable} first, and
   * call this method only when you know there is a key pressed event to read.
   * @return character that would be typed by the key pressed
   */
  public char readKey() {
    while (!this.keyAvailable()) {
      this.source.sleep(EVENT_POLLING_INTERVAL);
    }
    return this.keysTyped.remove();
  }

  /**
   * Checks whether the given key is currently down. Note that shift matters
   * - key press of 'A' key will be recorded as 'a' if shift key is not down,
   * and as 'A' if it is. Key press on '1' key will be recorded as '1' without
   * shift, and as '!' with shift.
   * @param key the key (character) being checked
   * @return true if key is currently down
   */
  public boolean keyDown(char key) {
    return this.keysDown.contains(key);
  }

  // Diagnostics code
  // ================

  /**
   * gets relevant information about the events/state being monitored by this
   * PanelInput object.
   * @return a list of Strings containing debug information
   */
  public ArrayList<String> getDebugInfo() {
    ArrayList<String> info = new ArrayList<String>();
    String status = "mouse is ";
    if (!this.mouseInDisplay) {
      status += "not ";
    }
    status += "in display ";

    if (this.mouseInDisplay) {
      status += " at (" + this.currentMousePos.x + ", "
        + this.currentMousePos.y + ")";
    }
    if (this.dragging) {
      status += " -- dragging";
    }
    info.add(status);
    info.add("\nKeys down: " + this.keysDown.toString());
    return info;
  }
}