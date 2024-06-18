package preevisiontosimulink.generator;

import com.mathworks.engine.*;

public class SimpleCircuitSimulation {
	public static void main(String[] args) {
		try {
			// Start the MATLAB Engine
			MatlabEngine matlab = MatlabEngine.startMatlab();

			// Create the Simulink model
			matlab.eval("model = new_system('simpleCircuit', 'Model')");

			// Add blocks to the model
			matlab.eval("add_block('fl_lib/Electrical/Electrical Sources/DC Voltage Source', 'simpleCircuit/DC')");
			matlab.eval("add_block('fl_lib/Electrical/Electrical Elements/Resistor', 'simpleCircuit/r1')");
			matlab.eval("add_block('fl_lib/Electrical/Electrical Elements/Electrical Reference', 'simpleCircuit/ref1')");
			matlab.eval("add_block('nesl_utility/Solver Configuration', 'simpleCircuit/solver1')");

			// Set parameters for the blocks
			matlab.eval("set_param('simpleCircuit/r1', 'R', '50')");
			matlab.eval("set_param('simpleCircuit/DC', 'v0', '10')");

			// Connect the blocks in the model
			matlab.eval("add_line('simpleCircuit', 'DC/LConn1', 'r1/LConn1', 'autorouting', 'on')");
			matlab.eval("add_line('simpleCircuit', 'r1/RConn1', 'ref1/LConn1', 'autorouting', 'on')");
			matlab.eval("add_line('simpleCircuit', 'DC/RConn1', 'ref1/LConn1', 'autorouting', 'on')");
			matlab.eval("add_line('simpleCircuit', 'solver1/RConn1', 'ref1/LConn1', 'autorouting', 'on')");

			// Save and close the model
			matlab.eval("save_system('simpleCircuit', 'simpleCircuit.slx')");
			matlab.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
