package components;

/**
 * A class represent type of the component called CircuitBreaker
 * @author Faisal Alzahrany
 */

public class CircuitBreaker extends Component{
    /**
     * The highest amount of draw that the CircuitBreaker can handle
     */
    private int limit;
    /**
     * a helper variable to save the last draw was going to the PowerSource before turning off the CircuitBreaker
     */
    private int deltaOff;


    /**
     * A method creat the CircuitBreaker
     * @param name the unique name for the CircuitBreaker
     * @param source the power source of the CircuitBreaker
     * @param limit The highest amount of draw that the CircuitBreaker can handle
     */
    public CircuitBreaker(String name, Component source, int limit) {
        super(name, source);
        source.addLoad(this);
        this.limit= limit;
    }

    /**
     * this method return the limit of the CircuitBreaker.
     * @return limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * this method for changing the status of the switchable component to On
     */
    @Override
    public void turnOn(){
        this.switcher="on";
        Reporter.report(this, Reporter.Msg.SWITCHING_ON);
        engageLoads();
    }

    /**
     * this method for changing the status of the switchable component to Off
     */
    @Override
    public void turnOff(){
        this.switcher="off";
        Reporter.report(this, Reporter.Msg.SWITCHING_OFF);
        setDraw(0);
        getSource().changeDraw(-deltaOff);
        disengageLoads();
    }

    /**
     * additional conditions for changeDraw when the CircuitBreaker is off
     * or when it's reach the limit.
     * @param delta could be negative draw or positive draw
     */
    public void changeDraw(int delta){
        if (!isSwitchOn()){
            return;
        }
        else if (limit<delta+getDraw()){
            this.deltaOff=getDraw();
            setDraw(getDraw()+delta);
            Reporter.report(this,Reporter.Msg.BLOWN, getDraw());
            turnOff();
        }
        else {
            super.changeDraw(delta);
        }
    }

    /**
     * Description for the CircuitBreaker.
     * @return String
     */
    public String toString(){
        String sp=super.toString();
        return sp+"("+switcher+"; "+getDraw()+"; "+getLimit()+")";
    }
}
