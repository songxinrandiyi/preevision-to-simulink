package preevisiontosimulink.generator;

import com.mathworks.engine.MatlabEngine;

public class SimulinkModelGenerator {
    public static void generateModel() {
        try {
            // Start the MATLAB engine
            MatlabEngine matlab = MatlabEngine.startMatlab();

            // Define the model name
            String modelName = "mySimulinkModel";

            // Generate the Simulink model (example)
            matlab.eval(modelName + " = new_system('" + modelName + "', 'Model')");
            matlab.eval("add_block('simulink/Sources/Sine Wave', '" + modelName + "/Sine')");
            matlab.eval("add_block('simulink/Math Operations/Gain', '" + modelName + "/Gain')");
            matlab.eval("add_block('simulink/Commonly Used Blocks/Scope', '" + modelName + "/Scope')");
            matlab.eval("add_line('" + modelName + "', 'Sine/1', 'Gain/1')");
            matlab.eval("add_line('" + modelName + "', 'Gain/1', 'Scope/1')");
            matlab.eval("set_param('" + modelName + "/Gain', 'Gain', '2')");

            // Save the model
            String modelFilePath = "" + modelName + ".slx";
            matlab.eval("save_system('" + modelName + "', '" + modelFilePath + "')");

            System.out.println("Simulink model generated: " + modelName);

            // Close the MATLAB engine
            matlab.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}