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
 */

package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A map-based implementation of the actor system.
 *
 * @author Alex Beccaro
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActorSystem implements ActorSystem {

  /**
   * Associates every active Actor with an identifier.
   */
  private Map<ActorRef<?>, Actor<?>> actors = new HashMap<>();

  @Override
  public ActorRef<? extends Message> actorOf(Class<? extends Actor> actor, ActorMode mode) {

    // ActorRef instance
    ActorRef<?> reference;
    try {
      // Create the reference to the actor
      reference = this.createActorReference(mode);
      // Create the new instance of the actor
      Actor actorInstance = ((AbsActor) actor.newInstance()).setSelf(reference);
      // Associate the reference to the actor
      actors.put(reference, actorInstance);

    } catch (InstantiationException | IllegalAccessException e) {
      throw new NoSuchActorException(e);
    }
    return reference;
  }

  @Override
  public ActorRef<? extends Message> actorOf(Class<? extends Actor> actor) {
    return this.actorOf(actor, ActorMode.LOCAL);
  }

  /**
   * Creates a new ActorRef specifying ActorMode
   * @param mode the mode of the Actor creation
   * @return the reference to an Actor with specified mode
   */
  protected abstract ActorRef createActorReference(ActorMode mode);
  
  /**
   * Returns the Actor referenced by ref.
   * @param ref the reference to requested Actor
   * @return the Actor referenced by ref
   */
  protected Actor getActor(ActorRef ref) {
    Actor actor = actors.get(ref);
    if (actor == null) {
      throw new NoSuchActorException("There is not such actor in the actor system");
    }
    return actor;
  }
  
  @Override
  public void stop(ActorRef<?> actor) {
    ((AbsActor) getActor(actor)).executor.shutdown();
    actors.remove(actor);
  }
  
  @Override
  public void stop() {
    for (Entry<ActorRef<? extends Message>, Actor<? extends Message>> actor : actors.entrySet()) {
      ((AbsActor) actor.getValue()).executor.shutdown();
    }
  }
  
}