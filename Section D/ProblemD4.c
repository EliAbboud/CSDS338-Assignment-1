#include <stdlib.h>

int main(){
  int i;
  for (i = 0; i < 10; i++)
     system("sleep 100");
}
//The PID of the c program is 2140
//The PID of the first system call is 2141
//The PID of the subsequent nine system calls are not immediately displayed because the synchronous execution
//I regain fg control after 1000 seconds
//1000 seconds until all spawned sleeps have completed
