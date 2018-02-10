package in.problem.handler;

import in.problem.entity.Army;

import in.problem.entity.Battalion;
import in.problem.entity.Battalion.Property;
import in.problem.entity.Player;

import java.util.List;
/**
 * 
 * This class must take care of Property updation and addition. Suppose if the Horse Property hierarchy or speed were changed then
 * the whole ranking of Battalions must change for every player army . This is done by adding a new Property with the updated
 * values and removing the old property .
 *
 */
public class PropertyManager
{
    private PlayerManager playerManager;

    public PropertyManager(PlayerManager playerManager)
    {
        super();
        this.playerManager = playerManager;
    }

    // changes the hierarchy by adding and removing the property if it exists
    // and resorting the army battalions
    public void updateProperty(Property oldProperty, int level, int speed)
    {

        List<Player> players = playerManager.getAllPlayers();
        Property newProperty = Property.createNewProperty(level, oldProperty.name, speed);
        boolean change = false;
        for (Player player : players) {
            Army army = player.getArmy();
            Battalion b = army.getMap().get(oldProperty);
            if(b!=null){
                change = true;
                army.removeBattalion(oldProperty);
                b.setHierarchy(newProperty);
                army.addBattalion(b);
            }
        }
        if(change){
            Property.getTypes().remove(oldProperty);
        }
    }

    public Property createNewProperty(String name, int level, int speed)
    {
        Property newProperty = Property.createNewProperty(level, name, speed);
        return newProperty;
    }
}
