import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;

/**
 * <insert class description here>
 *
 * @author Ally Dauck
 *
 */
public class Class {
	// Rule Code: OBJ01-J Limit the Accessibility of Fields (The code and name come
	// from the documentation)
	private String className;
	private String classNumber;
	private String classSection;
	private ArrayList<Student> students = new ArrayList<Student>();
	private ArrayList<GradeItem> grades = new ArrayList<GradeItem>();

	// NUM10-J: Do not construct BigDecimal objects from floating-point literals
	private DecimalFormat format = new DecimalFormat("##.#'%'");

	// Rule Code: OBJ11-J Be wary of letting constructors throw exceptions
	public Class(String className, String classNumber, String classSection) {
		this.className = className;
		this.classNumber = classNumber;
		this.classSection = classSection;
	}

	public String getClassName() {
		return className;
	}

	public String getClassSection() {
		return classSection;
	}

	public String getClassNumber() {
		return classNumber;
	}

	public String getFullClassName() {
		String result = classNumber + "-" + classSection + ": " + className;
		return result;
	}

	public int getClassSize() {
		return students.size();
	}

	public int getGradesSize() {
		return grades.size();
	}

	public GradeItem getGradeItem(int gradeIndex) {
		return grades.get(gradeIndex);
	}

	public void addStudent(Student studentToAdd) {
		students.add(studentToAdd);
		students.get(students.size() - 1).setGradesList(grades);
	}

	// Rule Code: OBJ04-J. Provide mutable classes with copy functionality to safely
	// allow passing instances to untrusted code
	public void setStudent(Student student, int index) {
		students.set(index, new Student(student.getFirstName(), student.getLastName(), student.getStudentID()));
	}

	public int addGradeItem(GradeItem gradeItemToAdd) {
		GradeItem toAdd = new GradeItem(gradeItemToAdd.getGradeItemName(), gradeItemToAdd.getMaxPoints());
		grades.add(toAdd);
		for (int i = 0; i < students.size(); i++) {
			students.get(i).addGrade(toAdd);
		}
		return grades.size();
	}

	public Student getStudent(int studentIndex) {
		return students.get(studentIndex);
	}

	// Rule Code: OBJ05-J. Do not return references to private mutable class members
	public ArrayList<Student> getStudentList() {
		ArrayList<Student> newStudents = new ArrayList<Student>();
		for (int i = 0; i < students.size(); i++) {
			newStudents.add(new Student(students.get(i).getFirstName(), students.get(i).getLastName(),
					students.get(i).getStudentID()));
			newStudents.get(i).setGradesList(students.get(i).getGradesList());
		}
		return newStudents;
	}

	public double getClassAverage() throws DivideByZeroException {
		double totalClass = 0;
		//MSC58-J Prefer using iterators over enumerations
		Iterator<Student> stu = students.iterator();
		while(stu.hasNext())
		{
			totalClass += stu.next().getTotalGrade();
		}
		/*
		for (int i = 0; i < students.size(); i++) {
			totalClass += students.get(i).getTotalGrade();
		}
		*/
		
		// NUM02-J. Ensure that division and remainder operations do not result in
		// divide-by-zero errors
		// ERR51-J. Prefer user-defined exceptions over more general exception types
		//ERR07-J. Do not throw Runtime Exception, Exception, or Throwable
		double average = (totalClass / students.size());
		if (Double.isNaN(average) || Double.isInfinite(average)) {
			throw new DivideByZeroException("Division by Zero Error");
		}
		return average;
	}

	public double getGradeItemAverage(int gradeItemIndex) {
		double total = 0;
		Iterator<Student> stu = students.iterator();
		while(stu.hasNext())
		{
			total += stu.next().getGrade(gradeItemIndex).getEarnedPoints(); 
		}
		/*
		for (int i = 0; i < students.size(); i++) {
			total += students.get(i).getGrade(gradeItemIndex).getEarnedPoints();
		}
		*/

		// NUM02-J. Ensure that division and remainder operations do not result in
		// divide-by-zero errors
		double average = 0;
		if (students.size() != 0) {
			average = (total / students.size());
		}
		return average;
	}

	public void viewAllStudents() {
		for (int i = 0; i < students.size(); i++) {
			System.out.println(students.get(i).getName() + "(" + students.get(i).getStudentID() + ")");
		}
	}

