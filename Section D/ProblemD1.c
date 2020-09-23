#include <stdio.h>

int main(){
  char name[] = "Eli Abboud";
  char* c = name;
  while (*c){
    putchar(*c++);
    putchar('\n');
  }
}
