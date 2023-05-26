package com.csefinalproject.github.multiplayer;

import org.apache.commons.cli.*;
import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;
import com.csefinalproject.github.multiplayer.behaviour.server.GameManager;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {

		// Create command line stuff
		CommandLine commandLine;
		CommandLineParser parser = new DefaultParser();

		// Add options
		Options options = CreateCommandLineOptions();

		// Parse the command line
		commandLine = parser.parse(options, args);

		// Get if the input from the user.
		Scanner console = new Scanner(System.in);

		// Get program type
		String programType = "";
		if (commandLine.hasOption("s")) {
			programType = "server";
		} else if (commandLine.hasOption("c")) {
			programType = "client";
		} else {
			programType = GetClientServerResponse(console);
		}

		switch (programType) {
			case "server" -> {
				System.out.println("Becoming a Server.");
				new GameManager();
			}
			case "client" -> {
				System.out.println("Becoming a Client.");
				new ClientManager();
			}
			default -> throw new Exception("Invalid program type of \"" + programType + "\". Must be a Server or Client.");
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

	/**
	 * Create the command line options
	 * @return Server and Client command line options
	 */
	private static Options CreateCommandLineOptions() {
		Options options = new Options();

		options.addOption(Option.builder("s")
				.required(false)
				.desc("Starts a server")
				.longOpt("server")
				.build()
		);
		options.addOption(Option.builder("c")
				.required(false)
				.desc("Starts a client")
				.longOpt("client")
				.build()
		);

		return options;
	}
}