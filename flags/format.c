#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void hax(char *shellcode) {
	int stack;
	printf(shellcode, &stack);
}

int main(int argc, char **argv) {
	char *buf, *p;
	int i;

	if(argc != 2) {
		printf("What? Are you chicken?\n");
		return -1;
	}
	// remove %n from format string, we're not *that* stupid
	buf = strdup(argv[1]);
	for(p = argv[1], i = 0; p[0]; ++p) {
		if(p[0] == '%' && p[1] == 'n') {
			++p;
			if(p[0]) {
				continue;
			} else {
				break;
			}
		}
		buf[i++] = p[0];
	}
	buf[i] = '\0';
	hax(buf);
	free(buf);
	putchar('\n');
	return 0;
}
