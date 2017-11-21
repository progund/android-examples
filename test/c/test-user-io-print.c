#include <stdio.h>
#include <string.h>
#include "user-io.h"

int main(void)
{
  char *message = "Hi there";
  size_t len = strlen(message);
  int ret = printmsg(message);
  if (ret > 0 && (size_t)ret != len)
    {
      fprintf(stderr, "Uh oh, print message failed\n");
      return 1;
    }
  return 0;
}
