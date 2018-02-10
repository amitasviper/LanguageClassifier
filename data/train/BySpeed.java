package in.problem.substitution.rules;

import in.problem.entity.Battalion.Property;

import java.util.Comparator;

/*
 * Runtime Comparator classes for determining the actual hierarchy
 */
public class BySpeed implements Comparator<Property>
{

    @Override
    public int compare(Property o1, Property o2)
    {
        if ((o1.getSpeed() - o2.getSpeed()) == 0) {
            return o1.getHierarchy() - o2.getHierarchy();
        } else
        return o1.getSpeed() - o2.getSpeed();
    }

}
