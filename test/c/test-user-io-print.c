#include <stdio.h>
#include <string.h>
#include "user-io.h"

int main(int argc, char **argv)
{
  char *message = "Hi there";
  size_t len = strlen(message);

  // This is indeed an ugly hack .... 
  if (argc>1)
    {
      message = NULL;
    }

  int ret = printmsg(message);
  if (ret > 0 && (size_t)ret != len)
    {
      fprintf(stderr, "Uh oh, print message failed\n");
      return 1;
    }
  return 0;
}
