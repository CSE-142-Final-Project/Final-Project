package com.csefinalproject.github.multiplayer;

import com.csefinalproject.github.multiplayer.util.ProgramType;
import org.apache.commons.cli.*;
import com.csefinalproject.github.multiplayer.behaviour.client.ClientManager;
import com.csefinalproject.github.multiplayer.behaviour.server.GameManager;

import java.util.Scanner;

/**
 * This class is responsible for getting command line input and starting the program
 */
public class Main {

	public static final int TPS = 20;

	public static void main(String[] args) {
		// Get if the input from the user.
		Scanner console = new Scanner(System.in);

		CommandLineResponseGetter responseGetter = new CommandLineResponseGetter(args, console);

		switch (responseGetter.getProgramType()) {
			case SERVER -> {
				System.out.println("Becoming a Server.");
				new GameManager(responseGetter.getPort());
			}
			case CLIENT -> {
				System.out.println("Becoming a Client.");
				new ClientManager(responseGetter.getUsername(), responseGetter.getIp(), responseGetter.getPort());
			}
			default -> throw new RuntimeException("Invalid program type of \"" + responseGetter.getProgramType() + "\". Must be either be " +
					"\"server\" or \"client\".");
		}
	}
}

/**
 * Simple class for getting the command line arguments
 */
class CommandLineResponseGetter {
	private final ProgramType programType;
	private String username;
	private String ip;
	private final short port;

	public CommandLineResponseGetter(String[] args, Scanner console) {
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

		// Get server/client
		if (commandLine.hasOption("s")) {
			this.programType = ProgramType.SERVER;
		} else if (commandLine.hasOption("c")) {
			this.programType = ProgramType.CLIENT;
		} else {
			this.programType = AskToBecomeServerOrClient(console);
		}

		// Get IP
		if (commandLine.hasOption("ip")) {
			this.ip = commandLine.getOptionValue("ip");
		} else if (this.programType == ProgramType.CLIENT) {
			this.ip = AskForIP(console);
		}

		// Get port
		if (commandLine.hasOption("p")) {
			try {
				this.port = Short.parseShort(commandLine.getOptionValue("p"));
			} catch (NumberFormatException e) {
				throw new RuntimeException(e);
			}
		} else {
			this.port = AskForPort(console);
		}

		// Get username
		if(commandLine.hasOption("u")) {
			this.username = commandLine.getOptionValue("u");
		} else if (this.programType == ProgramType.CLIENT) {
			this.username = AskForUsername(console);
		}
	}

	/**
	 * Asks the person in the console if they want to be a server or client and then returns the program type.
	 * @param console The System.in Scanner
	 * @return The program type, a server or client
	 */
	private ProgramType AskToBecomeServerOrClient(Scanner console) {
		String response = GetResponse(console,
				"Do you want to be a (S)erver or a (C)lient? ",
				"Answer must be (S) for Server or (C) for Client.",
				new String[] {
						"s", "server",
						"c", "client"
				}
		);

		if (response.equals("s") || response.equals("server")) {
			return ProgramType.SERVER;
		} else if (response.equals("c") || response.equals("client")) {
			return ProgramType.CLIENT;
		} else {
			throw new IllegalArgumentException("GetResponse is supposed to return a \"server\" or \"client\" here! AAAAAA");
		}
	}

	/**
	 * Ask for the username of the client
	 * @param console The System.in Scanner
	 * @return A string with the username of the client
	 */
	private String AskForUsername(Scanner console) {
		return GetResponse(console,
				"What is your username? ",
				"",
				new String[] { }
		);
	}

	/**
	 * Ask for the IP that the client wants to connect to.
	 * @param console The System.in Scanner
	 * @return A string with the IP of the server
	 */
	private String AskForIP(Scanner console) {
		return GetResponse(console,
				"Give the IP of the server you want to connect to. ",
				"",
				new String[] { }
		);
	}

	/**
	 * Ask for the port of the server
	 * @param console The System.in Scanner
	 * @return A short with the port of the server.
	 */
	private short AskForPort(Scanner console) {
		String prompt;
		switch(this.programType) {
			case CLIENT -> prompt = "Give the port of the server you want to connect to. ";
			case SERVER -> prompt = "Give the port you want to open the server on. ";
			default -> throw new IllegalStateException("Unexpected value: " + this.programType);
		}

		String response = GetResponse(console,
				prompt,
				"",
				new String[] { }
		);

		try {
			return Short.parseShort(response);
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		}
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
				for (String correctResponse : correctResponses) {
					if (response.equals(correctResponse)) {
						return correctResponse;
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
				.hasArg()
				.desc("IP to connect to.")
				.build()
		).addOption(Option.builder("p")
				.required(false)
				.hasArg()
				.desc("Port to open on/connect to.")
				.longOpt("port")
				.build()
		).addOption(Option.builder("s")
				.required(false)
				.desc("Starts a server,")
				.longOpt("server")
				.build()
		).addOption(Option.builder("c")
				.required(false)
				.desc("Starts a client.")
				.longOpt("client")
				.build()
		).addOption(Option.builder("u")
				.required(false)
				.hasArg()
				.desc("Username of the client.")
				.longOpt("username")
				.build()
		);

		return options;
	}

	public ProgramType getProgramType() {
		return programType;
	}

	public String getUsername() {
		return username;
	}

	public String getIp() {
		return ip;
	}

	public short getPort() {
		return port;
	}
}