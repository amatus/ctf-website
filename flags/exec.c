#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

char flag[32];

int main(int argc, char **argv) {
  int fd;
  pid_t pid;
  int status;

  fprintf(stderr, "real %d:%d effective %d:%d\n",
          getuid(), getgid(), geteuid(), getegid());

  fd = open("exec.flag", O_RDONLY);
  if(-1 != fd) {
    // load flag into memory where nobody will ever find it
    read(fd, flag, sizeof(flag));
  }
  // we'll even run something for you!
  if(argc != 2) {
    fprintf(stderr, "give me something to run\n");
    return -1;
  }
  pid = fork();
  if(pid < 0) {
    perror("fork failed");
  }
  if(0 == pid) {
    // drop privs
    seteuid(getuid());
    setegid(getgid());
    // exec tears down the address space, goodbye flag!
    execlp(argv[1], argv[1], NULL);
    perror("exec failed");
  }
  waitpid(pid, &status, 0);
  return 0;
}
