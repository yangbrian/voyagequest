package battle;

import org.newdawn.slick.Animation;
import voyagequest.DoubleRect;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * User: Edmund
 * Date: 6/30/13
 */
public class Projectile extends BattleEntity {
    public int contactDamage;

    public Animation explosionAnimation;

    private boolean hasExploded;


    public Projectile(DoubleRect location,
                      DoubleRect collision,
                      Allegiance projectileAllegiance) {
        super(location, collision);
        this.entityAllegiance = projectileAllegiance;
        hasExploded = false;
    }

    public void act(int delta)
    {
        //The Projectile has no script, so it must move forward on its own.

        //Process collisions too
        super.act(delta);

        //I mean.. it's a bullet. It won't have much of an animation
        updateAnimation(delta);

        attemptMove(velocityX, velocityY, delta);

        //If we're on the explosion animation and we're on the last frame, then we're basically done.
        //This may or may not work.
        if (hasExploded && explosionAnimation.getFrame() == explosionAnimation.getFrameCount())
        {
            markForDeletion();
            System.out.println("Marking this bullet for deletion!");

        }
    }

    public void processCollision(LinkedList<BattleEntity> collisions)
    {
        //If this projectile isn't fit for damaging, then stop now.
        //The query to the collisionTree will always return itself, so if the
        //size of the list is 0, then it's just itself --> return;
        if (isGhost || hasExploded || collisions.size() == 1) return;

        //Iterate through the LinkedList of BattleEntities.
        ListIterator<BattleEntity> iter = collisions.listIterator();

        boolean explodes = false;

        while (iter.hasNext())
        {
            BattleEntity collided = iter.next();

            //If the target is a ghost, then we don't hit them.
            if (collided.isGhost) continue;
            if (collided.equals(this)) continue;

            boolean causeDamage = false;
            switch(this.entityAllegiance)
            {
                case FRIENDLY:
                    if (collided.entityAllegiance.equals(Allegiance.UNFRIENDLY)) causeDamage = true;
                    break;
//                case UNFRIENDLY:
//                    causeDamage = (collided.entityAllegiance == Allegiance.FRIENDLY);
//                    break;
//                case DANGER:
//                    causeDamage = (collided.entityAllegiance == Allegiance.FRIENDLY ||
//                                   collided.entityAllegiance == Allegiance.UNFRIENDLY);
//                    break;
            }

            if (causeDamage)
            {

                collided.health -= contactDamage;
                explodes = true;
            }
        }

        if (explodes)
        {
            //Let's play the explosion animation
            hasExploded = true;
            currentAnimation = explosionAnimation;
            resetAnimationTiming();
        }


    }

}
