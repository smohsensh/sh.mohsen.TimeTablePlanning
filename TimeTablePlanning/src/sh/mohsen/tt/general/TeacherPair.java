package sh.mohsen.tt.general;

public class TeacherPair {

	private int t1, t2;

	
	
	public TeacherPair(int t1, int t2) {
		if(t1 == t2){
			try {
				throw new Exception("Pair of same teacher ! ");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.t1 = t1;
		this.t2 = t2;
	}

	public int first() {
		return t1;
	}

	public int second() {
		return t2;
	}
	
	
	
}
