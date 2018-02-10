package in.problem.substitution.rules;

/**
 * 
 * This class specifies the substitution rule for the Battalions .
 * 
 * if direction is -1 look in the lower ranks first else look in  higher ranks , for substituting units
 * 
 * if range is 1 look in adjacent battalions , if <0 look till the ends . if range>1 look from current battalion till current(+or-)range 
 * battallion , depending on direction.
 * 
 *higherTolowerRatio represents the substitution of battalions ratio . 
 */
public class Rule
{

    public int direction;
    public int range;
    public double higherTolowerRatio;

    public Rule(int direction, int range, double ratio)
    {
        super();
        this.direction = direction;
        this.range = range;
        this.higherTolowerRatio = ratio;
    }

	public Rule() {
		super();
	}

	
	public Rule copy(){
		Rule r = new Rule(this.direction, this.range,this.higherTolowerRatio);
		return r;
	}
    
}
