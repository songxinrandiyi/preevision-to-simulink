package preevisiontosimulink.generator;

import com.mathworks.engine.*;

public class ShortCircuitSimulation {
	public static void main(String[] args) {
		try {
			// Start the MATLAB Engine
			MatlabEngine matlab = MatlabEngine.startMatlab();

			// Create the Simulink model
			matlab.eval("model = new_system('ShortCircuitModel', 'Model')");

			// Add blocks to the model
			matlab.eval(
					"add_block('fl_lib/Electrical/Electrical Sources/DC Voltage Source', 'ShortCircuitModel/VoltageSource')");
			matlab.eval("add_block('fl_lib/Electrical/Electrical Elements/Resistor', 'ShortCircuitModel/Resistor')");
			matlab.eval(
					"add_block('fl_lib/Electrical/Electrical Elements/Electrical Reference', 'ShortCircuitModel/Ground')");

			// Set parameters for the blocks
			matlab.eval("set_param('ShortCircuitModel/VoltageSource', 'v0', '10')");
			matlab.eval("set_param('ShortCircuitModel/Resistor', 'R', '100')");

			// Get the position of ports
			matlab.eval("position_voltageSource = get_param('ShortCircuitModel/VoltageSource', 'PortConnectivity')");
			matlab.eval("position_resistor = get_param('ShortCircuitModel/Resistor', 'PortConnectivity')");
			matlab.eval("position_ground = get_param('ShortCircuitModel/Ground', 'PortConnectivity')");

			// Connect the blocks in the model
			matlab.eval(
					"add_line('ShortCircuitModel', [ position_voltageSource(1).Position ; position_resistor(1).Position])");
			matlab.eval(
					"add_line('ShortCircuitModel', [ position_resistor(2).Position ; position_ground(1).Position])");

			// Save and close the model
			matlab.eval("save_system('ShortCircuitModel', 'ShortCircuitModel.slx')");
			matlab.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
