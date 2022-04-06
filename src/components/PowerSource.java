package components;

/**
 * A class represent type of the component called PowerSource
 * which is the one generates the power.
 * @author Faisal Alzahrany
 */

public class PowerSource extends Component {

    /**
     * Method creat the PowerSource component.
     * @param name the unique name of the PowerSource
     */
    public PowerSource(String name) {
        super(name, null);
        this.switcher="on";

    }


    @Override
    public void turnOn() {
    }

    @Override
    public void turnOff() {
    }

    /**
     * Description for the component.
     * @return String
     */
    public String toString(){
        String sp=super.toString();
        return sp+"(draw "+getDraw()+")";
    }


}
