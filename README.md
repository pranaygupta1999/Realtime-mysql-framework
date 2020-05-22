# Realtime Mysql Framework
This is a java framework which provides you to handle the changes in your database at realtime.

## How it works
The framework has a server that listens to the changes in the general log file of the mysql and fires the listener associated with the updated.

The framework provides an interface with functions such as onUpdate, onSelect etc... You can override these functions to handle the changes and register the implemented interface with the framework. The framework will automatically call the function associated with the update.

## Usage
- Initialize the Mysql data directory
    ```console
        foo@bar:~$ mysqld --initialize --data-dir="datadirectory"
    ```
- Start the mysql daemon(mysql server)
    ```console
        foo@bar:~$ filename="your log file name for mysql"
        foo@bar:~$ datadir="your mysql data directory"
        foo@bar:~$ mysqld --datadir="$datadir" --general-log --general-log-file="$filename" 
    ```
- Start the Realtime Server provided and pass the general log file location as the parameter. Example: `java Server general_log_file_name`
    ```console
        foo@bar:~$ cd ./generated
        foo@bar:~$ file="$datadir/$filename"
        foo@bar:~$ java Server "$file" || kill -term `pidof mysqld`
    ```
- Import the ServerConnect java file in your program and implement the interface DatabaseEventListener. `See Demo.java`
    ```java
        import com.pranay.realtimedatabase.DatabaseEventListener;
        import com.pranay.realtimedatabase.ServerConnect;
        .
        .
        .
        ServerConnect conn = new ServerConnect();
            conn.setDatabaseChangeEventListener(new DatabaseEventListener() {...} );
        conn.startDatabaseChangeListenerService();
    ```
## QuickStart
I have provided some quickstart bash scripts to start the realtime server as well as mysql server with general logging enabled.
- Launch the `quickstart.sh` and follow the steps

### Other Scripts
- *compile.sh* Compile all the java files
- *runserver.sh* Start the realtime server