public class State {
    public boolean [] locations;
    public int depth;
    public State parent;
    public State (boolean [] newChild,int depth){
        locations = newChild.clone();
        this.depth = depth;
    }
}
