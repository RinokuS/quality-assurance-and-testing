/**
 * Copyright (c) 2009 ISP RAS.
 * 109004, A. Solzhenitsina, 25, Moscow, Russia.
 * All rights reserved.
 *
 * $Id$
 * Created on Jan 15, 2016
 */

package root.pow;

/**
 * @author Victor Kuliamin
 */
public class Power {

  public long pow(int a, int b) {
    long r = 1;
    
    while(b > 0) {
      if((b&1) != 0) {
        r *= a;
      }
      a *= a;
      b >>= 1;
    } 
    
    return r % 4294967296L;
  }
}
