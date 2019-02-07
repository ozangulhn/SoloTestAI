import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;
public class Main {
    public static int [][] rules = {{2,8},{9},{0,10},{5,15},{16},{3,17},{8,20},
            {9,21},{0,6,10,22},{1,7,11,23},{2,8,12,24},{9,25},{10,26},{15},{16},{3,13,17,27},
            {4,14,18,28},{5,15,19,29},{16},{17},{6,22},
            {7,23},{8,20,24,30},{9,21,25,31},{10,22,26,32},{11,23},{12,24},{15,29},
            {16},{17,27},{22,32},{23},{24,30}};
    private static int timelimit;
    public static boolean [] initialState = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
    public static boolean [] testState = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,true,false,false,false,false};
    public static void main (String [] args) {
        System.out.println("Select search method:\na)Breadth First Search\nb)Depth First Search\nc)Iterative Deepening Search\nd)Depth First Search with Random Selection\ne)Depth First Search with a Node Selection Heuristic");
       // IDS(new State(initialState,0));
        Scanner input = new Scanner(System.in);
        String selection = input.next();
        System.out.println("Enter timelimit in seconds:\n");
        timelimit = input.nextInt() * 1000; //timelimit in milliseconds
        switch (selection) {
            case "a":
                System.out.println("Method: \nBreadth First Search , Time Limit: " + timelimit / 1000 + " seconds");
                BFS(new State(initialState,0));
                break;
            case "b":
                System.out.println("Method: \nDepth First Search , Time Limit: " + timelimit / 1000 + " seconds");
                DFS(new State(initialState,0),false);
                break;
            case "c":
                System.out.println("Method: \nIterative Deepening Search , Time Limit: " + timelimit / 1000 + " seconds");
                IDS(new State(initialState,0));
                break;
            case "d":
                System.out.println("Method: \nDepth First Search with Random Selection , Time Limit: " + timelimit / 1000 + " seconds");
                DFS(new State(initialState,0),true);
                break;
            case "e":
                System.out.println("Method: \nDepth First Search with a Node Selection Heuristic , Time Limit: " + timelimit / 1000 + " seconds");
                Heuristic(new State(initialState,0));
                break;
                default:
                    System.out.println("Option is not in the list!");
                    break;
        }
    }
    public static void printFrontier(ArrayList<State> frontier){
        for(State e : frontier){
            printShape(e);
        }
    }
    private static State subOptimal(ArrayList<State> frontier){
        State maxDepth = frontier.get(0);
        for(State x : frontier){
            if(x.depth > maxDepth.depth){
                maxDepth = x;
            }
        }
        return maxDepth;
    }
    private static void DFS(State initial,boolean mode){
        ArrayList<State> Frontier = new ArrayList<>();
        Frontier.add(initial);
        long time = System.currentTimeMillis(); //time of start of the searching
        long visited = 0;
        while(!Frontier.isEmpty()){ //not empty
            long timeSpent = System.currentTimeMillis() - time;
            if(timeSpent > timelimit ){
                System.out.println("Time limit exceeded.\nTotal time spent finding sub-optimum solution is: "+ timeSpent  + " milliseconds.");
                System.out.println("Sub-optimal solution: ");
                State sub = subOptimal(Frontier);
                printShape(sub);
                System.out.println("\nDepth: "+ sub.depth);
                return;
            }
            State x = Frontier.get(0);
            if (checkStateValidity(x)) {
                System.out.println("Final state found. Total time spent finding optimum solution is: " + timeSpent + " milliseconds");
                System.out.println("Number of nodes expanded: " + visited + "\nFinal state: ");
                printShape(x);
                break;
            }
            Frontier.remove(x);
            if(mode){
                ArrayList<State> children = computeChilds(x);
                Collections.shuffle(children);
                Frontier.addAll(0, children);
            }else{
                Frontier.addAll(0, computeChilds(x));
            }
            visited++;

        }
    }
    private static void BFS(State initial){
        ArrayList<State> Frontier = new ArrayList<>();
        Frontier.add(initial);
        long counterVisited=0;
        long time = System.currentTimeMillis();
        while(!Frontier.isEmpty()){ //not empty
            State x = Frontier.get(0);
            long timeSpent = System.currentTimeMillis() - time;
            if(timeSpent > timelimit){
                System.out.println("Time limit exceeded. Total time spent finding sub optimal solution: "+ timeSpent  + " milliseconds.");
                System.out.println("Sub-optimal solution: ");
                State sub = subOptimal(Frontier);
                printShape(sub);
                System.out.println("\nDepth: " + sub.depth+"\nNumber of nodes expanded: "+ counterVisited);
                return;
            }
            if (checkStateValidity(x)) {
                System.out.println("Final state found. Total time spent finding optimum solution is: " + timeSpent   + " milliseconds.\n+" +
                        "Number of nodes expanded: " + counterVisited);
                printShape(x);
                break;
            }
            Frontier.remove(x);
            try {
                Frontier.addAll(computeChilds(x));
            }catch (OutOfMemoryError e){
                System.out.println("Out of Memory error");
                System.out.println("Time spent: " + timeSpent + "milliseconds");
                State sub = subOptimal(Frontier);
                System.out.println("\nDepth:  "+  sub);
                printShape(sub);
            }
            counterVisited++;
        }
    }
    private static void IDS(State initial){
        State x;
        int c = 0;
        long time =  System.currentTimeMillis();
        while(c < 32){
            long counterVisited=0;
            ArrayList<State> Frontier = new ArrayList<>();
            Frontier.add(initial);
            while(!Frontier.isEmpty()){
                long timeSpent = System.currentTimeMillis() - time;
                x = Frontier.get(0);
                if(x.depth > c){
                    System.out.println("\n Not found at depth " + c + "\tFrontier size: " + Frontier.size());
                    break;
                }
                if(timeSpent > timelimit ){
                    System.out.println("Time limit exceeded. Total time spent finding sub optimal solution: "+ timeSpent  + " milliseconds.");
                    System.out.println("Sub-optimal solution: ");
                    State sub = subOptimal(Frontier);
                    printShape(sub);
                    System.out.println("\nDepth:  " + sub.depth+"\nNumber of nodes expanded: "+ counterVisited);
                    return;
                }

                if(checkStateValidity(x)){
                    time = System.currentTimeMillis() - time;
                    System.out.println("Final state found. Total time spent finding optimum solution is: " + time / 1000 + " seconds.\n"+
                            "Number of nodes expanded: " + counterVisited);
                    printShape(x);
                    break;
                }

                counterVisited++;
                Frontier.remove(x);
                try {
                    Frontier.addAll(computeChilds(x));
                }catch (OutOfMemoryError e){
                    System.out.println("Out of Memory error");
                    System.out.println("Time spent: " + timeSpent + "milliseconds");
                    State sub = subOptimal(Frontier);
                    System.out.println("\nDepth: "+  sub);
                    printShape(sub);
                }

            }
            c++;
            System.out.println("Visited at depth: " + c + "\t total number visited: " + counterVisited);
        }
    }
    private static void Heuristic(State initial){
        ArrayList<State> Frontier = new ArrayList<>();
        Frontier.add(initial);
        long counterVisited=0;
        HashMap<Integer, boolean[]> hmap = new HashMap<Integer, boolean[]>();		// Initialize hashmap, holds board states with integer hashkeys
        while(!Frontier.isEmpty()){ //not empty
            State x = Frontier.get(0);
            if (checkStateValidity(x)) {
                System.out.println("Number of Visited: " + counterVisited);
                System.out.println("Final state found.");
                printShape(x);
                hmap.clear();			// Clear hashmap after use
                break;					// Exit after the first solution is found

            }
            ArrayList<State> Children = new ArrayList<>();
            Children = computeChilds(x);
            Frontier.remove(x);
            int e=0;
            while(e<Children.size()){
                int b = GetHashCode(Children.get(e).locations);			// Get a unique hash code for the next move
                if(hmap.containsKey(b)){								// If the same board is already in the hashmap, there's another exact copy of the board in the frontier
                    e++;				// Iterate to next child
                    continue;			// Exit without adding the duplicate board to the frontier
                }else{
                    hmap.put(b, Children.get(e).locations);		// Add the unique board in the hashmap
                    Frontier.add(0,Children.get(e));			// Add the unique board at the beginning of the frontier
                }
                e++;
            }
            counterVisited++;
            if (counterVisited % 1000 == 0) {
                System.out.println("Number of Visited: " + counterVisited);
            }
        }
    }
    private static void printBoolean(boolean x){
        if(x){
            System.out.print("X");
        }else System.out.print("O");
    }
    private static void printShape(State node){
        System.out.println();
        for(int i = 0; i < 33 ; i++){
            if( i==0 || i == 3 || i == 27 || i == 30){
                System.out.print("\n    ");
                printBoolean(node.locations[i]);
                System.out.print(" ");
            } else if(i == 2 || i == 5 || i == 29 || i == 32){
                printBoolean(node.locations[i]);
                System.out.print("  ");
            }else if(i==6||i==13||i==20){
                System.out.println();
                printBoolean(node.locations[i]);
                System.out.print(" ");
            }else {
                printBoolean(node.locations[i]);
                System.out.print(" ");
            }
        }
    }
    private static boolean checkStateValidity(State node){
       if(node.locations[16]){ ///while middle pawn is in the middle
           for(int i = 0; i < 16 ; i++){ //if any other pawn exist in anywhere, state is not end state.
               if(node.locations[i]){
                   return false;
               }
           }
           for(int j = 17; j < 33 ; j++){
               if(node.locations[j]){
                   return false;
               }
           }
           return true;
       }
       return false;
    }
    private static ArrayList<State> computeChilds (State node){
        ArrayList<State> childs = new ArrayList<>();
        for(int i = 0; i< 33 ; i++){

            if(node.locations[i]){ //if peg exists there
                for(int x : rules[i]){ //for every possible jump location
                    if(!node.locations[x]){ //if destination is empty
                        if((i<6 && x <= 5) || ((i>=6 && i<= 26) && (x>=6 && x<=26) ) || (i>26 && x > 26) ){ //if origin and jump destination is located at the same area
                            if(node.locations[(i+x)/2]){ //if middle is not empty
                                boolean [] temp = node.locations.clone();
                                temp[i] = false;
                                temp[x] = true;
                                temp[(i+x)/2] = false;
                                childs.add(new State(temp,(byte)(node.depth+1)));
                            }
                        }
                        else if((i<6 && x > 6) || (i > 5 && x<6)){ //i is in the top side , x is in the middle side or vice versa
                            if(node.locations[(i+x)/2-1]){ //if middle is not empty
                                boolean[] temp = node.locations.clone();
                                temp[i] = false;
                                temp[x] = true;
                                temp[(i+x)/2-1] = false;
                                childs.add(new State(temp,(byte)(node.depth+1)));
                            }
                        }else if(  (i< 27 && x>26) || (i > 26 &&  x < 27 )  ){ //i is in the middle side, x is in the bottom side or vice versa
                            if(node.locations[(i+x)/2+1]){ //if middle is not empty
                                boolean [] temp = node.locations.clone();
                                temp[i] = false;
                                temp[x] = true;
                                temp[(i+x)/2+1] = false;
                                childs.add(new State(temp,(byte)(node.depth+1)));
                            }
                        }
                    }
                }
            }
        }
        return childs;
    }
    private static int GetHashCode(boolean[] x){			// Generates a unique hashkey for each board
        int result = 29;
        for(int i=32; i>=0; i--)
        {
            if (x[i]) { result++; }
            result *= 23;
        }
        return result;
    }
}