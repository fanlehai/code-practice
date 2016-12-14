package sort.secondary;

import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableComparable;

public class GroupComparator extends WritableComparator {

	public GroupComparator() {
		super(KeyComparator.class, true);
	}

	
	@SuppressWarnings("rawtypes")
	public int compare(WritableComparable a, WritableComparable b) {

		KeyComparator a1 = (KeyComparator) a;
		KeyComparator b1 = (KeyComparator) b;
		
		return a1.getYearMonth().compareTo(b1.getYearMonth());
	}

}
