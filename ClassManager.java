import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <insert class description here>
 *
 * @author Ally Dauck
 *
 */
public class ClassManager {

	// Rule Code: EXP00-J Do not ignore values returned by methods
	// used every time we use choice = scanner.nextLine() instead of just
	// scanner.nextLine()

	// Rule Code: ENV06-J Production code must not contain debugging entry points
	// compliant by not including a debugging method in the final product

	// Rule Code: ERR07-J Do not throw Runtime Exception, Exception, or Throwable
	// no exceptions will be thrown as we are handling all the exceptions

	// Rule Code: NUM09-J Do not use floating-point variables as loop counters
	// all counters used are ints

	// Rule Code: OBJ10-J Do not use public static nonfinal fields

	// Rule Code: FIO03-J Remove temporary files before termination
	// no temporary files needed


	// Do not compare or inspect the string representation of floating-point values
	// floating point values are not permitted in this program and will be checked
	// during input validation

	// Recommendation Code: IDS50-J. Use conservative file naming conventions
	// all files are named accordingly and appropriately

	// Recommendation Code: DCL52-J. Do not declare more than one variable per
	// declaration
	// none of the combined files declare more than one variable per declaration

	// Recommendation Code: EXP52-J. Use braces for the body of an if, for, or while
	// statement
	// every if statement and loop usage has curly braces for the body

	// Recommendation: EXP51-J. Do not perform assignments in conditional
	// expressions
	//

	// Recommendation: DCL53-J. Minimize the scope of variables
	// all variables are in appropriate scope for usage or they are transfered along
	// to another method

	// Recommendation: DCL50-J. Use visually distinct identifiers
	// all variables use meaningful and distict naming conventions

	// Recommendation: DCL59-J. Do not apply public final to constants whose value
	// might change in later releases
	// all fields with values that might change are no declared final

	// Recommendation: DCL55-J. Properly encode relationships in constant
	// definitions
	// no constants have relationships to be encoded.

	// Recommendation: EXP55-J Use the same type for the second and third operands
	// in conditional expressions
	// no conditionals used

	// Recommendation: OBJ52- Write Garbage Collection Friendly Code
	// this code is small and uses only the space that it absolutely needs.
	// program can be rerun with new files entered and saved if needed.
	// large objects are not being used and the garbage collector is not be
	// explicitly called

	// Recommendation: MSC58-J Prefer using iterators over enumerations
	// while we are not using either an iterator nor enumerator, a for loop does the
	// same thing
	// as an iterator and is much easier to read

	// Recommendation: MSC51-J Do not place a semicolon immediately following an if,
	// for, or while condition
	// why would you do this? yucky

	// Recommendation: MET50-J: Avoid ambiguous or confusing uses of overloading.
	// We do not use overloading, but could have in several instances. One way the
	// SEI CERT document suggest to uphold this recommendation is to not use
	// overloading and to just use different method names as we have done.

	// Recommendation: ERR53-J. Try to gracefully recover from system errors
	// try-catches used where system errors may occur and allow program to recover

	// Recommendation: ERR50-J. Use exceptions only for exceptional conditions
	// the only exceptions we are catching are fileNotFound and IOException.
	// the rest are being handled by doing pre-checks

	private static Scanner scanner = new Scanner(System.in);

	// Rule Code: OBJ13-J. Ensure that references to mutable objects are not exposed
	private static final String[] OPTIONS = { "Add a class", "Manage a class", "Exit" }; // array of options
	private static final String[] CLASS_MANAGE_OPTIONS = { "View all", "Manage students", "Manage grade items",
			"Update student grade item", "Display class average", "Save to CSV", "Exit" };
	private static final String[] STUDENTS_MANAGE_OPTIONS = { "View all students", "Add a student", "Exit" };
	private static final String[] GRADEITEMS_MANAGE_OPTIONS = { "View all grade items", "View one grade item",
			"Add a grade item", "Exit" };
	private static ArrayList<Class> classes = new ArrayList<Class>();
	private static DecimalFormat format = new DecimalFormat("##.#'%'");

