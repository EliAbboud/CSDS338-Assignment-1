#include <stdlib.h>

int main(){
  int i;
  for (i = 0; i < 10; i++)
     system("sleep 100 &");
}
//The PIDs are: 1051, 1053, 1055, 1057, 1059, 1061, 1063, 1066, 1068, 1070
//I regain fg control immediately
//100 seconds until all spawned sleeps have completed
