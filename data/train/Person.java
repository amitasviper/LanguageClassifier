import java.io.*;

class Government{
	String name;
	static Government currentGovernment;
	
	private Government(){

	}

	public static Government getGovernment(){
		if(currentGovernment == null){
			currentGovernment = new Government();
		}
		return currentGovernment;
	}

}

class Person{
	
	public static void main(String[] args){
		Male male = new Male();
		Male male2 = new Male();
		male.GENDER = "FEMALE";
		System.out.println("Variable is : " + male2.GENDER);

		male.printAge();

		Male.printAge();

	}
}