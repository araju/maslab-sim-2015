package firmware_interfaces;

import core.Sonar;
import core.Config;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SonarInterface extends Thread implements SerialPortEventListener {
    SerialPort serialPort;
    /** The port we're normally going to use. */
    private static final String PORT_NAMES[] = { "/dev/tty.usbmodem1411", // Mac
                                                                          // OS
                                                                          // X
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows
    };
    /**
     * A BufferedReader which will be fed by a InputStreamReader converting the
     * bytes into characters making the displayed results codepage independent
     */
    private BufferedReader input;
    /** The output stream to the port */
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 2000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;
    public int ultNum = 0;
    public int numUlts = 7;
    public ArrayList<Sonar> sonars;
    public ArrayList<Sonar> internalSonars;
	public boolean ready;

    public SonarInterface() {
    	ready = false;
        CommPortIdentifier portId = null;
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

        // First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    System.out.println("found it" + portName);
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            //initialize Sonars
            Sonar tempSonar;
            for (int i = 0; i < numUlts; i++){
            	tempSonar = new Sonar(Config.sonarPositions[i]);
            	sonars.add(tempSonar);
            	tempSonar = new Sonar(Config.sonarPositions[i]);
            	internalSonars.add(tempSonar);

            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String in = "";
                ultNum = 0;
                while (!(in.equals("J"))) {
                    ultNum++;
                    in = input.readLine();
                    if (!(in.equals("J"))) {
                        int cm = Integer.parseInt(in);
                        cm /= 58;
                        internalSonars.get(ultNum).setMeasurement(cm, System.currentTimeMillis());
                    }
                }
                
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other
        // ones.
    }
    
    public ArrayList<Sonar> getSonars(){
    	return sonars;
    }
    
    public void sample() {
        Sonar internal;
        for (int i = 0; i < internalSonars.size(); i++) {
            internal = internalSonars.get(i);
            double meas = sonars.get(i).filter.update(internal.meas);
            sonars.get(i).setMeasurement(meas, internal.time);
        }
    }
    
    public void run(){
    	while (true){
    		sample();
    		ready = true;
    	}
    }
}