	public int getStudentOptions(Scanner scanner) {
		int choice;
		do {
			for (int i = 0; i < students.size(); i++) {
				System.out.println("\t" + (i + 1) + ". " + students.get(i).getName());
			}
			System.out.print("\nPlease pick an option: ");
			String choiceCheck = scanner.nextLine();
			Scanner scanInput = new Scanner(choiceCheck);
			while (!scanInput.hasNextInt()) {
				System.out.println("Invalid input, please enter a number.");
				System.out.print("\nPlease pick an option: ");
				choiceCheck = scanner.nextLine();
				scanInput = new Scanner(choiceCheck);
			}
			choice = Integer.parseInt(choiceCheck);
		} while (choice < 1 || choice > students.size());
		return choice;
	}

	public int getGradeOptions(Scanner scanner) {
		int choice;
		do {
			for (int i = 0; i < grades.size(); i++) {
				System.out.println("\t" + (i + 1) + ". " + grades.get(i).getGradeItemName());
			}
			System.out.print("\nPlease pick an option: ");
			String choiceCheck = scanner.nextLine();
			Scanner scanInput = new Scanner(choiceCheck);
			while (!scanInput.hasNextInt()) {
				System.out.println("Invalid input, please enter a number.");
				System.out.print("\nPlease pick an option: ");
				choiceCheck = scanner.nextLine();
				scanInput = new Scanner(choiceCheck);
			}
			choice = Integer.parseInt(choiceCheck);
		} while (choice < 1 || choice > grades.size());
		return choice;
	}

	public void viewOneGrade(int gradeIndex) {
		System.out.println(grades.get(gradeIndex).getGradeItemName() + ": ");
		System.out.println("Average: " + format.format(getGradeItemAverage(gradeIndex)));
		System.out.println("Name                \tEarned\tTotal");
		System.out.println("-------------------------------------------");
		for (int i = 0; i < students.size(); i++) {
			String tempName = students.get(i).getName();
			if (tempName.length() > 20) {
				// EXP00-J: Do not ignore values returned by methods
				tempName = tempName.substring(0, 19);
			}
			for (int k = students.get(i).getName().length(); k <= 20; k++) {
				// EXP00-J: Do not ignore values returned by methods
				tempName += " ";
			}
			System.out.print(tempName + "\t" + (students.get(i).getEarnedPoints(gradeIndex)) + "\t");
			System.out.print(format.format(students.get(i).getGrade(gradeIndex).getGradePercent()) + "\t");
			System.out.println();
		}
	}

	public void viewAllGrades() {
		if (grades.size() >= 1) {
			System.out.println("Name\tTotal\tAverage");
			System.out.println("-------------------------------------------");
			for (int i = 0; i < grades.size(); i++) {
				System.out.println(grades.get(i).getGradeItemName() + ":\t" + grades.get(i).getMaxPoints() + "\t"
						+ getGradeItemAverage(i));
			}
		} else {
			System.out.println("No grades have been entered.");
		}
		System.out.println();

	}

	public void viewAllStudentsGrades() {
		if (grades.size() >= 1 || students.size() >= 1) {
			System.out.print("Name                \tTotal\t");
			for (int i = 0; i < grades.size(); i++) {
				System.out.print(grades.get(i).getGradeItemName() + "\t");
			}
			System.out.println();
			System.out.println(
					"-----------------------------------------------------------------------------------------");
			for (int i = 0; i < students.size(); i++) {
				String tempName = students.get(i).getName();
				if (tempName.length() > 20) {

					tempName = tempName.substring(0, 19);
				}
				for (int k = students.get(i).getName().length(); k <= 20; k++) {
					tempName += " ";
				}
				System.out.print(tempName + "\t" + format.format(students.get(i).getTotalGrade()) + "\t");
				for (int j = 0; j < grades.size(); j++) {
					System.out.print(format.format(students.get(i).getGrade(j).getGradePercent()) + "\t");
				}
				System.out.println();
			}
		} else {
			System.out.println("Insufficient data.");
		}
		System.out.println();

	}
	//ERRO7-J. Do not throw Runtime Exception, Exception, or Throwable
	//OBJ08-J. Do not expose private members of an outer class from within a nested class 
	class DivideByZeroException extends Exception {
		public DivideByZeroException(String str) {
			super(str);
		}
	}
}
