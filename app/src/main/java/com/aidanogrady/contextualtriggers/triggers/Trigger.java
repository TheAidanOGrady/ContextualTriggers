package com.aidanogrady.contextualtriggers.triggers;

/**
 * The basic trigger interface. The purpose of a trigger is to prompt the user to perform some
 * behaviour. A trigger will determine the best time to notify the user based on some contextual
 * information it has access to.
 *
 * @author Aidan O'Grady
 */
interface Trigger{
    /**
     * Returns the complexity of this trigger.
     *
     * @return the trigger complexity
     *
     */
    int getComplexity();

    /**
     * Notify the user when the contextual information indicates this is the best time to do so.
     */
    void notifyUser();

    Boolean isTriggered();
}
