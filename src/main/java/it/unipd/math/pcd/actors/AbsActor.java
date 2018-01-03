/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
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
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

package it.unipd.math.pcd.actors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Defines common properties of all actors.
 *
 * @author Alex Beccaro
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {
  
  /**
   * Executor of MessageRunnable tasks.
   */
  protected ExecutorService executor;

  /**
   * Self-reference of the actor.
   */
  protected ActorRef<T> self;

  /**
   * Sender of the current message.
   */
  protected ActorRef<T> sender;
  
  /** 
   * Creates a new executor for this actor messages.
   */
  public AbsActor() {
    executor = Executors.newSingleThreadExecutor();
  }

  /**
   * Sets the self-reference.
   *
   * @param self The reference to itself
   * @return The actor.
   */
  protected final Actor<T> setSelf(ActorRef<T> self) {
    this.self = self;
    return this;
  }
  
  /**
   * Adds a new message to the mailbox.
   * @param message The message to add
   */
  protected void newMessage(T message, ActorRef<T> from) {
    MessageRunnable mr = new MessageRunnable(message, from);
    executor.execute(mr);
  }
  
  /**
   * stops this actor from receiving new messages.
   */
  protected void stop() {
    // TODO: close executor when all tasks have been completed
  }
  
  /**
   * Defines a task that executes after a message has been received.
   * @author Becks
   */
  class MessageRunnable implements Runnable {

    private T message;
    private ActorRef<T> sender;
    
    public MessageRunnable(T message, ActorRef<T> sender) {
      this.message = message;
      this.sender = sender;
    }

    @Override
    public void run() {
      AbsActor.this.sender = sender;
      receive(message);
    }
    
  }
  
}
