# My Client-Server Protocol

### What transport protocol do we use ?

It uses TCP as layer 4 because we full state connexion. The server should memorize some informations in the conversation.

### How does the client find the server (addresses and ports) ?

The client find the server with his IP adress + listening port (also call the socket).

The server will be in a Docker container so IP 172.17.0.x and listening on the port 12345

### Who speaks first?

The client speaks first to open to connexion.

### What is the sequence of messages exchanged by the client and the server?

Example of sequence :

| Client        | Server   |
| :------------ | -------- |
| REQUEST,8,*,8 | -        |
| -             | REPLY,64 |
| REQUEST,3,+,3 | -        |
| -             | REPLY,6  |
| STOP          | -        |
| -             | STOP     |

### What happens when a message is received from the other party?

Parse the message, then execute what to do following our syntax.

### What is the syntax of the messages? How we generate and parse them?
Using BufferedInput and BufferedOutput to send and receive the message. Then it could parse it by line.

There 3 types of syntax message :

- REQUEST,operande,operator,operande
- REPLY,result
- STOP

Note : if the syntax isn't respected, the party who receive the wrong syntax message close the connexion without feedback message.

### Who closes the connection and when?

The client closes the connexion when he's done with his calculs by sending a STOP.

