import java.util.ArrayList;
import java.util.Arrays;

/**
 * <insert class description here>
 *
 * @author Ally Dauck
 *
 */
// Rule Code: OBJ07-J. Sensitive classes must not let themselves be copied
public final class Student {
	// Rule Code: OBJ01-J Limit the Accessibility of Fields (The code and name come
	// from the documentation)
	private String firstName;
	private String lastName;
	private String studentID;
	private ArrayList<GradeItem> grades;

	// Rule Code: OBJ11-J Be wary of letting constructors throw exceptions
	public Student(String firstName, String lastName, String studentID) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentID = studentID;

		// MET05-J. Ensure that constructors do not call overridable methods.
		// the getName method is not overridable because it was declared as final.
		// Therefore we can use it in the constructor. No other overridable methods are called
		//System.out.println("Student " + this.getName() + " added.");

		grades = new ArrayList<GradeItem>();
	}

	// Rule Code: OBJ04-J. Provide mutable classes with copy functionality to safely
	// allow passing instances to untrusted code
	public void setGradeItem(GradeItem gradeItem, int index) {
		grades.set(index, new GradeItem(gradeItem.getGradeItemName(), gradeItem.getMaxPoints()));
		grades.get(index).setEarnedPoints(gradeItem.getEarnedPoints());
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public final String getName() {
		return lastName + ", " + firstName;
	}

	public String getStudentID() {
		return studentID;
	}

	public void addGrade(GradeItem gradeToAdd) {
		GradeItem toAdd = new GradeItem(gradeToAdd.getGradeItemName(), gradeToAdd.getMaxPoints());
		grades.add(toAdd);
		grades.get(grades.size() - 1).setEarnedPoints(0);
	}

	// Rule Code: OBJ05-J. Do not return references to private mutable class members
	public ArrayList<GradeItem> getGradesList() {
		ArrayList<GradeItem> newGrades = new ArrayList<GradeItem>();
		for (int i = 0; i < grades.size(); i++) {
			newGrades.add(new GradeItem(grades.get(i).getGradeItemName(), grades.get(i).getMaxPoints()));
			newGrades.get(i).setEarnedPoints(grades.get(i).getEarnedPoints());
		}
		return newGrades;
	}

	public void setGradesList(ArrayList<GradeItem> toMatch) {
		for (int i = grades.size(); i < toMatch.size(); i++) {
			addGrade(toMatch.get(i));
		}
	}

	public void updateGrade(int index, int actualPoints) {
		grades.get(index).setEarnedPoints(actualPoints);
	}

	public GradeItem getGrade(int index) {
		return grades.get(index);
	}

	public double getGradePercent(int index) {
		return grades.get(index).getGradePercent();
	}

	public double getEarnedPoints(int index) {
		return grades.get(index).getEarnedPoints();
	}

	public double getTotalGrade() {
		double totalPoints = 0;
		double totalEarned = 0;
		for (int i = 0; i < grades.size(); i++) {
			totalPoints += grades.get(i).getMaxPoints();
			totalEarned += (grades.get(i).getEarnedPoints());
		}
		// NUM02-J. Ensure that division and remainder operations do not result in
		// divide-by-zero errors
		double total = 0;
		if (totalPoints != 0) {
			total = (totalEarned / totalPoints) * 100;
		}
		return total;
	}

	public void printAllGrades() {
		for (int i = 0; i < grades.size(); i++) {
			System.out.println(grades.get(i).toString());
		}
	}

	@Override
	public boolean equals(Object o) {
		// EXP52-J. Use braces for the body of an if, for, or while statement
		// EXP51-J. Do not perform assignments in conditional expressions
		if (o == this) {
			return true;
		}
		// OBJ09-J. Compare classes and not class names
		if (this.getClass() != o.getClass()) {
			return false;
		}
		Student student = (Student) o;
		// NUM11-J. Do not compare or inspect the string representation of
		// floating-point values
		// EXP02-J: Do not use the Object.equals() method to compare two arrays
		// EXP51-J. Do not perform assignments in conditional expressions
		if ((firstName.equals(student.firstName)) && (lastName.equals(student.lastName)
				&& (studentID.equals(student.studentID)) && (grades.toArray().equals(student.grades.toArray())))) {
			return true;
		}
		return false;
	}

}
