package components;

/**
 * A class represent type of the component called Appliance
 * @author Faisal Alzahrany
 */

public class Appliance extends Component {
    /**
     * An integer take the rating for this Appliance
     * to know how much draw is going to change
     */
    private int rating;


    /**
     * Method creat the Appliance component.
     * @param name the unique name of the Appliance
     * @param source the source for this Appliance
     * @param rating An integer take the rating for this Appliance
     *               to know how much draw is going to change
     */
    public Appliance(String name, Component source, int rating) {
        super(name, source);
        source.addLoad(this);
        this.rating=rating;

    }

    /**
     * this method return the rating of this Appliance
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * A method turn on the Appliance and set the draw of it from the rating
     * and change the draw of its source if the CircuitBreaker is on
     * or if it connected directly to the SourcePower
     */
    @Override
    public void turnOn(){
        this.switcher="on";
        setDraw(getRating());
        Reporter.report(this, Reporter.Msg.SWITCHING_ON);
        getSource().changeDraw(rating);


    }

    /**
     * this method turn off the Appliance and set its draw to 0,
     * then it change its source draw to the top source
     */
    @Override
    public void turnOff(){
        this.switcher="off";
        Reporter.report(this, Reporter.Msg.SWITCHING_OFF);
        setDraw(0);
        getSource().changeDraw(-rating);



    }

    /**
     * this method engage the Appliance and if it has been On then it change draw
     */
    public void engage(){
        super.engage();
        if (isSwitchOn()){
            changeDraw(rating);
        }

    }

    /**
     * this method disengage the Appliance and change draw
     */
    public void disengage(){
        super.disengage();
        changeDraw(-rating);
    }



    /**
     * Description for the Appliance
     * @return String
     */
    @Override
    public String toString() {
        String sp=super.toString();
        return sp+"("+switcher+"; rating "+rating+")";
    }
}