	// MAIN - has 3 options. 1.) add a class 2.) manage a class 3.) exit
	public static void main(String[] args) {
		System.out.println("Welcome to the class manager!\n******************************************");
		int choice = 0;
		do {
			choice = displayMenu(OPTIONS);
			switch (choice) {
				case 1: // add a class
					addClass();
					break;
				case 2: // manage a class
					if (classes.size() >= 1) {
						int classSelection = getClassOptions();
						try {
							manageClass(classSelection - 1);
						} catch (Class.DivideByZeroException exception) {
							exception.getMessage();
						}

					} else {
						System.out.println("Sorry, no classes registered.");
					}
					break;
			}
		} while (choice != 3);
		// OBJ52 - Write Garbage Friendly Code
		System.gc();
	}

	private static void readFile() {
		System.out.println("Please enter the file name: ");
		String fileName = scanner.nextLine();
		while (!stringValidation(fileName)) {
			System.out.println("Invalid entry.");
			System.out.println("Please enter the file name: ");
			fileName = scanner.nextLine();
		}
		Scanner fileRead = null;
		try {
			fileRead = new Scanner(new File(fileName));
			String firstLine = fileRead.nextLine();
			String[] firstLineArray = firstLine.split(",");
			classes.add(new Class(firstLineArray[0], firstLineArray[1], firstLineArray[2]));
			String secondLine = fileRead.nextLine();
			String[] gradesArray = secondLine.split(",");
			String thirdLine = fileRead.nextLine();
			String[] gradesPointsArray = thirdLine.split(",");
			for (int i = 1; i < gradesArray.length; i++) {
				GradeItem toAdd = new GradeItem(gradesArray[i], Integer.parseInt(gradesPointsArray[i]));
				classes.get(classes.size() - 1).addGradeItem(toAdd);
			}
			fileRead.nextLine();
			while (fileRead.hasNextLine()) {
				String line = fileRead.nextLine();
				String[] studentArray = line.split(",");
				Student toAdd = new Student(studentArray[1], studentArray[0].substring(1),
						studentArray[2].substring(0, studentArray[2].length() - 1));
				boolean duplicate = false;
				ArrayList<Student> temp = classes.get(classes.size() - 1).getStudentList();
				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equals(toAdd)) {
						duplicate = true;
					}
				}
				if (!duplicate) {
					classes.get(classes.size() - 1).addStudent(toAdd);
					System.out.println("Student " + toAdd.getName() + " added.");
				}
				for (int j = 3; j < gradesArray.length + 2; j++) {
					classes.get(classes.size() - 1).getStudent(classes.get(classes.size() - 1).getClassSize() - 1)
							.updateGrade(j - 3, Integer.parseInt(studentArray[j]));
				}
			}
			System.out.println();
			System.out.println("File successful read.");
		} // ERR07-J Do not throw Runtime Exception, Exception, or Throwable
		catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} finally {
			// ERR08-J. Do not catch NullPointerException or any of its ancestors
			if (fileRead != null) {
				// FIO04-J Release resources when they are no longer needed
				fileRead.close();
			}
		}
	}

	private static void saveFile(int classIndex) {
		System.out.println("Please enter the file name: ");
		String fileName = scanner.nextLine();
		while (!stringValidation(fileName)) {
			System.out.println("Invalid entry.");
			System.out.println("Please enter the file name: ");
			fileName = scanner.nextLine();
		}
		PrintWriter printer = null;
		FileWriter file = null;
		try {
			// Recommendation: FIO50-J: Do not make assumptions about file creation
			// FIO02-J. Detect and handle file-related errors
			while (new File(fileName).exists()) {

				System.out.println("File already exists. \nPlease enter the filename: ");
				fileName = scanner.nextLine();
			}
			file = new FileWriter(fileName);
			printer = new PrintWriter(fileName);
			Class classToPrint = classes.get(classIndex);
			printer.println(classToPrint.getClassName() + "," + classToPrint.getClassNumber() + ","
					+ classToPrint.getClassSection());
			printer.print(",");
			for (int i = 2; i < classToPrint.getGradesSize() + 2; i++) {
				// EXP550J Use the same type for second and third operands in conditional
				// expressions
				if (i == classToPrint.getGradesSize() + 1) {
					printer.println(classToPrint.getGradeItem(i - 2).getGradeItemName());
				} else {
					printer.print(classToPrint.getGradeItem(i - 2).getGradeItemName() + ",");
				}
			}
			printer.print("Total,");
			for (int i = 2; i < classToPrint.getGradesSize() + 2; i++) {
				// EXP550J Use the same type for second and third operands in conditional
				// expressions
				if (i == classToPrint.getGradesSize() + 1) {
					printer.println(classToPrint.getGradeItem(i - 2).getMaxPoints());
				} else {
					printer.print(classToPrint.getGradeItem(i - 2).getMaxPoints() + ",");
				}
			}
			printer.println("Name");
			for (int i = 0; i < classToPrint.getClassSize(); i++) {
				Student toPrint = classToPrint.getStudent(i);
				// IDS55-J: Understand how escape characters are interpreted when strings are
				// loaded
				// we have to add the " to the name to ensure the commas in the name are not
				// included in the csv
				// we do this with the escape character \
				printer.print("\"" + toPrint.getLastName() + "," + toPrint.getFirstName() + "," + toPrint.getStudentID()
						+ "\",");
				for (int j = 0; j < classToPrint.getGradesSize(); j++) {
					// EXP550J Use the same type for second and third operands in conditional
					// expressions
					if (j == classToPrint.getGradesSize() - 1) {
						printer.println(toPrint.getGrade(j).getEarnedPoints());
					} else {
						printer.print(toPrint.getGrade(j).getEarnedPoints() + ",");
					}

				}
			}
			System.out.println("File saved successfully.");
			System.out.println();
		} // ERR07-J Do not throw Runtime Exception, Exception, Throwable
		catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			// ERR08-J. Do not catch NullPointerException or any of its ancestors
			if (printer != null) {
				// FIO04-J Release resources when they are no longer needed
				printer.close();
			}

		}
	}

	// Add a class - will ask to add students now, can be done later
	private static void addClass() {
		System.out.print("Add class from CSV? (y or n): ");
		String CSVOption = scanner.nextLine();
		while (!CSVOption.equalsIgnoreCase("y") && !CSVOption.equalsIgnoreCase("n")) {
			System.out.print("Add class from CSV? (y or n): ");
			CSVOption = scanner.nextLine();
		}
		if (CSVOption.equalsIgnoreCase("y")) {
			readFile();
		} else {
			System.out.println();

			System.out.print("Enter the class number: ");
			String classNumber = scanner.nextLine();
			while (!stringValidation(classNumber)) {
				System.out.println("Invalid entry.");
				System.out.println("Please enter the class number: ");
				classNumber = scanner.nextLine();
			}

			System.out.print("Enter the section number: ");
			String sectionNumber = scanner.nextLine();
			while (!stringValidation(sectionNumber)) {
				System.out.println("Invalid entry.");
				System.out.println("Please enter the section number: ");
				sectionNumber = scanner.nextLine();
			}

			System.out.print("Enter the class name: ");
			String className = scanner.nextLine();
			while (!stringValidation(className)) {
				System.out.println("Invalid entry.");
				System.out.println("Please enter the class name: ");
				className = scanner.nextLine();
			}

			classes.add(new Class(className, classNumber, sectionNumber));

			System.out.print("\nAdd students? (y or n): ");
			String addStudents = scanner.nextLine();
			while (!addStudents.equalsIgnoreCase("y") && !addStudents.equalsIgnoreCase("n")) {
				System.out.print("Add students? (y or n): ");
				addStudents = scanner.nextLine();
			}
			if (addStudents.equalsIgnoreCase("y")) {
				String input = "";
				do {
					addStudentToClass(classes.size() - 1);
					System.out.print("\nAdd another student (y or n): ");
					input = scanner.nextLine();
				} while (!input.equalsIgnoreCase("n"));
			}

		}
	}

	// actual code for adding the student object
	private static void addStudentToClass(int classIndex) {
		System.out.print("\nEnter student first name: ");

		String firstName = scanner.nextLine();
		while (!stringValidation(firstName)) {
			System.out.println("Invalid entry.");
			System.out.println("Please enter the first name: ");
			firstName = scanner.nextLine();
		}
		System.out.print("Enter student last name: ");
		String lastName = scanner.nextLine();
		while (!stringValidation(lastName)) {
			System.out.println("Invalid entry.");
			System.out.println("Please enter the last name: ");
			lastName = scanner.nextLine();
		}
		System.out.print("Enter student ID: ");
		String studentID = scanner.nextLine();
		while (!stringValidation(studentID)) {
			System.out.println("Invalid entry.");
			System.out.println("Please enter the file name: ");
			studentID = scanner.nextLine();
		}
		classes.get(classIndex).addStudent(new Student(firstName, lastName, studentID));
		System.out.println("Student " + firstName + " " + lastName + " added.");
	}

	// actual code for adding gradeItem object
	private static int addGradeItemToClass(int classIndex) {
		System.out.print("\nEnter grade item name: ");
		String name = scanner.nextLine();
		System.out.print("Enter max points: ");
		String choiceCheck = scanner.nextLine();
		Scanner scanInput = new Scanner(choiceCheck);
		while (!scanInput.hasNextInt()) {
			System.out.println("Invalid input, please enter a number.");
			System.out.print("\nEnter max points: ");
			choiceCheck = scanner.nextLine();
			scanInput = new Scanner(choiceCheck);
		}
		int maxPoints = Integer.parseInt(choiceCheck);
		int size = classes.get(classIndex).addGradeItem(new GradeItem(name, maxPoints));
		return size;
	}

	// Manage a class - has 7 options. 1.) view all 2.) manage students 3.) manage
	// grade items 4.) update a student grade 5.) display class average 6.) print to
	// csv
	// 7.) quit
	private static void manageClass(int classIndex) throws Class.DivideByZeroException {
		int classManageSelection;
		do {
			classManageSelection = displayMenu(CLASS_MANAGE_OPTIONS);
			switch (classManageSelection) {

				case 1: // view all
					classes.get(classIndex).viewAllStudentsGrades();
					System.out.println();
					break;

				case 2: // manage students
					int studentManageSelection;
					do {
						studentManageSelection = displayMenu(STUDENTS_MANAGE_OPTIONS);
						switch (studentManageSelection) {

							case 1:// view all students
								System.out.println("\nStudents: ");
								classes.get(classIndex).viewAllStudents();
								System.out.println();
								break;

							case 2:// add student to class
								addStudentToClass(classIndex);
								break;
						}

					} while (studentManageSelection != 3);
					break;

				case 3: // manage grade items
					int gradeItemManageSelection;
					do {
						gradeItemManageSelection = displayMenu(GRADEITEMS_MANAGE_OPTIONS);
						switch (gradeItemManageSelection) {
							case 1:// view all grade items
								classes.get(classIndex).viewAllGrades();
								break;
							case 2: // view one grade item
								if (classes.get(classIndex).getGradesSize() >= 1) {
									int gradeChoice = classes.get(classIndex).getGradeOptions(scanner) - 1;
									classes.get(classIndex).viewOneGrade(gradeChoice);
									System.out.println("Enter student grades for this? (y or n): ");
									String enterGrade = scanner.nextLine();
									while (!enterGrade.equalsIgnoreCase("y") && !enterGrade.equalsIgnoreCase("n")) {
										System.out.print("Enter student grades for this? (y or n): ");
										enterGrade = scanner.nextLine();
									}
									if (enterGrade.equalsIgnoreCase("y")) {
										enterGrades(classIndex, gradeChoice);
									}
								} else {
									System.out.println("No grades have been entered.");
								}
								System.out.println();

								break;
							case 3:// add a grade item
								int size = addGradeItemToClass(classIndex);
								System.out.println("Enter student grades for this? (y or n): ");
								String enterGrade = scanner.nextLine();
								while (!enterGrade.equalsIgnoreCase("y") && !enterGrade.equalsIgnoreCase("n")) {
									System.out.print("Enter student grades for this? (y or n): ");
									enterGrade = scanner.nextLine();
								}
								if (enterGrade.equalsIgnoreCase("y")) {
									enterGrades(classIndex, size - 1);
								}
								break;
						}
					} while (gradeItemManageSelection != 4);
					break;

				case 4:// update student grade item
					int studentChoice = classes.get(classIndex).getStudentOptions(scanner);
					int gradeChoice = classes.get(classIndex).getGradeOptions(scanner);
					System.out.print("Please enter the corrected grade: ");
					String choiceCheck = scanner.nextLine();
					Scanner scanInput = new Scanner(choiceCheck);
					// IDS03-J. Do not log unsanitized user input
					while (!scanInput.hasNextInt()) {
						System.out.println("Invalid input, please enter a number.");
						System.out.print("\nPlease pick an option: ");
						choiceCheck = scanner.nextLine();
						scanInput = new Scanner(choiceCheck);
					}
					int newEarnedPoints = Integer.parseInt(choiceCheck);
					classes.get(classIndex).getStudent(studentChoice - 1).updateGrade(gradeChoice - 1, newEarnedPoints);
					break;

				case 5:// display class average
					System.out.println("Class Average: " + format.format(classes.get(classIndex).getClassAverage()));
					break;
				case 6:// save to CSV
					saveFile(classIndex);
					break;
			}
		} while (classManageSelection != 7);
	}

	private static void enterGrades(int classIndex, int gradeIndex) {
		// MET00-J. Validate method arguments
		// we are using the method getGradeOptions() before we call this to ensure we
		// only get valid indices
		for (int i = 0; i < classes.get(classIndex).getClassSize(); i++) {
			System.out.println(classes.get(classIndex).getStudent(i).getName());
			System.out.print("Score: ");
			String choiceCheck = scanner.nextLine();
			Scanner scanInput = new Scanner(choiceCheck);
			// IDS03-J. Do not log unsanitized user input
			while (!scanInput.hasNextInt()) {
				System.out.println("Invalid input, please enter a number.");
				System.out.println("Score: ");
				choiceCheck = scanner.nextLine();
				scanInput = new Scanner(choiceCheck);
			}
			int actualPoints = Integer.parseInt(choiceCheck);
			classes.get(classIndex).getStudent(i).updateGrade(gradeIndex, actualPoints);
		}
	}

	private static int getClassOptions() {
		int choice;
		do {
			for (int i = 0; i < classes.size(); i++) {
				System.out.println("\t" + (i + 1) + ". " + classes.get(i).getFullClassName());
			}
			System.out.print("\nPlease pick an option: ");
			String choiceCheck = scanner.nextLine();
			Scanner scanInput = new Scanner(choiceCheck);
			// IDS03-J. Do not log unsanitized user input
			while (!scanInput.hasNextInt()) {
				System.out.println("Invalid input, please enter a number.");
				System.out.print("\nPlease pick an option: ");
				choiceCheck = scanner.nextLine();
				scanInput = new Scanner(choiceCheck);
			}
			choice = Integer.parseInt(choiceCheck);
		} while (choice < 1 || choice > classes.size());
		return choice;
	}

	private static int displayMenu(String[] toDisplay) {
		// MET00-J. Validate method arguments
		// this is done everytime we call choice = displayMenu().
		// compliant code is within displayMenu to ensure we only recieve valid input
		int choice;
		do {
			for (int i = 0; i < toDisplay.length; i++) {
				System.out.println("\t" + (i + 1) + ". " + toDisplay[i]);
			}
			System.out.print("\nPlease pick an option: ");
			String choiceCheck = scanner.nextLine();
			Scanner scanInput = new Scanner(choiceCheck);
			// IDS03-J. Do not log unsanitized user input
			while (!scanInput.hasNextInt()) {
				System.out.println("Invalid input, please enter a number.");
				System.out.print("\nPlease pick an option: ");
				choiceCheck = scanner.nextLine();
				scanInput = new Scanner(choiceCheck);
			}
			choice = Integer.parseInt(choiceCheck);
		} while (choice < 1 || choice > toDisplay.length);
		return choice;
	}

	// IDS01-J: Normalize strings before validating them
	// used on all fileName input reads
	// MET03-J: Methods that preform a security check must be declared private or
	// final
	// Recommendation: MET54-J. Always provide feedback about the resulting value of
	// a method
	// IDS03-J. Do not log unsanitized user input
	// IDS00-J: Prevent SQL Injection while we are not using an sql database currently
	// we have still included SQL Injection characters in our string validator for
	// future updates that may include storing the values in a database
	private static boolean stringValidation(String validate) {
		validate = Normalizer.normalize(validate, Form.NFKC);

		Pattern pattern = Pattern.compile("[A-Za-z0-9._-]+");
		Matcher matcher = pattern.matcher(validate);
		// EXP550J Use the same type for second and third operands in conditional
		// expressions
		if (matcher.find()) {
			// no blacklisted tag
			return true;
		} else {
			// found blacklisted tag
			return false;
		}
	}

}
