import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.*;

public class ColorCube {

	 public static void main(String[] args) {
			
			
			Model model = new Model("ColorCube"); 
			
			// Parameters
			// number of colors in each cube 
			int [][] num_color= new int [][]{
					{2,2,1,1},
					{1,2,1,2},
					{1,1,3,1},
					{2,1,2,1}}; 
		    
			// Variables
			IntVar [][] cube=model.intVarMatrix("CUBE",4, 6, 0,3, false);
		    IntVar[][] cube_trans = new IntVar[6][4];
		    
		    for (int i =0; i<4; i++){
		    	for (int j = 0; j<6; j++ ){
		    		cube_trans[j][i] = cube[i][j];
		    	} // End for (j)
		    } // End for (i)
		    
		    // Constraints
		    for (int side_indx = 0;  side_indx < 4; side_indx++) {
		         model.allDifferent(cube_trans[side_indx]).post();
		        } // End for (side_indx)
		    		    
		     for (int cube_indx = 0; cube_indx < 4; cube_indx++) {
		    	 
	    		 // Count number of each color in each cube
		    	 for (int clr_idx = 0; clr_idx<4; clr_idx++){
		    		IntVar num_clr = model.intVar(num_color[cube_indx][clr_idx]);
		   		    model.count(clr_idx, cube[cube_indx], num_clr).post();
		    	 } // End for (clr_idx)
		    	 
		    	 if(cube_indx==0) {
		    		for ( int l=0;l<2;l++){
		    			model.ifThen(model.arithm(cube[cube_indx][l],"=",2),
		    			             model.arithm(cube[cube_indx][l],"+",cube[cube_indx][l+2],"=",1));
		    	    	model.ifThen(model.arithm(cube[cube_indx][l],"=",3),
		    	    	             model.arithm(cube[cube_indx][l+2],"=",0));
		    	    	model.ifThen(model.arithm(cube[cube_indx][l],"=",0),
		    	    	             model.arithm(cube[cube_indx][l+2],"=",3));
		    		} // End for (l)
		    		 
		    	 } // End if(cube_indx==0)
		    	
		    	 if(cube_indx==1) {
		    		 for ( int l=0;l<2;l++){
		     			model.ifThen(model.or(model.arithm(cube[cube_indx][l],"=",2),
		     					     model.arithm(cube[cube_indx][l],"=",3)),
		     					     model.arithm(cube[cube_indx][l],"+",cube[cube_indx][l+2],"=",5));
		     	    	model.ifThen(model.arithm(cube[cube_indx][l],"=",3),
		     	    			     model.arithm(cube[cube_indx][l+2],"=",0));
		     	    	model.ifThen(model.arithm(cube[cube_indx][l],"=",0),
		     	    			     model.arithm(cube[cube_indx][l+2],"=",3));
		     	    	model.ifThen(model.arithm(cube[cube_indx][l],"=",1),
		    	    			     model.arithm(cube[cube_indx][l+2],"=",1));
		     		} // End for (l)
		    	} // End if(cube_indx==1)
		    	
		    	 if(cube_indx==2) {
		    		 for ( int l=0;l<2;l++){
		      			model.ifThen(model.arithm(cube[cube_indx][l],"=",0),
		      					model.arithm(cube[cube_indx][l+2],"=",3));
		      			model.ifThen(model.arithm(cube[cube_indx][l],"=",1),
		      					model.arithm(cube[cube_indx][l+2],"=",2));
		      			model.ifThen(model.arithm(cube[cube_indx][l],"=",2),
		      					model.arithm(cube[cube_indx][l+2],"=",1));
		      			model.ifThen(model.arithm(cube[cube_indx][l],"=",3),
		      					model.arithm(cube[cube_indx][l+2],"=",0));

		      		} // End for (l)
		    	} // End if(cube_indx==2)
		    	 
		    	 if(cube_indx==3) {
                    for ( int l=0;l<2;l++){
		     			model.ifThen(model.or(model.arithm(cube[cube_indx][l],"=",2),
		     					model.arithm(cube[cube_indx][l],"=",3)),
		     					model.arithm(cube[cube_indx][l],"+",cube[cube_indx][l+2],"=",5));
		     			model.ifThen(model.or(model.arithm(cube[cube_indx][l],"=",0),
		     					model.arithm(cube[cube_indx][l],"=",1)),
		     					model.arithm(cube[cube_indx][l],"+",cube[cube_indx][l+2],"=",1));	 
		     		} // End for (l)
		    	} // End if(cube_indx==3)
		      } // End for (cube_indx)
		    	   
		     
		     model.getSolver().solve();          
		     model.getSolver().printStatistics();    
		     System.out.print("\n");
		     
		     for (int i = 0; i < 4; i++) {
		        for (int j = 0; j < 6; j++) {
			   	   if(cube[i][j].getValue()==0) 
		        		{  System.out.print("red"); System.out.print("   ");}
			   	   if(cube[i][j].getValue()==1)  {  System.out.print("green "); System.out.print("   ");}
			   	   if(cube[i][j].getValue()==2)  {  System.out.print("yellow"); System.out.print("   ");}
			   	   if(cube[i][j].getValue()==3)  {  System.out.print("blue  "); System.out.print("   ");}
		        } // End for (j) 
		     System.out.print("\n");
		    } // End for (i)
	} // End main
} // End Class