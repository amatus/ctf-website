#include <fcntl.h>
#include <stdio.h>

// this one is really simple
int main() {
	int fd;
	char buff[32];
	int len;

	fd = open("simple.not.the.flag", O_RDONLY);
	if(-1 == fd) {
		perror("open failed");
		return -1;
	}
	len = read(fd, buff, sizeof(buff));
	if(len < 0) {
		perror("read failed");
		return -1;
	}
	write(1, buff, len);
	putchar('\n');
	return 0;
}
