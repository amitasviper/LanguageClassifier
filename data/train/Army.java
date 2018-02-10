package in.problem.entity;

import in.problem.entity.Battalion.Property;
import in.problem.substitution.rules.ByHierarchy;
import in.problem.substitution.rules.Rule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * 
 * This class holds the Battalions for specific player in a key value pair
 * map.The "behaviour" property of the class specifies the ranking order of the
 * battalions . The default is by the hierarchy value of each Battalion type.
 * The substitution rule specifies the values based on which the battalion
 * substitution should occur .
 *
 */
public class Army {

	private TreeMap<Property, Battalion> map;
	private Comparator<Property> behaviour;
	private Rule substitutionRule;

	public Army() {
		// default behaviour
		behaviour = new ByHierarchy();
		map = new TreeMap<Property, Battalion>(); // behaviour
		this.substitutionRule = new Rule(-1, 1, 2);
	}

	public Army(Player p, Comparator b, Rule rule) {
		map = new TreeMap<Property, Battalion>(); // b
		this.substitutionRule = rule;
		this.behaviour = b;
	}

	public Army copy() {
		Army a = new Army();
		a.map = new TreeMap<>();
		// cannot use pullAll since the previous map would also get updated if
		// new map key's value is updated
		for (Property p : this.map.keySet()) {
			a.map.put(p, this.map.get(p).copy());
		}
		a.behaviour = this.behaviour;
		a.substitutionRule = this.substitutionRule.copy();
		return a;
	}

	public TreeMap<Property, Battalion> getMap() {
		return map;
	}

	public void setMap(TreeMap<Property, Battalion> map) {
		this.map = map;
	}

	public boolean addBattalion(Battalion b) {
		if (b != null) {
			this.map.put(b.getHierarchy(), b);
			return true;
		} else
			return false;

	}

	public boolean removeBattalion(Property p) {
		if (p != null) {
			return this.map.remove(p) != null;
		}
		return false;
	}

	/**
	 * 
	 * Subtracts the army variables from the current map with the same Battalion
	 * type and no ratio
	 */
	public Army subtract(Army army) {
		Army newArmy = this.copy();
		for (Property h : army.getMap().keySet()) {
			newArmy.map.get(h).subtract(army.map.get(h));
		}
		return newArmy;
	}

	/**
	 * 
	 * @param enemyArmy
	 * @param ptoePowerRatio
	 * </br>
	 *            This function battles same battalion groups of 2 players . It
	 *            then assigns the remaining units of the battalion of main
	 *            player (after this player to enemy battalion ratio has been
	 *            applied) to the deploy unit. If any units are -ve , it means
	 *            that there was insufficient units to battle and so is
	 *            substituted and balanced.
	 * 
	 * 
	 * @return Army
	 */
	public Army battleArmy(Army enemyArmy, int ptoePowerRatio) {
		Army deployUnit = new Army();
		// 1.Calculate the remaining units first
		for (Property h : Property.getTypes().values()) {
			Battalion mine = this.map.get(h);
			Battalion enemy = enemyArmy.map.get(h);
			if (mine == null) {
				mine = new Battalion(0, h);
			}
			// Calculate the remaining units after battling battalions
			int remainingUnits = mine.battleBattalion(enemy, ptoePowerRatio);
			deployUnit.updateBattalion(mine.getHierarchy(), remainingUnits);

		}

		// 2.Substitute now since only after processing all the battalion
		// battles we can swap.
		// This is because we may need to get units from higher hierarchy ,
		// which won't be processed until after the current.
		try {

			deployUnit.substituteBattalions();
		} catch (Exception e) {
			return null;
		}

		// 3. The deploy unit contains the remaining units . Subtract from the
		// main army to get the actual deployed unit
		deployUnit = this.subtract(deployUnit);

		return deployUnit;
	}

