package com.csefinalproject.github.multiplayer;

import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;
import com.csefinalproject.github.multiplayer.behaviour.server.GameManager;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
		// Get if the input from the user.
		Scanner console = new Scanner(System.in);
		String programType = GetClientServerResponse(console);

		switch (programType) {
			case "server" -> {
				System.out.println("Becoming a Server.");
				GameManager gameManager = new GameManager();
			}
			case "client" -> {
				System.out.println("Becoming a Client.");
				ClientManager clientManager = new ClientManager();
			}
			default -> throw new Exception("Invalid Program Type of " + programType + ". Must be a Server or Client.");
		}
	}

	/**
	 * Uses the Scanner to get if the person running the program wants it to be a server or client.
	 * @param console The System.in scanner object.
	 * @return "client" or "server"
	 */
	private static String GetClientServerResponse(Scanner console) {
		// Stuck in permanent loop!
		while(true) {
			System.out.print("Would you like to be the (S)erver or the (C)lient? ");
			String response = console.nextLine().trim().toLowerCase();

			if (response.equals("s") || response.equals("server")) {
				return "server";
			} else if (response.equals("c") || response.equals("client")) {
				return "client";
			} else {
				System.out.println("Answer must be (S) for Server or (C) for Client.");
			}
		}
	}
}