package components;

/**
 * A class represent type of the component called Outlet
 * @author Faisal Alzahrany
 */

public class Outlet extends Component {



    /**
     * Method creat the Outlet component.
     * @param name the unique name of the Outlet
     * @param source the source for this Outlet
     */
    public Outlet(String name, Component source) {
        super(name, source);
        this.switcher="on";
        source.addLoad(this);
    }

    /**
     * additional condition for the Outlet when its source is off and the changing is positive
     * @param delta could be negative draw or positive draw
     */
    public void changeDraw(int delta){
        if (delta>0 && !getSource().isSwitchOn()){
            return;
        }
        else {
            super.changeDraw(delta);
        }
    }

    @Override
    public void turnOn() {
    }

    @Override
    public void turnOff() {
    }


    /**
     * Description for the Outlet
     * @return String
     */
    public String toString(){
        String sp=super.toString();
        return sp+"(draw "+getDraw()+")";
    }
}
