#include<stdio.h>
#include<stdlib.h>
#include<sys/time.h>

// NOTE: This is not a 1-on-1 translation of the Scala code

// Structure for ResizableArray data
typedef struct {
  int size;
  int elemCount;
  int* array;
} ResizableArray;


// Methods for ResizableArray
void init(ResizableArray* this) {
  this->size = 4;
  this->elemCount = 0;
  this->array = (int *)malloc(this->size * sizeof(int));
}

void finalize(ResizableArray* this) {
  free(this->array);
}

void extend(ResizableArray* this) {
  if (this->elemCount == this->size) {
    int pos = 0;
    int* newArray = (int *) malloc(2 * this->size * sizeof(int));
    while (pos < this->size) {
      newArray[pos] = this->array[pos];
      pos++;
    }
    free(this->array);
    this->array = newArray;
    this->size *= 2;
  }
}

void add(ResizableArray* this, int elem) {
  extend(this);
  this->array[this->elemCount++] = elem;
}

void reverse(ResizableArray* this) {
  int pos = 0;
  int tmp1, tmp2;
  while(pos * 2 < this-> elemCount) {
    tmp1 = this->array[pos];
    tmp2 = this->array[this->elemCount - pos - 1];
    this->array[pos] = tmp2;
    this->array[this->elemCount - pos - 1] = tmp1;
    pos++;
  }
}

void contains(ResizableArray* this) {
  // not implemented
}

int lenght(ResizableArray* this) {
  return this->elemCount;
}


// Main method for benchmark
int main(int argc, char** argv) {
  int testSize = 1000000;
  int i;
  ResizableArray array;
  struct timeval t1, t2;
  double time;
  int exp, exps = 50; // experiments

  // 1. Create large array, counting time
  exp = 0;
  time = 0.0;
  while (exp < exps) {
    gettimeofday(&t1, NULL);
    init(&array);
    i = 0;
    while(i < testSize) {
      add(&array, i);
      i++;
    }
    gettimeofday(&t2, NULL);
    time += (t2.tv_sec - t1.tv_sec) * 1000.0;
    time += (t2.tv_usec - t1.tv_usec) / 1000.0;
    exp++;
    if (exp != exps) finalize(&array);
  }
  printf("insert %f ms\n", time/exps);
 
  // 2. Reverse it, counting time
  exp = 0;
  time = 0.0;
  while (exp < exps) {
    gettimeofday(&t1, NULL);
    reverse(&array);
    gettimeofday(&t2, NULL);
    time += (t2.tv_sec - t1.tv_sec) * 1000.0;
    time += (t2.tv_usec - t1.tv_usec) / 1000.0;
    exp++;
  }
  printf("reverse %f ms\n", time/exps);
  finalize(&array);

  return 0;
}
