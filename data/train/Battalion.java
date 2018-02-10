package in.problem.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * The Battalion class holds the units , speed (additional param) and its hierarchy
 *
 */
public class Battalion {


	/**
	 * 
	 * 	Ranking of battalions is dynamic , here it has  a level to determine the hierarchy . Suppose if the Ranking is based on battalion types,
	single combat unit size , attack power , etc , then writing Comparators to order in such a way would be an advantage .
	This class can be extended to store a individual battalion unit configuration , and hierarchy can be compared based on that .

     This class needs to be immutable to allow the get operation to work , since we should not
    update the key . If it needs to be updated then remove the key and create and insert
    value with the new key.
	 *
	 */
    public static final class Property implements Comparable<Property>
    {
        public final String name;
        private int hierarchy;



        private static Map<String, Property> types = new HashMap<String, Battalion.Property>();

        private Property(String name, int level, int speed)
        {
            this.name = name;
			this.hierarchy = level;
            this.speed = speed;
		}

        private Property(Property p)
        {
            this.name = p.name;
            this.hierarchy = p.hierarchy;
            this.speed = p.speed;

        }

        // addition properties . based on which the hierarchy can change
        private int speed;

        public int getSpeed()
        {
            return speed;
        }

        public int getHierarchy()
        {
			return this.hierarchy;
		}

        public static void initProperties()
        {
            final Property HORSE = new Property("Horse", 0, 3);
            final Property ELEPHANT = new Property("Elephant", 1, 2);
            final Property ARMOUREDTANK = new Property("Tank", 2, 1);
            final Property SLINGSHOT = new Property("SlingShot", 3, 0);
            types.put(HORSE.name, HORSE);
            types.put(ELEPHANT.name, ELEPHANT);
            types.put(ARMOUREDTANK.name, ARMOUREDTANK);
            types.put(SLINGSHOT.name, SLINGSHOT);
        }

        public static Property createNewProperty(int level, String name, int speed)
        {
            Property p = new Property(name, level, speed);
            types.put(name, p);
            return types.get(name);
        }

        public static Map<String, Property> getTypes()
        {
            return types;
        }

        @Override
        public String toString()
        {
            return "Property [name=" + name + ", hierarchy=" + hierarchy + ", speed=" + speed + "]";
        }

        @Override
        public int hashCode()
        {
            return this.hierarchy;
        }

        @Override
        public boolean equals(Object obj)
        {
            Property p = (Property) obj;
            return this.name.equals(p.name) && this.hierarchy == p.hierarchy && this.speed == p.speed;
        }

        @Override
        public int compareTo(Property o)
        {
            int i = 0;
            if (this.getHierarchy() - o.getHierarchy() == 0) {
                i = this.getSpeed() - o.getSpeed();
            } else
                i = this.getHierarchy() - o.getHierarchy();

            return i;
        }
		
	};

	private int units;	
	private Property hierarchy;
	
	//default for copy
	public Battalion(){
		
	}
	public Battalion(int units,Property h){
		this.units = units;
		this.hierarchy = h;
	}
	
	public boolean subtract(int units){
		if(this.units>=units){
			this.units-=units;
			return true;
		}
		else
			return false;
	}
	
	public boolean subtract(Battalion b){
		if(this.units>=b.units && this.hierarchy == b.getHierarchy()){
			this.units-=b.units;
			return true;
		}
		else
			return false;
	}
	
	public int battleBattalion(Battalion enemy , int ratio){
		if(enemy==null)
			return this.units;
		int ecount = (int)Math.ceil(((double)enemy.getUnits())*1/ratio);
		return this.units - ecount;
	}


	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public Property getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Property hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Battalion copy(){
		Battalion b = new Battalion();
		b.hierarchy = this.hierarchy;
		b.units = this.units;
		return b;
	}
	
    @Override
    public String toString()
    {
        return "Battalion [units=" + units + ", hierarchy=" + hierarchy + "]\n";
    }
	
	
}
