package sort.secondary;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class KeyComparator implements WritableComparable<KeyComparator> {
	private String strYearMonth;
	private int nCount;

	/**
	 * Serialize the fields of this object to <code>out</code>.
	 * 
	 * @param out
	 *            <code>DataOuput</code> to serialize this object into.
	 * @throws IOException
	 */
	public void write(DataOutput out) throws IOException {
		out.writeUTF(strYearMonth);
		out.writeInt(nCount);
	};

	/**
	 * Deserialize the fields of this object from <code>in</code>.
	 * 
	 * <p>
	 * For efficiency, implementations should attempt to re-use storage in the
	 * existing object where possible.
	 * </p>
	 * 
	 * @param in
	 *            <code>DataInput</code> to deseriablize this object from.
	 * @throws IOException
	 */
	public void readFields(DataInput in) throws IOException {
		this.strYearMonth = in.readUTF();
		this.nCount = in.readInt();
	};

	@Override
	public int compareTo(KeyComparator data) {
		// 先对YearMonth进行分类
		int result = this.strYearMonth.compareTo(data.getYearMonth());

		if (result == 0) {
			// 然后再对count也就是最后的值进行表排序
			result = compare(nCount, data.getCount());
		}
		return result;
	}

	public String getYearMonth() {
		return strYearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.strYearMonth = yearMonth;
	}

	public int getCount() {
		return nCount;
	}

	public void setCount(int count) {
		this.nCount = count;
	}

	public static int compare(int a, int b) {
		return a < b ? -1 : (a > b ? 1 : 0);
	}

	@Override
	public String toString() {
		return strYearMonth;
	}

}
