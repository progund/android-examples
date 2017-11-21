#include <stdio.h>
#include "user-io.h"


int read_int(void)
{
  int i;
  scanf("%d", &i);
  return i;
}

int printmsg(char *msg)
{
  return fprintf(USER_IO_MSG_STREAM, "%s", msg);
}

