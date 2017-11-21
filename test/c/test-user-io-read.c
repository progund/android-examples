#include <stdio.h>
#include "user-io.h"

int main(void)
{
  int ret;
  printf("Type an integer value: ");
  ret = read_int();
  printf("You typed: %d\n", ret);

  return 0;
}
