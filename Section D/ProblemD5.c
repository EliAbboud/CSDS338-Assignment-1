#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(){
  int i;
  char filename[] = "out.a.txt";
  remove(filename);
  for (i = 0; i < 1000; i++)
      system("printf A >> out.a.txt");
}
//In chronological order, the times for each run are:
//3.213s, 3.028s, 3.083s, 2.991s, 3.068s, 2.992s, 3.018s, 2.969s, 3.047s, and 3.173s.
//So, it is indeed slowest on the first run.
