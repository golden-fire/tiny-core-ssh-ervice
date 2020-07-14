# tiny-core-ssh-ervice
a tiny ssh service help you execute comments on a remote server. The tiny library is able to:
* create a ssh connection (or ssh context)
* execute a list of commands in the same ssh context
* bring back the final result of the whole list of the commands

## prerequisite

A ssh public/private key pair has to been steup correctly between local server and remote server
* local server means the serve where your java problem is running
* remote server means the server where a list of command to be executed

## execute the junit tests

**./gradlew test -Dserver_ip=**[your server ip] **-Dserver_user=**[your server login] **-Dwait_time=**[ssh_connection timeout] **-Dtime_unit=**[timeout's time unit]

* server_ip is mandatory, it is the remote server you are going to connect
* server_user is mandatory. it is the remote user you are going to use on the remote server
* wait_time is optional, default value is 20, it is the maximum waiting time for you to finish all your remote commands
* time_unit is optional, default value is seconds (only supports seconds and minutes), it is the time unit for the waiting time


