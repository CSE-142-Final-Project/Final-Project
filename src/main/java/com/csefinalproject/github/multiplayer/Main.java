package com.csefinalproject.github.multiplayer;

import org.apache.commons.cli.*;
import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;
import com.csefinalproject.github.multiplayer.behaviour.server.GameManager;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		CommandLineResponseGetter responseGetter = new CommandLineResponseGetter(args);

		switch (responseGetter.getProgramType()) {
			case "server" -> {
				System.out.println("Becoming a Server.");
				new GameManager(responseGetter.getIp(), responseGetter.getPort());
			}
			case "client" -> {
				System.out.println("Becoming a Client.");
				new ClientManager();
			}
			default -> throw new RuntimeException("Invalid program type of \"" + responseGetter.getProgramType() + "\". Must be either say " +
					"\"server\" or \"client\".");
		}
	}
}

class CommandLineResponseGetter {
	private String programType;
	private String ip;
	private short port;

	public CommandLineResponseGetter(String[] args) {
		// Get if the input from the user.
		Scanner console = new Scanner(System.in);

		// Create command line stuff
		CommandLine commandLine;
		CommandLineParser parser = new DefaultParser();

		// Add options
		Options options = CreateCommandLineOptions();

		// Parse the command line
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		System.out.println();
	}

	/**
	 * Uses the Scanner to get a response from the user.
	 * @param console The System.in scanner object.
	 * @return "client" or "server"
	 */
	private String GetResponse(Scanner console, String prompt, String errorMessage, String[] correctResponses) {
		// Might get stuck in permanent loop! Yay!
		while(true) {
			System.out.print(prompt);
			String response = console.nextLine().trim().toLowerCase();

			// Check if the response is correct
			if(correctResponses.length == 0) {
				return response;
			} else {
				for(int i = 0; i < correctResponses.length; i++) {
					if(response.equals(correctResponses[i])) {
						return correctResponses[i];
					}
				}
			}

			System.out.println(errorMessage);
		}
	}

	/**
	 * Create the command line options
	 * @return Server and Client command line options
	 */
	private Options CreateCommandLineOptions() {
		Options options = new Options();

		options.addOption(Option.builder("ip")
				.required(false)
				.desc("IP to connect to.")
				.build()
		).addOption(Option.builder("p")
				.required(false)
				.desc("Port to open on/connect to.")
				.longOpt("p")
				.build()
		).addOption(Option.builder("s")
				.required(false)
				.desc("Starts a server")
				.longOpt("server")
				.build()
		).addOption(Option.builder("c")
				.required(false)
				.desc("Starts a client")
				.longOpt("client")
				.build()
		);;

		return options;
	}

	public String getProgramType() {
		return programType;
	}

	public String getIp() {
		return ip;
	}

	public short getPort() {
		return port;
	}
}