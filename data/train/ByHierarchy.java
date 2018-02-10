package in.problem.substitution.rules;

import in.problem.entity.Battalion.Property;

import java.util.Comparator;

public class ByHierarchy implements Comparator<Property>{

	@Override
	public int compare(Property o1, Property o2) {
        if (o1.getHierarchy() - o2.getHierarchy() == 0) {
            return o1.getSpeed() - o2.getSpeed();
        } else
            return o1.getHierarchy() - o2.getHierarchy();
	}

}
