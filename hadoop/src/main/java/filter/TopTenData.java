package filter;

import java.util.ArrayList;



public class TopTenData {
		public ArrayList<String> listData = null;
		
		TopTenData() {
			if (listData == null) {
				listData = new ArrayList<String>();
			}
			System.out.println("TopTenData()");
		}
		
		TopTenData(TopTenData data , String value){
			if (listData == null) {
				listData = new ArrayList<String>();
			}
			listData.addAll(data.listData);
			listData.add(value);
		}
		
		TopTenData( String value ){
			if (listData == null) {
				listData = new ArrayList<String>();
			}
			listData.add(value);
			
		}
}
