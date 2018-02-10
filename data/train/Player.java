package in.problem.entity;

import in.problem.entity.Battalion.Property;
import in.problem.substitution.rules.Rule;

import java.util.Comparator;

public class Player
{

    private String name;

    private Army army;

    private int ptoePowerRatio;

    //default constructor for copying
    public Player() {
		super();
	}

	public Player(String name)
    {
        this.name = name;
        this.ptoePowerRatio = 2;
        this.army = new Army();
    }

	public Player copy(){
		Player p = new Player();
		p.name = this.name;
		p.army = this.army.copy();
		p.ptoePowerRatio = this.ptoePowerRatio;
		return p;
	}
    /*
     * This is an initialization function for the main player army
     */
    public void prepareArmy()
    {
        // create battalions
        Battalion horse = new Battalion(100, Property.getTypes().get("Horse"));
        Battalion elephant = new Battalion(50, Property.getTypes().get("Elephant"));
        Battalion tank = new Battalion(10, Property.getTypes().get("Tank"));
        Battalion slingshot = new Battalion(5, Property.getTypes().get("SlingShot"));

        this.army.addBattalion(horse);
        this.army.addBattalion(elephant);
        this.army.addBattalion(tank);
        this.army.addBattalion(slingshot);

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Army getArmy()
    {
        return army;
    }

    public void setArmy(Army mainArmy)
    {
        this.army = mainArmy;
    }

    public int getPtoePowerRatio()
    {
        return ptoePowerRatio;
    }

    public void setPtoePowerRatio(int ptoePowerRatio)
    {
        this.ptoePowerRatio = ptoePowerRatio;
    }

    public void setArmyBehaviour(Comparator behaviour)
    {
        this.army.setBehaviour(behaviour);
    }

    public void setSubstitutionRule(Rule rule)
    {
        this.army.setSubstitutionRule(rule);
    }

    @Override
    public String toString()
    {
        return "Player [name=" + name + ", army=" + army + "]";
    }

}
