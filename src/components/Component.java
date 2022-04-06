package components;

import java.util.ArrayList;

/**
 * The abstract of al component types which has al the methods to do the overload simulation
 * @author Faisal Alzahrany
 */

public abstract class Component {
    /**
     * String take the component unique name
     */
    private String name;
    /**
     * this is a ready component but it's the source of this component
     */
    private Component source;
    /**
     * An integer save the draw number of the component
     */
    private int draw;
    /**
     * a list of components which they are going to be the loads of this component
     * which this component is going to be the source of these loads.
     */
    private ArrayList<Component> loads= new ArrayList<>();
    /**
     * A string describe the switcher status.
     */
    protected String switcher="off";
    /**
     * A boolean shows the status of engaging
     */
    private boolean engagement;


    /**
     * creating the new component.
     * @param name the component unique name
     * @param source the source of this component
     */
    protected Component(String name, Component source) {
        this.name = name;
        this.source = source;
        Reporter.report(this, Reporter.Msg.CREATING);

    }

    /**
     * this method return the component unique name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * this method attach the new load to the component
     * and engage the new load if the component engaged
     * @param load
     */
    protected void attach(Component load){

        Reporter.report(this,load, Reporter.Msg.ATTACHING);
        if (engagement){
            load.engage();

        }

    }

    /**
     * this method return the source of this component.
     * @return source
     */
    public Component getSource() {
        return source;
    }

    /**
     * this method return the draw of this component.
     * @return draw
     */
    public int getDraw() {
        return draw;
    }

    /**
     * this method set the number for the draw of this component.
     * @param draw
     */
    public void setDraw(int draw) {
        this.draw = draw;
    }

    /**
     * this method Change the amount of current passing through this Component.
     * This method is called by one of this Component's loads when something changes.
     * If the result is a change in current draw
     * in this component, that information is relayed up
     * to the source Component via a call to its changeDraw method.
     * @param delta could be negative draw or positive draw
     */
    protected void changeDraw(int delta){
        if (delta<0){
            if (getDraw()==0){
                return;
            }
            else {
                this.draw += delta;
                Reporter.report(this, Reporter.Msg.DRAW_CHANGE, delta);
                if (getSource() != null) {
                    source.changeDraw(delta);
                }
            }
        }
        else {
            this.draw += delta;
            Reporter.report(this, Reporter.Msg.DRAW_CHANGE, delta);
            if (getSource() != null) {
                source.changeDraw(delta);
            }
        }
    }

    /**
     * This method check the status of the switch in the switchable components.
     * @return
     */
    public boolean isSwitchOn(){
        if (switcher.equals("on")){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Add a new load to this component.
     * @param load
     */
    public void addLoad(Component load){
        loads.add(load);
        attach(load);
    }

    /**
     * The source for this component is now being powered.
     */
    public void engage(){
        engagement=true;
        Reporter.report(this, Reporter.Msg.ENGAGING);
        if (switcher.equals("on")){
            engageLoads();
        }


    }

    /**
     * This Component tells its loads that they can no longer draw current from it.
     */
    public void disengage(){
        engagement=false;
        Reporter.report(this, Reporter.Msg.DISENGAGING);
        disengageLoads();

    }

    /**
     * Inform all Components to which this Component acts as a source that they may now draw current.
     */
    public void engageLoads(){
        for (Component load : loads) {
            load.engage();
        }
    }

    /**
     * Inform all Components to which this Component acts as a source that they will no longer get any current.
     */
    public void disengageLoads(){
        for (Component load : loads) {
            load.disengage();
        }
    }

    /**
     * Is this Component currently being fed power?
     * @return boolean
     */
    public boolean engaged(){
        if (engagement){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Display this (sub)tree vertically, with indentation.
     */
    public void display(){
        if (source==null){
            System.out.println(toString());
        }
        else if (source.getClass().getSimpleName().equals("PowerSource")){
            System.out.println("\t"+toString());
        }
        else if (source.getClass().getSimpleName().equals("CircuitBreaker")){
            System.out.println("\t\t"+toString());
        }
        else if (source.getClass().getSimpleName().equals("Outlet")) {
            System.out.println("\t\t\t" + toString());
        }
        for (Component load: loads){
            load.display();
        }
    }

    /**
     * this method for changing the status of the switchable component to On
     */
    public abstract void turnOn();

    /**
     * this method for changing the status of the switchable component to Off
     */
    public abstract void turnOff();


    /**
     * Description for the component.
     * @return String
     */
    public String toString(){
        return "+ "+this.getClass().getSimpleName()+" "+getName();
    }


}
