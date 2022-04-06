package components.testing;

import components.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * the main class to start the Overload simulation
 *
 * @author Faisal Alzahrany
 */
public class Overload {
    private ArrayList<PowerSource> power=new ArrayList<>();
    private HashMap<String, Component> components=new HashMap<>();

    public static final int BAD_ARGS = 1;
    public static final int FILE_NOT_FOUND = 2;
    public static final int BAD_FILE_FORMAT = 3;
    public static final int UNKNOWN_COMPONENT = 4;
    public static final int REPEAT_NAME = 5;
    public static final int UNKNOWN_COMPONENT_TYPE = 6;
    public static final int UNKNOWN_USER_COMMAND = 7;
    public static final int UNSWITCHABLE_COMPONENT = 8;

    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String[] NO_STRINGS = new String[ 0 ];

    private static final String PROMPT = "? ";

    static {
        Reporter.addError(
                BAD_ARGS, "Usage: java components.Overload <configFile>" );
        Reporter.addError( FILE_NOT_FOUND, "Config file not found" );
        Reporter.addError( BAD_FILE_FORMAT, "Error in config file" );
        Reporter.addError(
                UNKNOWN_COMPONENT,
                "Reference to unknown component in config file"
        );
        Reporter.addError(
                REPEAT_NAME,
                "Component name repeated in config file"
        );
        Reporter.addError(
                UNKNOWN_COMPONENT_TYPE,
                "Reference to unknown type of component in config file"
        );
        Reporter.addError(
                UNKNOWN_USER_COMMAND,
                "Unknown user command"
        );
    }

    /**
     * A method to read the text file config
     * @param filename file name
     * @throws FileNotFoundException
     */
    public void readFileConfig(String filename) throws FileNotFoundException {

        Scanner read = new Scanner(new File(filename));
        while (read.hasNextLine()){
            String line=read.nextLine();
            String[] words=line.split(" ");
            if ((!Character.isUpperCase(words[0].charAt(0))) || (Character.isDigit(words[0].charAt(0)))){
                Reporter.usageError(3);
                Reporter.usageError(6);
            }
            else if (components.containsKey(words[1])){
                Reporter.usageError(3);
                Reporter.usageError(5);
            }
            else {
                List<String> newWords=Arrays.asList(words);
                creatComponent(newWords);
            }
        }
    }


    /**
     * A method to creat the components that aske for from config text file or from the user command
     * @param component list of string include the type and the name of the component and maybe the source and the limit or the rate
     */
    public void creatComponent(List<String> component){

        if (component.get(0).equals("PowerSource")){
            if (component.size()!=2){
                Reporter.usageError(3);
            }
            else {
                PowerSource p=new PowerSource(component.get(1));
                components.put(component.get(1),p);
                power.add(p);
            }

        }
        else if (component.get(0).equals("CircuitBreaker")){
            if (component.size()!=4){
                Reporter.usageError(3);
            }
            else {
                if (!components.containsKey(component.get(2))) {
                    Reporter.usageError(4);
                }
                else {
                    CircuitBreaker c = new CircuitBreaker(component.get(1), components.get(component.get(2)), Integer.parseInt(component.get(3)));
                    components.put(component.get(1), c);
                }
            }
        }
        else if (component.get(0).equals("Outlet")){
            if (component.size()!=3){
                Reporter.usageError(3);
            }
            else {
                if (!components.containsKey(component.get(2))){
                    Reporter.usageError(4);
                }
                else {
                    Outlet o = new Outlet(component.get(1), components.get(component.get(2)));
                    components.put(component.get(1), o);
                }
            }
        }
        else if (component.get(0).equals("Appliance")) {
            if (component.size() != 4) {
                Reporter.usageError(3);
            } else {
                if (!components.containsKey(component.get(2))){
                    Reporter.usageError(4);
                }
                else {
                    Appliance a = new Appliance(component.get(1), components.get(component.get(2)), Integer.parseInt(component.get(3)));
                    components.put(component.get(1), a);
                }
            }
        }
    }


    /**
     * A method to do the commands that the user input
     * @param command type of command
     * @param line the thing that command going to do on it
     */
    public void overloadCommand(String command, List<String> line){

        if (command.equals("display")){
            for (Component component:power){
                component.display();
            }
        }


        if (command.equals("toggle")){
            if (line.size()!=1){
                Reporter.usageError(7);
            }
            else {
                Component component=components.get(line.get(0));

                if (component.isSwitchOn()) {
                    component.turnOff();
                }
                else {
                    component.turnOn();
                }
            }
        }
        else if (command.equals("connect")){
            creatComponent(line);
        }
    }

    /**
     * the main method that start the Overload simulation
     * @param args
     */
    public static void main( String[] args ) {
        Scanner in=new Scanner(System.in);
        System.out.println( "Overload Project, CS2" );
        Overload overload=new Overload();
        try {
            overload.readFileConfig(args[0]);
        }
        catch (IndexOutOfBoundsException e){
            Reporter.usageError(1);
        }
        catch (FileNotFoundException e) {
            Reporter.usageError(2);
        }
        System.out.println(overload.components.size()+" components created.");
        System.out.println("Starting up the main circuit(s).");
        for (PowerSource supplie: overload.power){
            Reporter.report(supplie, Reporter.Msg.POWERING_UP);
            supplie.engage();
        }

        while (true){
            System.out.print(PROMPT);
            String words=in.nextLine();
            String[] command=words.split(" ");
            List<String> line=new ArrayList<String>();
            for (String word:command){
                if (!word.equals(command[0])){
                    line.add(word);
                }
            }
            if (command[0].isEmpty()){
                Reporter.usageError(7);
            }
            else if (command[0].equals("quit")){
                System.out.println(" -> "+command[0]+line.toString());
                break;
            }
            else {
                System.out.println(" -> "+command[0]+line.toString());
                overload.overloadCommand(command[0], line);
            }

        }
    }
}
