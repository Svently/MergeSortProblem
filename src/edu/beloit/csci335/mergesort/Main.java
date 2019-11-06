package edu.beloit.csci335.mergesort;

import java.util.concurrent.ThreadLocalRandom;

public class Main {
    static final int ARRAY_LENGTH = 10000;
    static final long ARRAY_MAX_VALUE = 100000000;
    static long[] myArray;
    public static void main(String[] args) {
        myArray = new long[ARRAY_LENGTH];
	    for (int i = 0; i < ARRAY_LENGTH; i++) {
            myArray[i] = ThreadLocalRandom.current().nextLong(ARRAY_MAX_VALUE);
        }
	    System.out.println(myArray);
    }
    procedure mergesort( var a as array )
    if ( n == 1 ) return a

    var l1 as array = a[0] ... a[n/2]
    var l2 as array = a[n/2+1] ... a[n]

    l1 = mergesort( l1 )
    l2 = mergesort( l2 )

    return merge( l1, l2 )
 end procedure

 procedure merge( var a as array, var b as array )

    var c as array
    while ( a and b have elements )
       if ( a[0] > b[0] )
          add b[0] to the end of c
          remove b[0] from b
       else
          add a[0] to the end of c
          remove a[0] from a
       end if
    end while
    
    while ( a has elements )
       add a[0] to the end of c
       remove a[0] from a
    end while
    
    while ( b has elements )
       add b[0] to the end of c
       remove b[0] from b
    end while
    
    return c
 	
 end procedure
  
}
