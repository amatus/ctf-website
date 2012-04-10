Simple
======

Files
-----

    -rwxr-sr-x 1 root simple  5266 Apr  6 02:08 simple
    -rw-r--r-- 1 root root     363 Apr  6 02:08 simple.c
    -rw-r----- 1 root simple    33 Apr  6 02:00 simple.flag
    -rw-r--r-- 1 root root      33 Apr  6 01:59 simple.not.the.flag

Synopsis
--------

This program opens a file named "simple.not.the.flag" and prints its contents.
The file containing the flag is named "/flags/simple.flag".

Vulnerability
-------------

The open call looks in the current directory for a file named
"simple.not.the.flag" and will follow symlinks, rather than opening the
intended file named "/flags/simple.not.the.flag" which the unprivileged user
has no control over.

Exploit
-------

Change the current directory to one in which we have write permissions.

    cd ~

Create a symlink named "simple.not.the.flag" that points to the flag file we
want.

    ln -s /flags/simple.flag simple.not.the.flag
    
Run the program from this directory.

    /flags/simple

Exec
====

Files
-----

    -rwxr-sr-x 1 root exec    6477 Apr  5 22:50 exec
    -rw-r--r-- 1 root root     883 Apr  5 22:50 exec.c
    -rw-r----- 1 root exec      33 Apr  5 00:32 exec.flag

Synopsis
--------

This program:

1. Opens the flag file "/flags/exec.flag".
2. Reads it into memory.
3. Forks a child process.
4. Drops privileges in the child process before executing a user-specified
program.
5. Waits for the child to exit.

Vulnerability
-------------

The program forgets to close the open file descriptor to the flag file before
execing the user-specified program in step 4. Step 2 is simply misdirection
supported by comments in the code.

Exploit
-------

Write a program which performs the following operations on file descriptor
number 3:

1. Seeks to the beginning of the file.
2. Reads the contents of the file and prints it out.

Run the exec program given the name of the our exploit program to execute.

RSA
===

Files
-----

    -rwxr-xr-x 1 root root     217 Apr  6 00:41 make-keys.rsa
    -rw-r----- 1 root rsa       33 Apr  5 23:19 rsa.flag
    -rw-r----- 1 root rsa     1600 Apr  6 00:48 rsa.keys
    -rwxr-xr-x 1 root root    1966 Apr  6 23:12 rsa.py
    -rwxr-sr-x 1 root rsa     5794 Apr  6 20:19 rsa-runner
    -rw-r--r-- 1 root root     468 Apr  6 20:19 rsa-runner.c

Synopsis
--------

The make-keys.rsa program is a python script which generates the rsa.keys
keystore containing a private and public key for "Alice" and a public key for
"Bob".
The rsa-runner program is a setgid wrapper for rsa.py, since interpred scripts
cannot be run setgid.
The rsa.py program reads the flag file and the keystore into memory and then
starts a TCPServer to handle requests. When a request is received it is
unpickled into a python object and the "request" property is examined.
If the request property is the string "start" a new python object with the
following properties is pickled and sent to the remote peer:

    name = "Alice"
    request = "get_flag"
    keyid = MD5(DER encoding of Alice's public key)
    signature = Sig(keyid + ":" + request, Alice's private key)

If the request property is the string "get_flag" the message must pass these
tests before the flag is sent to the remote peer:

1. The the name property must exist in the keystore.
2. The keyid property must match the MD5 hash of a key in the keystore.
3. The name must not be the string "Alice".
4. The signature is validated against the key in the keystore which matches the
keyid property.

Vulnerability
-------------

The name property is not covered by the signature, nor is it checked against
keyid, nor is it used to select the signature verification key, so it can be
modified without detection leaving a "valid" get_flag request.

Exploit
-------

Using the python REPL, connect to the rsa.py server, send the "start" request,
and unpickle the response.

    >>> import socket, pickle
    >>> sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    >>> sock.connect(('localhost', 6666))
    >>> class Request:
    ...    pass
    ...
    >>> msg = Request()
    >>> msg.request='start'
    >>> sock.sendall(pickle.dumps(msg))
    >>> buf = sock.recv(1024)
    >>> msg2 = pickle.loads(buf)
    
Reconnect to the server (this has something to do with the way I coded rsa.py),
modify the name property of the reponse to "Bob" and send it back.

    >>> sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    >>> sock.connect(('localhost', 6666))
    >>> msg2.name='Bob'
    >>> sock.sendall(pickle.dumps(msg2))
    
Consume the response and unpickle it.

    >>> buf = sock.recv(1024)
    >>> pickle.loads(buf)

Lottery
=======

Files
-----

    -rw-r----- 1 root 1666   33 Apr  5 22:33 lottery.flag

Synopsis
--------

This is simply a flag file that can only be read by group 1666.

Vulnerability
-------------

The CTF website allows competitors to create a user and group for themselves.

Exploit
-------

Write a script to create users and groups until group number 1666 is assigned.
