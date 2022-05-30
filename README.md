# jdbc-example
Simple examples of JDBC usage

## Running MySQL docker container

To use this examples project, we need MySQL running somewhere. Preferably in a container.

    docker run -p 3306:3306 --name jdbc-examples -e MYSQL_ROOT_PASSWORD=<PASSWORD> -d mysql --default-authentication-plugin=mysql_native_password -h 127.0.0.1

The above command will initialize a container and setup MySQL. The default user is 'root' and its password is the one specified in the arguments.
