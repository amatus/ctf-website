all: exec format rsa-runner simple

exec: exec.c
	gcc -o exec exec.c
	chown root:exec exec
	chmod 2755 exec

format: format.c
	gcc -o format format.c
	chown root:format format
	chmod 2755 format

rsa-runner: rsa-runner.c
	gcc -o rsa-runner rsa-runner.c
	chown root:rsa rsa-runner
	chmod 2755 rsa-runner

simple: simple.c
	gcc -o simple simple.c
	chown root:simple simple
	chmod 2755 simple
