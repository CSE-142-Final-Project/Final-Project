package com.csefinalproject.github.multiplayer.behaviour.client.keyboard;

import com.buildingjavaprograms.drawingpanel.PanelInput;

//public class KeyboardManager {
//	private static final KeyboardManager instance;
//	private final PanelInput input;
//
//	public KeyboardManager(PanelInput input) {
//		this.input = input;
//	}
//
//	public void Update()
//	{
//		this.input.flushKeyboardEvents();
//		while (this.input.keyAvailable())
//		{
//			char nextKeyPress = this.input.readKey();
//
//			if (nextKeyPress == '\b')
//			{
//				if (_partialCommand.Length > 0)
//				{
//					// Something about this bothers me
//					// I can't seem to say why
//					// However this is how im supposed to do it
//					// So i guess if it works it works
//					_partialCommand.Length--;
//				}
//			}
//			else if (nextKeyPress == "\n")
//			{
//				if (isError)
//				{
//					_partialCommand.Clear();
//					isError = false;
//					continue;
//				}
//				try
//				{
//					String command = _partialCommand.ToString();
//					_commandHistory.Add(command);
//					_sysem.ParseCommand(command);
//					if (command == _partialCommand.ToString())
//					{
//						_partialCommand.Clear();
//					}
//				}
//				catch (Exception e)
//				{
//					_partialCommand.Clear();
//					isError = true;
//					_partialCommand.Append($"<color=\"{errorColor}\">"+e.Message);
//				}
//			}
//			else
//			{
//				if (isError)
//				{
//					_partialCommand.Clear();
//					isError = false;
//				}
//				_partialCommand.Append(nextKeyPress);
//			}
//		}
//		_text.SetText(_partialCommand.ToString());
//		_screen.Draw();
//	}
//
//	public PanelInput getInput() {
//		return input;
//	}
//}
