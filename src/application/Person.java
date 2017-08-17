package application;

public class Person {
	private String firstName;
	private String lastName;
	private String email;
	
	public String getfirstName(){
		return firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public String getemail(){
		return email;
	}
	public void setfirstName(String firstName){
		this.firstName=firstName;
	}
	public void setlastName(String lastName){
		this.lastName=lastName;
	}
	public void setemail(String email){
		this.email=email;
	}

}
