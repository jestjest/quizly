package quizme.links;

/**
 * A class to store a handful of minimal stats.
 * @author hadip
 *
 */
public class SummaryStat {
	/**
	 * The number of times one person or more have taken this quiz.
	 */
	public int numberTaken;
	public float meanScore;
	public float minScore;
	public float maxScore;
	public double meanTime;
	public long minTime;
	public long maxTime;
	
	
	public SummaryStat() {
		numberTaken = 0;
		meanScore = 0;
		minScore = 1000;
		maxScore = -1;
		meanTime = 0;
		minTime = 1000;
		maxTime = -1;
	}
	
	public SummaryStat( int numberTaken, float meanScore, float minScore, float maxScore, 
			double meanTime, long minTime, long maxTime) {
		this.numberTaken = numberTaken;
		this.meanScore = meanScore;
		this.minScore = minScore;
		this.maxScore = maxScore;
		this.meanTime = meanTime;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}
}
