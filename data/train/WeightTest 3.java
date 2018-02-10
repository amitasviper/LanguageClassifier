package javaapplication6;

import KeywordSearch.Weight;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest 
{
    public WeightTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testCompareToForEquals()
    {
        System.out.println("compareTo");
        Weight weight1 = new Weight();
        weight1.setTotalWeight(64.0);
        Weight weight2=new Weight();
        weight2.setTotalWeight(64.0);
        int expectedResult = 0;
        int result = weight1.compareTo(weight2);
        assertEquals(expectedResult, result);
        
    }
    
    @Test
    public void testCompareToForGreater()
    {
        System.out.println("compareTo");
        Weight weight1 = new Weight();
        weight1.setTotalWeight(96.0);
        Weight weight2=new Weight();
        weight2.setTotalWeight(64.0);
        int expectedResult = 1;
        int result = weight1.compareTo(weight2);
        assertEquals(expectedResult, result);
        
    }
    
    @Test
    public void testCompareToForLesser()
    {
        System.out.println("compareTo");
        Weight weight1 = new Weight();
        weight1.setTotalWeight(44.0);
        Weight weight2=new Weight();
        weight2.setTotalWeight(64.0);
        int expectedResult = -1;
        int result = weight1.compareTo(weight2);
        assertEquals(expectedResult, result);
        
    }
}
