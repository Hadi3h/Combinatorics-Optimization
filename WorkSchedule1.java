//********************3H********************\\


import java.util.Arrays;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.*;

public class WorkSchedule1 {

	public static void main(String[] args) {
		Model model = new Model("WS");
		
		// Parameters
		
		BoolVar[][] Mws = model.boolVarMatrix("Mws", 5, 16); // A matrix 5*16
		int [] R = new int []{-1,-2,-3,-4,-5,-4,-2,-3,-4,-3,-5,-5,-4,-3,-3,-3}; // Number of require employee
		
		
		// Create variables
		
		IntVar[] Br = model.intVarArray("Br", 5, 0, 15, false); // Time of break
		IntVar[] S = model.intVarArray("S", 5, 0, 15, false); //Start time
		IntVar[] F = model.intVarArray("F", 5, 0, 15, false); // End time
		IntVar[] TD = model.intVarArray("TD", 5, 10, 14, false); // Time for work in a day
		IntVar[] Bb = model.intVarArray("Bb", 5, 4, 16, false); // Before break
		IntVar[] Ab = model.intVarArray("Ab", 5, 4, 16, false); // After break
		IntVar[] num_employee = model.intVarArray("num_employee",16,1,5,true); // C is an array
		IntVar[] cost = model.intVarArray("cost",16,0,5,true); // Cost is an array
		IntVar total_cost = model.intVar("total_cost", 0, 1000,false);

		
		// Create Constraint
		
		for (int i=0; i<5; i++){
			model.arithm(S[i], "+", Bb[i], "=", Br[i]).post();
			model.arithm(Br[i], "+", Ab[i], "=", F[i]).post();
			model.arithm(Bb[i], "+", Ab[i], "=", TD[i]).post();
			
			model.arithm(S[i], "<=", F[i]).post();
			model.arithm(Br[i], ">=", S[i]).post();
			model.arithm(Br[i], "<=", F[i]).post();
			
			for (int t=0; t<16; t++){
				IntVar time = model.intVar(t);
				model.ifOnlyIf((model.arithm(Mws[i][t], "=", 1)), model.and(model.arithm(time, ">=", S[i]),
						model.arithm(time, "!=", Br[i]), model.arithm(time, "<=", F[i])));
			} // End: for(t)
		} // End: for(i)
		
		//Transpose to access columns
		
		BoolVar[][] Mws_trans = new BoolVar[16][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 16; j++) {
            	Mws_trans[j][i] = Mws[i][j];
            } // End: for(j)
        } // End: for(i)
		
		 
        // At least two employees must be presented at time 14 and 15 
	
        model.sum(Mws_trans[14],">=",2).post();
	    model.sum(Mws_trans[15],">=",2).post();
	
		
		// Calculate The Cost
		
		for (int j=0; j<16; j++){
		
			model.sum(Mws_trans[j], "=", num_employee[j]).post();
			cost[j] =  model.intAbsView((model.intOffsetView(num_employee[j], R[j])));
		} // End: for(j)
					
		int [] penalty = new int[16];
		
		Arrays.fill(penalty,0,16, 20);
		
		model.scalar(cost, penalty, "=", total_cost).post();
		model.setObjective(Model.MINIMIZE, total_cost);
		
		Solver solver = model.getSolver();
		solver.solve();
		solver.showStatistics();
		model.getSolver().printStatistics();
		
		
		Solution best = solver.findSolution(null);
		System.out.print(best.record()+"\n");
		System.out.print("\n Number of solution= "+solver.getSolutionCount()+"\n");
						
		
        		
			int THW= 0; // Total Hours Work
			int TETj= 0; // Total Employee in Time j
					    
			for (int i=0; i<5; i++){
				THW = 0;
				for (int j=0; j<16; j++){
					THW = Mws[i][j].getValue()+THW;
					System.out.print(Mws[i][j].getValue());
				} // End: for(j)
				
				System.out.print("\n");
						 
			} // End: for(i)
					 
					 
			for (int j=0; j<16; j++){
				TETj = 0 ;
				for (int k=0;k<5;k++){
					TETj = Mws_trans[j][k].getValue()+TETj;
			    } // End: for(k)
			System.out.print(" cost houre "+j+"th: "+cost[j].getValue()+"\n");
							
			System.out.print(" Total Employee at houre "+j+"th: "+TETj+"\n");
			System.out.print("Requested"+j+"th: "+R[j]+"\n");
			
			}// End: for(j)
					
			System.out.print("  ******Total cost = "+total_cost.getValue()+"**********\n");
		
        			
		
	}//End of Main

}// End of Class
