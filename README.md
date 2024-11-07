# Conversation application

### Description
This application simulates a conversation between two talkers: A and B.

Flow of the conversation:
- Talker A sends a message to Talker B
- Talker B accepts the message and replies to Talker A
- Talker A accepts the reply from Object B

The above scenario repeats 10 times (i.e. Talker A sends 10 messages and receives 10 replies), after which the conversation between talkers ends.

The conversation can happen in two modes:
1. Both talkers reside in one process (with same PID).
2. Talkers reside in different processes (with different PIDs) and use socket connection for communication.

Below you can find the description of how to launch the application in each mode.

### Mode 1 - Conversation in single process (same PID)
In order to start the conversation in single process, run shell script with no args:
```shell
bash ./boot.sh
```
### Mode 2 - Conversation in different processes (different PIDs)
In order to start the conversation in separate processes, first run shell script with 'S' argument. It will start socket server with Talker B (respondent):
```shell
bash ./boot.sh S
```
After server with Talker B is started (you will see 'Server is started (PID <...>)' in the console), run shell script with 'C' argument to start socket client with Talker A (initiator): 
```shell
bash ./boot.sh C
```