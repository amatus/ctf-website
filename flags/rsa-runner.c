#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

int main(int argc, char **argv) {
  pid_t pid;
  int status;

  fprintf(stderr, "real %d:%d effective %d:%d\n",
          getuid(), getgid(), geteuid(), getegid());

  pid = fork();
  if(pid < 0) {
    perror("fork failed");
  }
  if(0 == pid) {
    execlp("/flags/rsa.py", "rsa.py", NULL);
    perror("exec failed");
  }
  waitpid(pid, &status, 0);
  return 0;
}