	/**
	 * 
	 * @param i - current requesting battalion index
	 * @param boundary - the range of looking
	 * @param inc - direction of looking
	 * @param requiredUnits
	 * @param ratio - substitution ratio
	 * @param properties - the current army available battalions
	 * </br>
	 * 
	 * @return The required units for the requesting Battalion . If 0 , then there were no units found .
	 * 
	 */
	private int findASubstitute(int i, int boundary, int inc, int requiredUnits, double ratio,
			ArrayList<Property> properties) {
		if ((inc > 0 && i > boundary) || (inc < 0 && i < boundary))
			return 0;
		Battalion b = this.map.get(properties.get(i));
		//the required units from another battalion should have ratio applied.
		requiredUnits = (int) (requiredUnits * ratio);
		//if the battalion doesn't have enough units
		if (b.getUnits() <= 0) {
			//we look else where but the required units must consider this battalion also,
			requiredUnits += Math.abs(b.getUnits());
			int val = findASubstitute(i + inc, boundary, inc, requiredUnits, ratio, properties);
			int temp = Math.abs(b.getUnits());
			if (val >= b.getUnits()) {
				b.setUnits(0);
			}
			val -= temp;
			return (int) (val / ratio);
		} else {
			//if not enough units available
			if (b.getUnits() < requiredUnits) {
				int giveBack = b.getUnits();
				//we make use of it 
				requiredUnits = requiredUnits - b.getUnits();
				b.setUnits(0);
				int val = findASubstitute(i + inc, boundary, inc, requiredUnits, ratio, properties);
				return (int) ((giveBack + val) / ratio);
			} else {
				//when there are enough units
				b.setUnits(b.getUnits() - requiredUnits);
				return (int) (requiredUnits / ratio);
			}
		}
	}

	/**
	 * This is the main logic function with the substitution rules and army ranking behaviour taken into consideration.
	 * @throws Exception
	 */
	public void substituteBattalions() throws Exception {
		int iter = 0;
		int n = this.map.keySet().size();
		ArrayList<Property> properties = new ArrayList<Property>(this.map.keySet());
		for (Property h : properties) {
			Battalion currentB = this.map.get(h);
			int requiredUnits = currentB.getUnits() > 0 ? 0 : currentB.getUnits();
			if (requiredUnits > 0)// no substitution required
				continue;

			if (substitutionRule.direction < 0) {// check the lower rank first
				int boundary = 0;
				if (substitutionRule.range > 0)
					boundary = iter - substitutionRule.range;
				boundary = boundary < 0 ? 0 : boundary;
				int subUnits = findASubstitute(iter - 1, boundary, -1, Math.abs(requiredUnits),
						substitutionRule.higherTolowerRatio, properties);
				if (subUnits > 0) {
					currentB.setUnits(currentB.getUnits() + subUnits);
				}

				if (currentB.getUnits() < 0) {// still zero then check the
												// higher ranks
					boundary = n - 1;
					if (substitutionRule.range > 0)
						boundary = iter + substitutionRule.range;
					boundary = boundary > n - 1 ? n - 1 : boundary;
					subUnits = findASubstitute(iter + 1, boundary, 1, Math.abs(requiredUnits),
							1 / substitutionRule.higherTolowerRatio, properties);
					if (subUnits > 0) {
						currentB.setUnits(currentB.getUnits() + subUnits);
					}
				}
			} else {// check the higher rank first

				int boundary = n - 1;
				int subUnits = 0;
				if (substitutionRule.range > 0)
					boundary = iter + substitutionRule.range;
				boundary = boundary > n - 1 ? n - 1 : boundary;
				subUnits = findASubstitute(iter + 1, boundary, 1, Math.abs(requiredUnits),
						1 / substitutionRule.higherTolowerRatio, properties);
				if (subUnits > 0) {
					currentB.setUnits(currentB.getUnits() + subUnits);
				}

				if (currentB.getUnits() < 0) {// still zero then check the
												// higher ranks
					boundary = 0;
					if (substitutionRule.range > 0)
						boundary = iter - substitutionRule.range;
					boundary = boundary < 0 ? 0 : boundary;
					subUnits = findASubstitute(iter - 1, boundary, -1, Math.abs(requiredUnits),
							substitutionRule.higherTolowerRatio, properties);
					if (subUnits > 0) {
						currentB.setUnits(currentB.getUnits() + subUnits);
					}
				}

			}

			if (currentB.getUnits() < 0) {
				throw new Exception("Player lost");
			}
			iter++;
		}
	}

	public void updateBattalion(Property h, int units) {
		Battalion b = this.map.get(h);
		if (b == null) {
			// create a new battalion with the units
			b = new Battalion(units, h);
			addBattalion(b);
		}
		b.setUnits(units);
	}

	public Comparator<Property> getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(Comparator<Property> newbehaviour) {
		this.behaviour = newbehaviour;

		TreeMap<Property, Battalion> temp = new TreeMap<Property, Battalion>(); // newbehaviour
		temp.putAll(map);
		this.map = temp;
	}

	public void setSubstitutionRule(Rule substitutionRule) {
		this.substitutionRule = substitutionRule;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		for (Property p : this.map.keySet()) {
			str.append(this.map.get(p).toString()).append("\n");
		}
		return str.toString();
	}

}
