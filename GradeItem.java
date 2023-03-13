/**
 * <insert class description here>
 *
 * @author Ally Dauck
 *
 */
// Rule Code: OBJ07-J. Sensitive classes must not let themselves be copied
public final class GradeItem {
	// Rule Code: OBJ01-J Limit the Accessibility of Fields (The code and name come
	// from the documentation)
	private String gradeItemName;
	private int earnedPoints;
	private int maxPoints;

	// Rule Code: OBJ11-J Be wary of letting constructors throw exceptions
	public GradeItem(String name, int maxPoints) {
		this.gradeItemName = name;
		this.maxPoints = maxPoints;
	}

	public void setGradeItemName(String newName) {
		this.gradeItemName = newName;
	}

	public String getGradeItemName() {
		return gradeItemName;
	}

	public void setMaxPoints(int newMaxPoints) {
		this.maxPoints = newMaxPoints;
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public void setEarnedPoints(int earnedPoints) {
		this.earnedPoints = earnedPoints;
	}

	public int getEarnedPoints() {
		return earnedPoints;
	}

	public double getGradePercent() {
		double percent = 0;
		// NUM02-J. Ensure that division and remainder operations do not result in
		// divide-by-zero errors
		// Recommendation: EXP53-J. Use parentheses for precedence of operation
		// Recommendation: NUM50-J. Convert integers to floating point for
		// floating-point operations
		if (maxPoints != 0) {
			percent = ((double) earnedPoints / (double) maxPoints) * 100;
		}
		return percent;
	}
}
