package sh.mohsen.tt.runner;
import sh.mohsen.tt.evolutionary.ArraySolver;


public class RunGeneticWM {

	
	public static void main(String[] args) {
//		int x =10, y = 20, z = 5;
//		String[][][] b = new String[x][y][z];
//		String[] a = new String [x*y*z];
//		String[] c = new String [x*y*z];
//		String[][][] d = new String[x][y][z];
//		
//		
//		for (int i = 0; i < a.length; i++) {
//			a [i] = i+"";
//		}
//		for (int i = 0; i < b.length; i++) {
//			for (int j = 0; j < b[0].length; j++) {
//				for (int k = 0; k < b[0][0].length; k++) {
//					b[i][j][k] = i+"," + j+ ","+ k;
//					c[i*z*y + j*z + k] = b[i][j][k];
//				}
//			}
//		}
////		for (int i = 0; i < c.length; i++) {
////			System.out.println(a[i] + " <---> " + c[i]);
////		}
//		for (int i = 0; i < b.length; i++) {
//			for (int j = 0; j < b[0].length; j++) {
//				for (int k = 0; k < b[0][0].length; k++) {
//					d[i][j][k] = c[i*(y*z) + j*z + k];
//					if(! d[i][j][k].equals( b[i][j][k]))
//						System.out.println("fuck");
//				}
//			}
//		}
		ArraySolver as = new ArraySolver();
		as.run();
		
		
		
	}
}
