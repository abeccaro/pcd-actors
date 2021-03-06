/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Alex Beccaro
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 */

package it.unipd.math.pcd.actors;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of a local actor reference.
 *
 * @author Alex Beccaro
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActorRef<T extends Message> implements ActorRef<T> {
  
  private int id;
  private AbsActorSystem as;
  
  private static AtomicInteger counter = new AtomicInteger(0);
  
  private Lock lock = new ReentrantLock();
  
  /**
   * Sets actor system reference and internal counter.
   * @param as the actor system
   */
  public AbsActorRef(AbsActorSystem as) {
    this.as = as;
    id = counter.getAndIncrement();
  }

  @Override
  public int compareTo(ActorRef other) {
    AbsActorRef absOther = (AbsActorRef) other;
    if (absOther != null) {
      return id - absOther.id;
    }
    return id;
  }

  @Override
  public void send(T message, ActorRef to) {
    AbsActor receiver = (AbsActor) as.getActor(to);
    receiver.newMessage(message, this);
  }

}