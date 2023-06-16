# User Manual

## Playing the game
* Use the keys `W`, `A`, `S`, and `D` to move around
* Press `T` to start chatting, and press `Enter` to send a message

## Starting a Server
There are two ways to tell the program that you want to run as a server. Launch options and just telling the program what you want to do.

### Launch arguments
Use these launch options to start the program as a server on a specific port.

`--server --port [port]` or `-s -p [port]`

### Telling the program what you want to do
* When the program asks, `Do you want to start the (S)erver or (C)lient?`
  * Type `Server` or simply `S`.
* Then once the program asks, `Give the port you want to open the server on`
  * Give a number between 1 and 32627.


## Starting a Client
There are also two ways to tell the program that you want to run as a client. Launch options and telling the program what you want to do.

### Launch Arguments
Use these launch options to start the program as a client connecting to a specific server on a port.

`--client --ip [IP Address] --port [port]`

### Telling the program what you want to do
* When the program asks, `Do you want to start the (S)erver or (C)lient?`
  * Type `Client` or simply `C`.
* Then once the program asks, `Give the IP of the server you want to connect to.`
  * Write out the IP of the server, ex. `192.168.0.1` 
* Finally, once the program asks, `Give the port of the server you want to connect to.`
  * Give a number between 1 and 32627.

# Launch Options
* `-s` or `--server`
  * Starts the program as a server
* `-c` or `--client`
  * Starts the program as a client
* `-u [Username]` or `--username [Username]`
  * **CLIENT ONLY**
  * The username of the player
* `-ip [IP to connect to]`
  * **CLIENT ONLY**
  * The IP for the client to connect to.
* `-p [Port]` or `--port [Port]`
  * The port to connect to or the port that the server should open on.